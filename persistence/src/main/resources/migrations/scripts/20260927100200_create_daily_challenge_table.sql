-- // create_daily_challenge_table
-- The one quiz everyone plays on a day. Set by the first read of the day; the runs are ordinary
-- Q_USER_HISTORY rows under this QUIZ_ID, and the day's table is read straight off them.
CREATE TABLE Q_DAILY_CHALLENGE (
                        CHALLENGE_DAY DATE NOT NULL,
                        QUIZ_ID NUMERIC(20,0) NOT NULL
)
/execute/

ALTER TABLE Q_DAILY_CHALLENGE
    ADD CONSTRAINT Q_DAILY_CHALLENGE_PK PRIMARY KEY (CHALLENGE_DAY)
/execute/

ALTER TABLE Q_DAILY_CHALLENGE
    ADD CONSTRAINT Q_DAILY_CHALLENGE_QUIZ FOREIGN KEY (QUIZ_ID) REFERENCES Q_QUIZ(QUIZ_ID)
/execute/

-- //@UNDO
DROP TABLE Q_DAILY_CHALLENGE
/execute/
