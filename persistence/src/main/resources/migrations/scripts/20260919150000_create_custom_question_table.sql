-- // create_custom_question_table
-- Questions a player writes for their own quiz. Kept apart from Q_QUESTION: they belong to one
-- quiz, never reach the public quizzes, and keep their wrong options next to the right answers.
CREATE TABLE Q_CUSTOM_QUESTION (
    QUESTION_ID NUMERIC(20, 0) NOT NULL,
    QUIZ_ID NUMERIC(20, 0) NOT NULL,
    CONTENT VARCHAR(1000) NOT NULL,
    POSITION NUMERIC(3, 0) NOT NULL,
    CREATED_BY NUMERIC(20, 0),
    UPDATED_BY NUMERIC(20, 0),
    CREATED_DATE DATE NOT NULL,
    UPDATED_DATE DATE
)
/execute/

ALTER TABLE Q_CUSTOM_QUESTION
    ADD CONSTRAINT Q_CUSTOM_QUESTION_PK
        PRIMARY KEY (QUESTION_ID)
/execute/

-- //@UNDO
DROP TABLE Q_CUSTOM_QUESTION
/execute/
