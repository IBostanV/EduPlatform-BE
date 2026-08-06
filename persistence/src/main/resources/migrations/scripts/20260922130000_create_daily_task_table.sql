-- // create_daily_task_table
-- What a player has already been paid for today, one row per finished daily task. Progress is
-- not kept anywhere: it is read off the day's quiz runs, so this table exists only to stop a
-- task paying twice when the list is read again. The unique key is what says "once a day".
CREATE TABLE Q_DAILY_TASK (
                        TASK_ID NUMERIC(20,0) NOT NULL,
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        TASK_CODE VARCHAR2(50) NOT NULL,
                        TASK_DAY DATE NOT NULL,
                        EXPERIENCE NUMERIC(10,0) NOT NULL,
                        CREATED_BY NUMERIC(20, 0),
                        UPDATED_BY NUMERIC(20, 0),
                        CREATED_DATE DATE NOT NULL,
                        UPDATED_DATE DATE
)
/execute/

ALTER TABLE Q_DAILY_TASK
    ADD CONSTRAINT Q_DAILY_TASK_PK PRIMARY KEY (TASK_ID)
/execute/

-- One payment per player, per task, per day, however many times the list is read.
ALTER TABLE Q_DAILY_TASK
    ADD CONSTRAINT Q_DAILY_TASK_UK UNIQUE (ACCOUNT_ID, TASK_CODE, TASK_DAY)
/execute/

ALTER TABLE Q_DAILY_TASK
    ADD CONSTRAINT Q_DAILY_TASK_USER FOREIGN KEY (ACCOUNT_ID) REFERENCES Q_USER(ACCOUNT_ID)
/execute/

-- //@UNDO
DROP TABLE Q_DAILY_TASK
/execute/
