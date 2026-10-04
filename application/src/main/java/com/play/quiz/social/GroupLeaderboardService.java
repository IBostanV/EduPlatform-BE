package com.play.quiz.social;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.play.quiz.domain.UserQuizHistory;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.AccountRepository;
import com.play.quiz.repository.UserGroupRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.util.ServerText;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A chat group's table: how its members played over the last week or month, most right answers
 * first. Read off their quiz history; custom quizzes count for nothing, as with experience.
 */
@Service
@RequiredArgsConstructor
public class GroupLeaderboardService {

    public enum Period {
        WEEK(7), MONTH(30);

        final int days;

        Period(final int days) {
            this.days = days;
        }
    }

    public record Row(int rank, UserSummary user, int quizzes, int rightAnswers, int totalAnswers, int accuracy) {}

    private final UserGroupRepository userGroupRepository;
    private final UserQuizHistoryRepository historyRepository;
    private final AccountRepository accountRepository;
    private final AuthenticationFacade authenticationFacade;

    @Transactional(readOnly = true)
    public List<Row> leaderboard(final Long groupId, final Period period) {
        if (!userGroupRepository.isMember(groupId, authenticationFacade.getPrincipal().getUsername())) {
            throw new IllegalArgumentException(ServerText.t("err_not_in_group", "You are not in that group"));
        }
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusDays(period.days);

        // ponytail: one history read per member; groups are a handful of friends. One grouped
        // query over all members would replace it if groups grow into the hundreds.
        List<Row> rows = accountRepository.findAllById(userGroupRepository.findUserIdsByUserGroupId(groupId)).stream()
                .map(member -> {
                    List<UserQuizHistory> runs = historyRepository.findRunsBetween(member.getAccountId(), from, to).stream()
                            .filter(run -> Objects.nonNull(run.getTotalAnswers()) && !run.getQuiz().isCustom())
                            .toList();
                    int right = runs.stream().mapToInt(UserQuizHistory::getRightAnswers).sum();
                    int total = runs.stream().mapToInt(UserQuizHistory::getTotalAnswers).sum();
                    return new Row(0, UserSummary.withPhoto(member), runs.size(), right, total,
                            total == 0 ? 0 : Math.round(right * 100f / total));
                })
                .sorted(Comparator.comparingInt(Row::rightAnswers).reversed()
                        .thenComparing(Comparator.comparingInt(Row::accuracy).reversed())
                        .thenComparing(row -> row.user().displayName()))
                .toList();

        return java.util.stream.IntStream.range(0, rows.size())
                .mapToObj(index -> {
                    Row row = rows.get(index);
                    return new Row(index + 1, row.user(), row.quizzes(), row.rightAnswers(), row.totalAnswers(), row.accuracy());
                })
                .toList();
    }
}
