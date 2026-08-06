-- // create_feedback_table
-- Bug reports, questions and suggestions players send the admins. The sender is CREATED_BY;
-- PAGE is where they were when they sent it.
CREATE TABLE Q_FEEDBACK (
    FEEDBACK_ID NUMERIC(20, 0) NOT NULL,
    TYPE VARCHAR(20) NOT NULL,
    MESSAGE VARCHAR(2000) NOT NULL,
    PAGE VARCHAR(500),
    RESOLVED NUMBER(1, 0) DEFAULT 0 NOT NULL,
    CREATED_BY NUMERIC(20, 0),
    UPDATED_BY NUMERIC(20, 0),
    CREATED_DATE DATE NOT NULL,
    UPDATED_DATE DATE
)
/execute/

ALTER TABLE Q_FEEDBACK
    ADD CONSTRAINT Q_FEEDBACK_PK
        PRIMARY KEY (FEEDBACK_ID)
/execute/

-- //@UNDO
DROP TABLE Q_FEEDBACK
/execute/
