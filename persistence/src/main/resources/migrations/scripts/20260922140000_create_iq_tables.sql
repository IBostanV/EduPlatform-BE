-- // create_iq_tables
-- The IQ test: an item bank, one row per go at the test, and one row per answer.
--
-- DIFFICULTY is in Rasch logits on the same scale as the ability the test estimates, which is
-- what lets a run of different questions be compared with anyone else's. ATTEMPTS, CORRECT_COUNT
-- and THETA_SUM are what the item's difficulty is later re-reckoned from: the seeded value is a
-- guess from the template's rules, these are the measurement.
CREATE TABLE Q_IQ_ITEM (
                        ITEM_ID NUMERIC(20,0) NOT NULL,
                        CODE VARCHAR2(50) NOT NULL,
                        ITEM_TYPE VARCHAR2(20) NOT NULL,
                        PAYLOAD CLOB NOT NULL,
                        ANSWER_INDEX NUMERIC(5,0) NOT NULL,
                        DIFFICULTY NUMBER(5,2) NOT NULL,
                        ATTEMPTS NUMERIC(10,0) DEFAULT 0 NOT NULL,
                        CORRECT_COUNT NUMERIC(10,0) DEFAULT 0 NOT NULL,
                        THETA_SUM NUMBER(12,4) DEFAULT 0 NOT NULL,
                        CREATED_DATE DATE NOT NULL
)
/execute/

ALTER TABLE Q_IQ_ITEM ADD CONSTRAINT Q_IQ_ITEM_PK PRIMARY KEY (ITEM_ID)
/execute/

ALTER TABLE Q_IQ_ITEM ADD CONSTRAINT Q_IQ_ITEM_UK UNIQUE (CODE)
/execute/

-- One go at the test. CURRENT_ITEM_ID and SERVED_DATE are the question on screen and when it went
-- out: the clock belongs to the server, so an answer cannot be slowed down by stopping the
-- browser's. THETA and STANDARD_ERROR are the measurement; IQ and PERCENTILE are that measurement
-- put on the scale people quote, and NORMED says which scale was available at the time.
CREATE TABLE Q_IQ_SESSION (
                        SESSION_ID NUMERIC(20,0) NOT NULL,
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        STARTED_DATE DATE NOT NULL,
                        FINISHED_DATE DATE,
                        CURRENT_ITEM_ID NUMERIC(20,0),
                        SERVED_DATE DATE,
                        ANSWERED NUMERIC(5,0) DEFAULT 0 NOT NULL,
                        THETA NUMBER(8,4),
                        STANDARD_ERROR NUMBER(8,4),
                        IQ NUMERIC(5,0),
                        PERCENTILE NUMERIC(5,0),
                        NORMED NUMBER(1,0) DEFAULT 0 NOT NULL,
                        ATTEMPT_NO NUMERIC(5,0) DEFAULT 0 NOT NULL
)
/execute/

ALTER TABLE Q_IQ_SESSION ADD CONSTRAINT Q_IQ_SESSION_PK PRIMARY KEY (SESSION_ID)
/execute/

ALTER TABLE Q_IQ_SESSION
    ADD CONSTRAINT Q_IQ_SESSION_USER FOREIGN KEY (ACCOUNT_ID) REFERENCES Q_USER(ACCOUNT_ID)
/execute/

-- The two reads: this player's own tests, and the first attempts every score is normed against.
CREATE INDEX Q_IQ_SESSION_IX ON Q_IQ_SESSION (ACCOUNT_ID, FINISHED_DATE)
/execute/

CREATE INDEX Q_IQ_SESSION_NORM_IX ON Q_IQ_SESSION (ATTEMPT_NO, THETA)
/execute/

CREATE TABLE Q_IQ_RESPONSE (
                        RESPONSE_ID NUMERIC(20,0) NOT NULL,
                        SESSION_ID NUMERIC(20,0) NOT NULL,
                        ITEM_ID NUMERIC(20,0) NOT NULL,
                        CHOSEN_INDEX NUMERIC(5,0) NOT NULL,
                        CORRECT NUMBER(1,0) NOT NULL,
                        SECONDS NUMERIC(10,0) NOT NULL
)
/execute/

ALTER TABLE Q_IQ_RESPONSE ADD CONSTRAINT Q_IQ_RESPONSE_PK PRIMARY KEY (RESPONSE_ID)
/execute/

ALTER TABLE Q_IQ_RESPONSE
    ADD CONSTRAINT Q_IQ_RESPONSE_SESSION FOREIGN KEY (SESSION_ID) REFERENCES Q_IQ_SESSION(SESSION_ID)
/execute/

ALTER TABLE Q_IQ_RESPONSE
    ADD CONSTRAINT Q_IQ_RESPONSE_ITEM FOREIGN KEY (ITEM_ID) REFERENCES Q_IQ_ITEM(ITEM_ID)
/execute/

CREATE INDEX Q_IQ_RESPONSE_IX ON Q_IQ_RESPONSE (SESSION_ID)
/execute/

-- //@UNDO
DROP TABLE Q_IQ_RESPONSE
/execute/

DROP TABLE Q_IQ_SESSION
/execute/

DROP TABLE Q_IQ_ITEM
/execute/
