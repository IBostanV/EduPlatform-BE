package com.play.quiz.cosmetic;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.play.quiz.domain.Account;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cosmetics: bought with coins (or won on the season pass), then worn. A player wears at most one
 * frame and one name colour, kept on the account so every place that shows them has it to hand.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class CosmeticService {

    private final OwnedItemRepository ownedItemRepository;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    /** An item as the shop shows it to the reader. */
    public record ItemView(String code, Cosmetic.Type type, Integer price, String color, boolean seasonal,
                           boolean owned, boolean worn) {}

    /** The whole catalog, with what the reader owns and wears, and their coins to spend. */
    public record Catalog(int coins, List<ItemView> items) {}

    @Transactional(readOnly = true)
    public Catalog catalog() {
        Account me = currentAccount();
        Set<String> owned = ownedItemRepository.findByAccountId(me.getAccountId()).stream()
                .map(OwnedItem::getItemCode)
                .collect(Collectors.toSet());
        return new Catalog(accountRepository.findCoins(me.getAccountId()), Arrays.stream(Cosmetic.values())
                .map(item -> new ItemView(item.name(), item.getType(), item.getPrice(), item.getColor(), item.seasonal(),
                        owned.contains(item.name()), worn(me, item)))
                .toList());
    }

    /** Pays and takes the item. The coins come off first, so a refusal after hands them back. */
    @Transactional
    public Catalog buy(final String code) {
        Cosmetic item = item(code);
        if (item.seasonal()) {
            throw new IllegalArgumentException(ServerText.t("err_cosmetic_season_only", "This one is won on the season pass, not bought"));
        }
        Long me = currentAccount().getAccountId();
        if (ownedItemRepository.existsByAccountIdAndItemCode(me, code)) {
            throw new IllegalArgumentException(ServerText.t("err_cosmetic_owned", "You already own this"));
        }
        if (accountRepository.spendCoins(me, item.getPrice()) != 1) {
            throw new IllegalArgumentException(ServerText.t("err_not_enough_coins", "Not enough coins: this costs {{price}}", "price", item.getPrice()));
        }
        grant(me, item);
        log.info("Account {} bought {} for {} coins", me, code, item.getPrice());
        return catalog();
    }

    /** Wears an owned item, in place of whatever of its kind was worn. */
    @Transactional
    public Catalog wear(final String code) {
        Cosmetic item = item(code);
        Long me = currentAccount().getAccountId();
        if (!ownedItemRepository.existsByAccountIdAndItemCode(me, code)) {
            throw new IllegalArgumentException(ServerText.t("err_cosmetic_not_owned", "You do not own this yet"));
        }
        setWorn(me, item.getType(), code);
        return catalog();
    }

    /** Takes off whatever of this kind is worn. */
    @Transactional
    public Catalog takeOff(final Cosmetic.Type type) {
        setWorn(currentAccount().getAccountId(), type, null);
        return catalog();
    }

    /** Gives an item without payment: a season-pass reward. Owning it already changes nothing. */
    @Transactional
    public boolean grant(final Long accountId, final Cosmetic item) {
        if (ownedItemRepository.existsByAccountIdAndItemCode(accountId, item.name())) return false;
        ownedItemRepository.save(OwnedItem.builder()
                .accountId(accountId)
                .itemCode(item.name())
                .acquiredDate(LocalDateTime.now())
                .build());
        return true;
    }

    private void setWorn(final Long accountId, final Cosmetic.Type type, final String code) {
        if (type == Cosmetic.Type.FRAME) accountRepository.setEquippedFrame(accountId, code);
        else accountRepository.setEquippedNameColor(accountId, code);
        log.info("Account {} wears {} as its {}", accountId, code, type);
    }

    private static boolean worn(final Account account, final Cosmetic item) {
        String worn = item.getType() == Cosmetic.Type.FRAME ? account.getEquippedFrame() : account.getEquippedNameColor();
        return Objects.equals(worn, item.name());
    }

    private static Cosmetic item(final String code) {
        return Cosmetic.of(code).orElseThrow(() ->
                new IllegalArgumentException(ServerText.t("err_cosmetic_unknown", "No such item: {{code}}", "code", code)));
    }

    private Account currentAccount() {
        // Read through JPA, not the JDBC lookup: the worn items and the coins have to be current.
        return accountRepository.findById(userService.findByEmail(authenticationFacade.getPrincipal().getUsername())
                .getAccountId()).orElseThrow();
    }
}
