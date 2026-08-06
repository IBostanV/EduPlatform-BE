-- // create_challenge_table
-- "Beat my score": one row per friend a finished quiz was sent to. The scores are not kept here;
-- each is that player's own run of the quiz, read from Q_USER_HISTORY by QUIZ_ID.
CREATE TABLE Q_CHALLENGE (
                        CHALLENGE_ID NUMERIC(20,0) NOT NULL,
                        QUIZ_ID NUMERIC(20,0) NOT NULL,
                        CHALLENGER_ID NUMERIC(20,0) NOT NULL,
                        CHALLENGER_HISTORY_ID NUMERIC(20,0) NOT NULL,
                        OPPONENT_ID NUMERIC(20,0) NOT NULL,
                        CREATED_BY NUMERIC(20, 0),
                        UPDATED_BY NUMERIC(20, 0),
                        CREATED_DATE DATE NOT NULL,
                        UPDATED_DATE DATE
)
/execute/

ALTER TABLE Q_CHALLENGE
    ADD CONSTRAINT Q_CHALLENGE_PK PRIMARY KEY (CHALLENGE_ID)
/execute/

-- One challenge per quiz per friend, however often the button is pressed.
ALTER TABLE Q_CHALLENGE
    ADD CONSTRAINT Q_CHALLENGE_UK UNIQUE (QUIZ_ID, OPPONENT_ID)
/execute/

ALTER TABLE Q_CHALLENGE
    ADD CONSTRAINT Q_CHALLENGE_QUIZ FOREIGN KEY (QUIZ_ID) REFERENCES Q_QUIZ(QUIZ_ID)
/execute/

ALTER TABLE Q_CHALLENGE
    ADD CONSTRAINT Q_CHALLENGE_CHALLENGER FOREIGN KEY (CHALLENGER_ID) REFERENCES Q_USER(ACCOUNT_ID)
/execute/

ALTER TABLE Q_CHALLENGE
    ADD CONSTRAINT Q_CHALLENGE_OPPONENT FOREIGN KEY (OPPONENT_ID) REFERENCES Q_USER(ACCOUNT_ID)
/execute/

CREATE INDEX Q_CHALLENGE_OPPONENT_IX ON Q_CHALLENGE (OPPONENT_ID)
/execute/

CREATE INDEX Q_CHALLENGE_CHALLENGER_IX ON Q_CHALLENGE (CHALLENGER_ID)
/execute/

-- //@UNDO
DROP TABLE Q_CHALLENGE
/execute/
