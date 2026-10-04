package com.play.quiz.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.play.quiz.domain.Account;
import com.play.quiz.domain.CustomAnswer;
import com.play.quiz.domain.CustomQuestion;
import com.play.quiz.domain.Quiz;
import com.play.quiz.domain.QuizInvite;
import com.play.quiz.domain.QuizType;
import com.play.quiz.dto.CustomQuizDto;
import com.play.quiz.dto.CustomQuizPlayDto;
import com.play.quiz.dto.QuizDto;
import com.play.quiz.dto.wrapper.HistoryAnswer;
import com.play.quiz.enums.UserRole;
import com.play.quiz.exception.RecordNotFoundException;
import com.play.quiz.exception.UserNotFoundException;
import com.play.quiz.mapper.QuizMapper;
import com.play.quiz.record.CustomQuizSummary;
import com.play.quiz.record.QuizInvitation;
import com.play.quiz.record.UserSummary;
import com.play.quiz.repository.CategoryRepository;
import com.play.quiz.repository.CustomQuestionRepository;
import com.play.quiz.repository.QuizInviteRepository;
import com.play.quiz.repository.QuizRepository;
import com.play.quiz.repository.QuizTypeRepository;
import com.play.quiz.repository.UserQuizHistoryRepository;
import com.play.quiz.repository.UserRepository;
import com.play.quiz.security.AuthenticationFacade;
import com.play.quiz.service.CustomQuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomQuizServiceImpl implements CustomQuizService {

    private static final String INPUT = "INPUT";
    private static final String IN_ORDER = "IN_ORDER";

    /**
     * The quiz types a player can pick for questions they write, and how many right answers and
     * wrong options a question needs in each. The rest (map, value range, drag and drop) need data
     * a written question does not have. The create page offers the same names.
     */
    private record QuestionShape(int minRight, int maxRight, int minWrong, int maxWrong) {
    }

    private static final Map<String, QuestionShape> CUSTOM_QUIZ_TYPES = Map.of(
            "SINGLE_CHOICE", new QuestionShape(1, 1, 1, 10),
            "MULTIPLE_CHOICE", new QuestionShape(1, 10, 1, 10),
            "ONE_FROM_TWO", new QuestionShape(1, 1, 1, 1),
            // The right answers are the spellings accepted when typed.
            INPUT, new QuestionShape(1, 10, 0, 0),
            // The right answers, in the right order.
            IN_ORDER, new QuestionShape(2, 10, 0, 0));

    private final AuthenticationFacade authenticationFacade;
    private final CategoryRepository categoryRepository;
    private final CustomQuestionRepository customQuestionRepository;
    private final QuizInviteRepository quizInviteRepository;
    private final QuizMapper quizMapper;
    private final QuizRepository quizRepository;
    private final QuizTypeRepository quizTypeRepository;
    private final UserQuizHistoryRepository userQuizHistoryRepository;
    private final UserRepository userRepository;

    /**
     * Saves the quiz, its questions with their answers, and an invite for each person asked. Who
     * sent the invites is the audited CREATED_BY on their rows.
     */
    @Override
    @Transactional
    public QuizDto create(final CustomQuizDto customQuizDto) {
        // Checked before anything is saved: a question that does not fit its type is unplayable.
        QuizType quizType = quizTypeRepository.findById(customQuizDto.getQuizTypeId())
                .filter(type -> CUSTOM_QUIZ_TYPES.containsKey(type.getName()))
                .orElseThrow(() -> new IllegalArgumentException("This quiz type is not available for a custom quiz"));
        checkQuestionsFit(quizType, customQuizDto.getQuestions());

        Account creator = getCurrentAccount();
        List<CustomQuizDto.CustomQuestionDto> questions = customQuizDto.getQuestions();

        // A quiz belongs to one category; the first chosen stands for it.
        // ponytail: single home category, add Q_QUIZ_CATEGORY if a quiz must keep them all.
        Long homeCategoryId = customQuizDto.getCategoryIds().iterator().next();

        // The questions first: the quiz lists their ids. Here QUESTION_IDS are Q_CUSTOM_QUESTION ids.
        Set<Long> questionIds = new LinkedHashSet<>();
        for (int position = 0; position < questions.size(); position++) {
            questionIds.add(customQuestionRepository.save(toQuestion(questions.get(position), position)).getQuestionId());
        }

        Quiz quiz = quizRepository.save(Quiz.builder()
                .category(categoryRepository.getReferenceById(homeCategoryId))
                .type(quizType)
                .custom(true)
                .questionsCount(questions.size())
                .questionTime(customQuizDto.getTimePerQuestion())
                .questionIds(questionIds)
                .createdDate(LocalDateTime.now())
                .build());

        invite(quiz, creator, customQuizDto.getInvitedUserIds());
        log.info("Created custom quiz id: {}, type: {}, category: {}, questions: {}",
                quiz.getQuizId(), quizType.getName(), homeCategoryId, questionIds.size());

        return quizMapper.toDto(quiz).toBuilder()
                // Asked for per question, spent over the whole run.
                .quizTime(questions.size() * customQuizDto.getTimePerQuestion())
                .build();
    }

    /**
     * For its creator and the people invited only: its questions were written for them, not for
     * everyone who can guess an id.
     */
    @Override
    @Transactional(readOnly = true)
    public CustomQuizPlayDto getForPlay(final Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .filter(Quiz::isCustom)
                .orElseThrow(() -> new RecordNotFoundException("No custom quiz with id: " + quizId));

        // Its creator and the admins, plus the people invited to play it.
        String username = authenticationFacade.getPrincipal().getUsername();
        if (!canManage(quiz, username)
                && !quizInviteRepository.existsByQuiz_QuizIdAndAccount_Email(quizId, username)) {
            throw new AccessDeniedException("Only the quiz's creator and the people invited can play it");
        }

        boolean typed = INPUT.equals(quiz.getType().getName());
        List<CustomQuizPlayDto.Question> questions = questionsOf(quiz)
                .stream()
                .map(question -> new CustomQuizPlayDto.Question(question.getQuestionId(), question.getContent(),
                        typed ? List.of() : shuffledOptions(question)))
                .toList();

        QuizDto quizDto = quizMapper.toDto(quiz).toBuilder()
                .quizTime(quiz.getQuestionsCount() * quiz.getQuestionTime())
                .build();
        return new CustomQuizPlayDto(quizDto, questions);
    }

    // ponytail: one exists-query per invitation for `played`; fold it into the invites query if
    // invitation lists ever get long.
    @Override
    @Transactional(readOnly = true)
    public List<QuizInvitation> getMyInvitations() {
        String username = authenticationFacade.getPrincipal().getUsername();
        return quizInviteRepository.findByInvitedEmail(username).stream()
                .map(invite -> {
                    Quiz quiz = invite.getQuiz();
                    return new QuizInvitation(
                            quiz.getQuizId(),
                            quiz.getType().getName(),
                            quiz.getQuestionsCount(),
                            quiz.getQuestionTime(),
                            // Who sent it is the invite's audited creator.
                            Optional.ofNullable(invite.getCreatedBy()).map(UserSummary::of).orElse(null),
                            invite.getCreatedDate(),
                            userQuizHistoryRepository.existsByQuiz_QuizIdAndAccount_Email(quiz.getQuizId(), username));
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomQuizSummary> getMyQuizzes() {
        String username = authenticationFacade.getPrincipal().getUsername();
        return summarise(quizRepository.findByCustomTrueAndCreatedBy_EmailOrderByCreatedDateDesc(username));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomQuizSummary> getAllCustomQuizzes() {
        return summarise(quizRepository.findByCustomTrueOrderByCreatedDateDesc());
    }

    @Override
    @Transactional
    public void delete(final Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .filter(Quiz::isCustom)
                .orElseThrow(() -> new RecordNotFoundException("No custom quiz with id: " + quizId));

        if (!canManage(quiz, authenticationFacade.getPrincipal().getUsername())) {
            throw new AccessDeniedException("Only the quiz's creator and the admins can delete it");
        }

        // Everything pointing at the quiz goes first: the runs of it (whose results it would
        // otherwise keep alive), then the invitations, then its questions with their answers.
        userQuizHistoryRepository.deleteByQuiz_QuizId(quizId);
        quizInviteRepository.deleteByQuiz_QuizId(quizId);
        customQuestionRepository.deleteAllById(quiz.getQuestionIds());
        quizRepository.delete(quiz);
        log.info("Deleted custom quiz id: {} with its {} questions, invites and history", quizId, quiz.getQuestionIds().size());
    }

    /** Its creator, or an admin moderating what players write. */
    private boolean canManage(final Quiz quiz, final String username) {
        boolean isCreator = Objects.nonNull(quiz.getCreatedBy()) && username.equals(quiz.getCreatedBy().getEmail());
        boolean isAdmin = authenticationFacade.getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> UserRole.ROLE_ADMIN.name().equals(authority.getAuthority()));
        return isCreator || isAdmin;
    }

    // ponytail: two count-queries per quiz; fold them into one query if these lists get long.
    private List<CustomQuizSummary> summarise(final List<Quiz> quizzes) {
        return quizzes.stream()
                .map(quiz -> new CustomQuizSummary(
                        quiz.getQuizId(),
                        quiz.getType().getName(),
                        quiz.getQuestionsCount(),
                        quiz.getQuestionTime(),
                        quiz.getCreatedDate(),
                        Optional.ofNullable(quiz.getCreatedBy()).map(UserSummary::of).orElse(null),
                        quizInviteRepository.countByQuiz_QuizId(quiz.getQuizId()),
                        userQuizHistoryRepository.countByQuiz_QuizId(quiz.getQuizId())))
                .toList();
    }

    /**
     * Picks are answer ids: one (single choice, one from two), several (multiple choice: the set
     * must match; in order: the sequence must), or the text typed (input: any right answer,
     * ignoring case and outer spaces). A question with no pick ran out of time.
     */
    @Override
    @Transactional(readOnly = true)
    public List<HistoryAnswer> score(final Quiz quiz, final JsonArray userAnswers) {
        // The saved answers: one {"<questionId>": {"answer": …, "time": …}} object per question answered.
        Map<Long, JsonObject> picks = new HashMap<>();
        userAnswers.forEach(element -> element.getAsJsonObject().entrySet()
                .forEach(entry -> picks.put(Long.valueOf(entry.getKey()), entry.getValue().getAsJsonObject())));

        String quizTypeName = quiz.getType().getName();
        return questionsOf(quiz).stream()
                .map(question -> scoreQuestion(question, picks.get(question.getQuestionId()), quizTypeName))
                .toList();
    }

    private HistoryAnswer scoreQuestion(final CustomQuestion question, final JsonObject pick, final String quizTypeName) {
        List<CustomAnswer> rightAnswers = question.getAnswers().stream().filter(CustomAnswer::isRight).toList();
        String separator = IN_ORDER.equals(quizTypeName) ? " → " : ", ";
        String rightText = rightAnswers.stream().map(CustomAnswer::getContent).collect(Collectors.joining(separator));

        // Unanswered: shown like the other quizzes show it, with the right answer.
        if (Objects.isNull(pick)) {
            return new HistoryAnswer(0, question.getContent(), null, rightText);
        }

        double time = pick.get("time").getAsDouble();
        JsonElement answer = pick.get("answer");

        if (INPUT.equals(quizTypeName)) {
            String typed = answer.getAsString().trim();
            boolean right = rightAnswers.stream().anyMatch(option -> option.getContent().trim().equalsIgnoreCase(typed));
            return new HistoryAnswer(time, question.getContent(), typed, right ? null : rightText);
        }

        List<Long> picked = new ArrayList<>();
        if (answer.isJsonArray()) {
            answer.getAsJsonArray().forEach(id -> picked.add(id.getAsLong()));
        } else {
            picked.add(answer.getAsLong());
        }
        // Already in the order written, so these are also the right order.
        List<Long> rightIds = rightAnswers.stream().map(CustomAnswer::getAnswerId).toList();
        boolean right = IN_ORDER.equals(quizTypeName)
                ? picked.equals(rightIds)
                : new HashSet<>(picked).equals(new HashSet<>(rightIds));

        Map<Long, String> contents = question.getAnswers().stream()
                .collect(Collectors.toMap(CustomAnswer::getAnswerId, CustomAnswer::getContent));
        String pickedText = picked.stream()
                .map(contents::get)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(separator));

        // As for the other quizzes: the right answer is only filled in when the pick was wrong.
        return new HistoryAnswer(time, question.getContent(), pickedText, right ? null : rightText);
    }

    // The quiz's QUESTION_IDS, in the order they were written.
    private List<CustomQuestion> questionsOf(final Quiz quiz) {
        return customQuestionRepository.findAllById(quiz.getQuestionIds()).stream()
                .sorted(Comparator.comparingInt(CustomQuestion::getPosition))
                .toList();
    }

    private static List<CustomQuizPlayDto.Option> shuffledOptions(final CustomQuestion question) {
        List<CustomQuizPlayDto.Option> options = question.getAnswers().stream()
                .map(answer -> new CustomQuizPlayDto.Option(answer.getAnswerId(), answer.getContent()))
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(options);
        return options;
    }

    private static void checkQuestionsFit(final QuizType quizType, final List<CustomQuizDto.CustomQuestionDto> questions) {
        QuestionShape shape = CUSTOM_QUIZ_TYPES.get(quizType.getName());
        questions.forEach(question -> {
            int right = question.getAnswers().size();
            int wrong = Optional.ofNullable(question.getWrongAnswers()).map(List::size).orElse(0);
            if (right < shape.minRight() || right > shape.maxRight()
                    || wrong < shape.minWrong() || wrong > shape.maxWrong()) {
                throw new IllegalArgumentException(
                        "\"" + question.getContent() + "\" does not fit a " + quizType.getName() + " quiz");
            }
        });
    }

    /** Right answers first, then wrong options, each group in the order written. */
    private static CustomQuestion toQuestion(final CustomQuizDto.CustomQuestionDto question, int position) {
        List<CustomAnswer> answers = new ArrayList<>();
        question.getAnswers().forEach(content -> answers.add(toAnswer(content, true, answers.size())));
        Optional.ofNullable(question.getWrongAnswers()).orElse(List.of())
                .forEach(content -> answers.add(toAnswer(content, false, answers.size())));

        return CustomQuestion.builder()
                .content(question.getContent().trim())
                .position(position)
                .answers(answers)
                .build()
                .fillAnswersParent();
    }

    // Saved by cascade, which the audit aspect does not see, so the date is set here.
    private static CustomAnswer toAnswer(final String content, boolean right, int position) {
        return CustomAnswer.builder()
                .content(content.trim())
                .right(right)
                .position(position)
                .createdDate(LocalDateTime.now())
                .build();
    }

    /** Nobody is invited to their own quiz, and an id nobody owns is simply left out. */
    private void invite(final Quiz quiz, final Account creator, final Set<Long> invitedUserIds) {
        Set<Long> ids = new HashSet<>(invitedUserIds == null ? Set.of() : invitedUserIds);
        ids.remove(creator.getAccountId());
        // Checked before querying: an empty IN () is invalid SQL.
        if (ids.isEmpty()) {
            log.debug("Custom quiz id: {} has nobody to invite", quiz.getQuizId());
            return;
        }

        // save() one by one, not saveAll(): the audit aspect only intercepts save().
        Set<Account> invited = userRepository.findByUserIds(ids);
        invited.forEach(account -> quizInviteRepository.save(QuizInvite.builder()
                .quiz(quiz)
                .account(account)
                .build()));
        log.info("Invited {} of {} requested users to custom quiz id: {}", invited.size(), ids.size(), quiz.getQuizId());
    }

    private Account getCurrentAccount() {
        String username = authenticationFacade.getPrincipal().getUsername();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("No user found with email: " + username));
    }
}
