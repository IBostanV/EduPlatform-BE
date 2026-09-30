-- // alter_quiz_add_question_time
-- A custom quiz times each question, not the quiz as a whole.
ALTER TABLE Q_QUIZ
    ADD QUESTION_TIME NUMERIC(10, 0)
/execute/

-- //@UNDO
ALTER TABLE Q_QUIZ DROP COLUMN QUESTION_TIME
/execute/
