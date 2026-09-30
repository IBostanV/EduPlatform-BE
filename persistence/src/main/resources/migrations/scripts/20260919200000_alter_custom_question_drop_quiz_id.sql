-- // alter_custom_question_drop_quiz_id
-- A custom quiz lists its questions in Q_QUIZ.QUESTION_IDS, like every quiz does (IS_CUSTOM = 1
-- says the ids are Q_CUSTOM_QUESTION ones), so the question no longer points back at its quiz.

-- Custom quizzes saved while the link was on the question: copy their question ids over first,
-- in the order written ("1, 2, 3": QuestionIdsConverter's format).
UPDATE Q_QUIZ q
    SET QUESTION_IDS = (
        SELECT LISTAGG(c.QUESTION_ID, ', ') WITHIN GROUP (ORDER BY c.POSITION)
        FROM Q_CUSTOM_QUESTION c
        WHERE c.QUIZ_ID = q.QUIZ_ID
    )
    WHERE q.IS_CUSTOM = 1
      AND q.QUESTION_IDS IS NULL
      AND EXISTS (SELECT 1 FROM Q_CUSTOM_QUESTION c WHERE c.QUIZ_ID = q.QUIZ_ID)
/execute/

ALTER TABLE Q_CUSTOM_QUESTION DROP CONSTRAINT Q_CUSTOM_QUESTION_QUIZ_FK
/execute/

ALTER TABLE Q_CUSTOM_QUESTION DROP COLUMN QUIZ_ID
/execute/

-- //@UNDO
-- The column comes back empty: which quiz a question belongs to stays in Q_QUIZ.QUESTION_IDS.
ALTER TABLE Q_CUSTOM_QUESTION
    ADD QUIZ_ID NUMERIC(20, 0)
/execute/

ALTER TABLE Q_CUSTOM_QUESTION
    ADD CONSTRAINT Q_CUSTOM_QUESTION_QUIZ_FK
        FOREIGN KEY (QUIZ_ID)
            REFERENCES Q_QUIZ(QUIZ_ID)
/execute/
