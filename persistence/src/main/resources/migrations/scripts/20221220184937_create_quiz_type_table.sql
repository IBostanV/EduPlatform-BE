-- // create_quiz_type_table
CREATE TABLE Q_QUIZ_TYPE (
                           ID NUMERIC(20, 0) NOT NULL,
                           NAME VARCHAR(800),
                           IS_ACTIVE NUMERIC(1, 0) DEFAULT 1,
                           DESCRIPTION VARCHAR(200),
                           BIT_VALUE NUMERIC(20, 0) NOT NULL,
                           CREATED_BY NUMERIC(20, 0),
                           UPDATED_BY NUMERIC(20, 0),
                           CREATED_DATE DATE NOT NULL,
                           UPDATED_DATE DATE
)
/execute/

ALTER TABLE Q_QUIZ_TYPE
    ADD CONSTRAINT QUIZ_TYPE_PK
        PRIMARY KEY (ID)
/execute/

-- //@UNDO
DROP TABLE Q_QUIZ_TYPE
/execute/



