package com.play.quiz.appearance;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.UserService;
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The signed-in player's own look of the site, kept on their account as JSON. */
@Log4j2
@Service
@RequiredArgsConstructor
public class AppearanceService {

    /** The most an uploaded background may weigh. The browser scales and compresses it well under this. */
    public static final int MAX_BACKGROUND_BYTES = 1024 * 1024;

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;
    private final UserBackgroundRepository backgroundRepository;

    /** A stored picture as it goes out. */
    public record Picture(String contentType, byte[] bytes) {
    }

    @Transactional(readOnly = true)
    public Appearance get() {
        Long accountId = currentAccountId();
        Appearance stored = read(accountId);
        // The upload's address comes from its own row, so it is always the current one; a custom
        // background whose picture has gone is the site's own again.
        String upload = backgroundRepository.findPublicId(accountId).orElse(null);
        Appearance appearance = stored.withCustomBackground(upload);
        return Appearance.CUSTOM.equals(appearance.background()) && Objects.isNull(upload)
                ? appearance.withBackground(null) : appearance;
    }

    private Appearance read(final Long accountId) {
        String stored = accountRepository.findAppearance(accountId);
        if (Objects.isNull(stored) || stored.isBlank()) {
            return Appearance.defaults();
        }
        try {
            return objectMapper.readValue(stored, Appearance.class);
        } catch (JsonProcessingException exception) {
            // A value that no longer reads (a setting renamed since) is the site as it comes,
            // not an error on every page the player opens.
            log.warn("Unreadable appearance on account {}, using the defaults: {}",
                    accountId, exception.getMessage());
            return Appearance.defaults();
        }
    }

    /** Replaces the lot; the settings left out (null) go back to how the site comes. */
    @Transactional
    public Appearance save(final Appearance appearance) {
        Appearance settings = (Objects.isNull(appearance) ? Appearance.defaults() : appearance.validated())
                .withCustomBackground(null);
        Long accountId = currentAccountId();
        if (Appearance.CUSTOM.equals(settings.background()) && backgroundRepository.findPublicId(accountId).isEmpty()) {
            throw new IllegalArgumentException(ServerText.t("err_background_not_uploaded", "Upload a picture first"));
        }
        try {
            // Nothing changed is stored as nothing, so a full reset leaves the column as it started.
            accountRepository.setAppearance(accountId,
                    settings.isDefault() ? null : objectMapper.writeValueAsString(settings));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(ServerText.t("err_appearance_not_stored", "Could not store the appearance"), exception);
        }
        log.info("Account {} saved appearance (defaults: {})", accountId, settings.isDefault());
        return get();
    }

    /**
     * The player's own background, replacing the one they had, and in use from now on. Only a
     * JPEG, PNG or WebP is taken, known by its first bytes rather than by what the browser says
     * it is: the picture is served back to browsers, so it must be a picture (an SVG could carry
     * a script, and is not taken).
     */
    @Transactional
    public Appearance uploadBackground(final byte[] image) {
        if (Objects.isNull(image) || image.length == 0 || image.length > MAX_BACKGROUND_BYTES) {
            throw new IllegalArgumentException(ServerText.t("err_background_size",
                    "The picture must be smaller than {{size}} MB", "size", MAX_BACKGROUND_BYTES / (1024 * 1024)));
        }
        String type = imageType(image).orElseThrow(() -> new IllegalArgumentException(
                ServerText.t("err_background_type", "The picture must be a JPEG, PNG or WebP image")));
        Long accountId = currentAccountId();
        // A new address with every upload: the old one can stay in browsers' caches for good.
        backgroundRepository.save(new UserBackground(accountId, UUID.randomUUID().toString(), type, image,
                LocalDateTime.now()));
        log.info("Account {} uploaded a background ({} bytes, {})", accountId, image.length, type);
        return save(read(accountId).withBackground(Appearance.CUSTOM));
    }

    /** Deletes the player's own background; if it was in use, the site's logo is back. */
    @Transactional
    public Appearance removeBackground() {
        Long accountId = currentAccountId();
        backgroundRepository.findById(accountId).ifPresent(backgroundRepository::delete);
        Appearance stored = read(accountId);
        return Appearance.CUSTOM.equals(stored.background()) ? save(stored.withBackground(null)) : get();
    }

    /** Anybody's uploaded background by its address, for the browser to draw. */
    @Transactional(readOnly = true)
    public Optional<Picture> background(final String publicId) {
        return backgroundRepository.findByPublicId(publicId)
                .map(background -> new Picture(background.getContentType(), background.getImage()));
    }

    private static Optional<String> imageType(final byte[] image) {
        if (startsWith(image, 0xFF, 0xD8, 0xFF)) return Optional.of("image/jpeg");
        if (startsWith(image, 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A)) return Optional.of("image/png");
        if (startsWith(image, 'R', 'I', 'F', 'F') && image.length > 12
                && Arrays.equals(Arrays.copyOfRange(image, 8, 12), new byte[]{'W', 'E', 'B', 'P'})) {
            return Optional.of("image/webp");
        }
        return Optional.empty();
    }

    private static boolean startsWith(final byte[] image, final int... prefix) {
        if (image.length < prefix.length) return false;
        for (int i = 0; i < prefix.length; i++) {
            if ((image[i] & 0xFF) != prefix[i]) return false;
        }
        return true;
    }

    private Long currentAccountId() {
        return userService.findByEmail(authenticationFacade.getPrincipal().getUsername()).getAccountId();
    }
}
