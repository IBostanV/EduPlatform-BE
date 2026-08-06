-- // alter_quiz_widen_question_ids
-- QUESTION_IDS holds the ids comma-joined (QuestionIdsConverter). 100 characters fitted about
-- fourteen five-digit ids; a quiz can have fifty questions, which even at the longest ids (19
-- digits) take about 1,050 characters.
ALTER TABLE Q_QUIZ
    MODIFY QUESTION_IDS VARCHAR(4000)
/execute/

-- //@UNDO
-- Fails if a quiz already lists more than 100 characters of ids.
ALTER TABLE Q_QUIZ
    MODIFY QUESTION_IDS VARCHAR(100)
/execute/
