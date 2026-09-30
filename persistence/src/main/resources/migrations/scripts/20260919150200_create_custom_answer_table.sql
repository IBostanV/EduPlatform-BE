-- // create_custom_answer_table
-- A custom question's answers: the right ones (IS_RIGHT = 1) and the wrong options, in the order
-- written. For a put-in-order question, POSITION of the right answers is the right order.
CREATE TABLE Q_CUSTOM_ANSWER (
    ANSWER_ID NUMERIC(20, 0) NOT NULL,
    QUESTION_ID NUMERIC(20, 0) NOT NULL,
    CONTENT VARCHAR(1000) NOT NULL,
    IS_RIGHT NUMBER(1, 0) NOT NULL,
    POSITION NUMERIC(3, 0) NOT NULL,
    CREATED_BY NUMERIC(20, 0),
    UPDATED_BY NUMERIC(20, 0),
    CREATED_DATE DATE NOT NULL,
    UPDATED_DATE DATE
)
/execute/

ALTER TABLE Q_CUSTOM_ANSWER
    ADD CONSTRAINT Q_CUSTOM_ANSWER_PK
        PRIMARY KEY (ANSWER_ID)
/execute/

-- //@UNDO
DROP TABLE Q_CUSTOM_ANSWER
/execute/
