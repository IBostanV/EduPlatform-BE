-- // create_conquest_attempt_table
-- One row per finished go at conquering a country. The standing conqueror is not stored: it is
-- the best attempt on that country from a round that has closed (most right, then fastest, then
-- whoever got there first), so a holder keeps the country until somebody beats the record and
-- nothing has to be settled by a job.
CREATE TABLE Q_CONQUEST_ATTEMPT (
                        ATTEMPT_ID NUMERIC(20,0) NOT NULL,
                        ROUND_NO NUMERIC(20,0) NOT NULL,
                        CAT_ID NUMERIC(20,0) NOT NULL,
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        HISTORY_ID NUMERIC(20,0) NOT NULL,
                        RIGHT_ANSWERS NUMERIC(10,0) NOT NULL,
                        TOTAL_ANSWERS NUMERIC(10,0) NOT NULL,
                        SPENT_TIME NUMERIC(20,0) DEFAULT 0,
                        CREATED_BY NUMERIC(20, 0),
                        UPDATED_BY NUMERIC(20, 0),
                        CREATED_DATE DATE NOT NULL,
                        UPDATED_DATE DATE
)
/execute/

ALTER TABLE Q_CONQUEST_ATTEMPT
    ADD CONSTRAINT Q_CONQUEST_ATTEMPT_PK PRIMARY KEY (ATTEMPT_ID)
/execute/

-- One run counts once, however many times the client reports it.
ALTER TABLE Q_CONQUEST_ATTEMPT
    ADD CONSTRAINT Q_CONQUEST_ATTEMPT_UK UNIQUE (HISTORY_ID)
/execute/

ALTER TABLE Q_CONQUEST_ATTEMPT
    ADD CONSTRAINT Q_CONQUEST_ATTEMPT_CAT FOREIGN KEY (CAT_ID) REFERENCES Q_CATEGORY(CAT_ID)
/execute/

ALTER TABLE Q_CONQUEST_ATTEMPT
    ADD CONSTRAINT Q_CONQUEST_ATTEMPT_USER FOREIGN KEY (ACCOUNT_ID) REFERENCES Q_USER(ACCOUNT_ID)
/execute/

-- The two reads: who holds a country, and when this player last tried it.
CREATE INDEX Q_CONQUEST_ATTEMPT_IX ON Q_CONQUEST_ATTEMPT (CAT_ID, ROUND_NO)
/execute/

-- //@UNDO
DROP TABLE Q_CONQUEST_ATTEMPT
/execute/
