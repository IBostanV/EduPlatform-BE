-- // create_user_trophy_table
-- Which trophies a player has earned, and when. Only that: what each trophy takes, and how far
-- along somebody is, is worked out from what they have done (TrophyCatalog/TrophyService), so
-- there is no table of trophies to keep in step with the code that judges them. The date is the
-- one thing the counts cannot answer later, which is why it is kept.
CREATE TABLE Q_USER_TROPHY (
                        USER_TROPHY_ID NUMERIC(20,0) NOT NULL,
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        CODE VARCHAR2(80) NOT NULL,
                        EARNED_DATE DATE NOT NULL,
                        CREATED_BY NUMERIC(20, 0),
                        UPDATED_BY NUMERIC(20, 0),
                        CREATED_DATE DATE NOT NULL,
                        UPDATED_DATE DATE
)
/execute/

ALTER TABLE Q_USER_TROPHY ADD CONSTRAINT Q_USER_TROPHY_PK PRIMARY KEY (USER_TROPHY_ID)
/execute/

-- A trophy is earned once, however many times the shelf is read.
ALTER TABLE Q_USER_TROPHY ADD CONSTRAINT Q_USER_TROPHY_UK UNIQUE (ACCOUNT_ID, CODE)
/execute/

ALTER TABLE Q_USER_TROPHY
    ADD CONSTRAINT Q_USER_TROPHY_USER FOREIGN KEY (ACCOUNT_ID) REFERENCES Q_USER(ACCOUNT_ID)
/execute/

CREATE SEQUENCE USER_TROPHY_SEQ START WITH 1 INCREMENT BY 1 NOCACHE
/execute/

-- //@UNDO
DROP SEQUENCE USER_TROPHY_SEQ
/execute/

DROP TABLE Q_USER_TROPHY
/execute/
