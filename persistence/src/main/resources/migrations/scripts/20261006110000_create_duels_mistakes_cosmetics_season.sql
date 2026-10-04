-- // create_duels_mistakes_cosmetics_season
-- Four features, each storing only what cannot be worked out from the quiz history:
--   duels (com.play.quiz.duel): who plays whom, and which stored quiz each round is;
--   the mistakes deck (com.play.quiz.review): which questions a player got wrong, and when each
--     comes back;
--   cosmetics (com.play.quiz.cosmetic): what a player owns, and what they wear;
--   the season pass (com.play.quiz.season): which weekly quests and tiers were paid.
-- Everything goes with the account it belongs to.

-- A turn-based duel between two friends: five rounds, a day per turn.
CREATE TABLE Q_DUEL (
                        DUEL_ID NUMERIC(20,0) NOT NULL,
                        CHALLENGER_ID NUMERIC(20,0) NOT NULL,
                        OPPONENT_ID NUMERIC(20,0) NOT NULL,
                        CREATED_DATE DATE NOT NULL,
                        CONSTRAINT Q_DUEL_PK PRIMARY KEY (DUEL_ID),
                        CONSTRAINT Q_DUEL_CHALLENGER FOREIGN KEY (CHALLENGER_ID)
                            REFERENCES Q_USER(ACCOUNT_ID) ON DELETE CASCADE,
                        CONSTRAINT Q_DUEL_OPPONENT FOREIGN KEY (OPPONENT_ID)
                            REFERENCES Q_USER(ACCOUNT_ID) ON DELETE CASCADE
)
/execute/

CREATE SEQUENCE DUEL_SEQ START WITH 1 INCREMENT BY 1 NOCACHE
/execute/

-- A round's questions: a stored quiz, set when the round's first player starts it. Each player's
-- score is their first run of that quiz, as for challenges.
CREATE TABLE Q_DUEL_ROUND (
                        DUEL_ID NUMERIC(20,0) NOT NULL,
                        ROUND_NO NUMERIC(2,0) NOT NULL,
                        QUIZ_ID NUMERIC(20,0) NOT NULL,
                        CREATED_DATE DATE NOT NULL,
                        CONSTRAINT Q_DUEL_ROUND_PK PRIMARY KEY (DUEL_ID, ROUND_NO),
                        CONSTRAINT Q_DUEL_ROUND_DUEL FOREIGN KEY (DUEL_ID)
                            REFERENCES Q_DUEL(DUEL_ID) ON DELETE CASCADE
)
/execute/

-- A question a player got wrong, in a box of the spaced-repetition ladder: due again on DUE_DATE.
CREATE TABLE Q_MISTAKE (
                        MISTAKE_ID NUMERIC(20,0) NOT NULL,
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        QUESTION_ID NUMERIC(20,0) NOT NULL,
                        BOX NUMERIC(2,0) DEFAULT 0 NOT NULL,
                        DUE_DATE DATE NOT NULL,
                        CONSTRAINT Q_MISTAKE_PK PRIMARY KEY (MISTAKE_ID),
                        CONSTRAINT Q_MISTAKE_UK UNIQUE (ACCOUNT_ID, QUESTION_ID),
                        CONSTRAINT Q_MISTAKE_USER FOREIGN KEY (ACCOUNT_ID)
                            REFERENCES Q_USER(ACCOUNT_ID) ON DELETE CASCADE,
                        CONSTRAINT Q_MISTAKE_QUESTION FOREIGN KEY (QUESTION_ID)
                            REFERENCES Q_QUESTION(QUESTION_ID) ON DELETE CASCADE
)
/execute/

CREATE SEQUENCE MISTAKE_SEQ START WITH 1 INCREMENT BY 1 NOCACHE
/execute/

-- Cosmetics a player owns, by the catalog's code (CosmeticCatalog).
CREATE TABLE Q_USER_ITEM (
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        ITEM_CODE VARCHAR2(40) NOT NULL,
                        ACQUIRED_DATE DATE NOT NULL,
                        CONSTRAINT Q_USER_ITEM_PK PRIMARY KEY (ACCOUNT_ID, ITEM_CODE),
                        CONSTRAINT Q_USER_ITEM_USER FOREIGN KEY (ACCOUNT_ID)
                            REFERENCES Q_USER(ACCOUNT_ID) ON DELETE CASCADE
)
/execute/

-- What they wear: shown wherever they are, so kept on the account itself.
ALTER TABLE Q_USER ADD EQUIPPED_FRAME VARCHAR2(40)
/execute/

ALTER TABLE Q_USER ADD EQUIPPED_NAME_COLOR VARCHAR2(40)
/execute/

-- The season pass's payments: a weekly quest ("QUEST:<code>", PERIOD_START its week's Monday) or a
-- tier ("TIER:<n>", PERIOD_START its season's first day). One row each, paid once.
CREATE TABLE Q_SEASON_CLAIM (
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        CODE VARCHAR2(40) NOT NULL,
                        PERIOD_START DATE NOT NULL,
                        POINTS NUMERIC(6,0) DEFAULT 0 NOT NULL,
                        CLAIMED_DATE DATE NOT NULL,
                        CONSTRAINT Q_SEASON_CLAIM_PK PRIMARY KEY (ACCOUNT_ID, CODE, PERIOD_START),
                        CONSTRAINT Q_SEASON_CLAIM_USER FOREIGN KEY (ACCOUNT_ID)
                            REFERENCES Q_USER(ACCOUNT_ID) ON DELETE CASCADE
)
/execute/

-- //@UNDO
DROP TABLE Q_SEASON_CLAIM
/execute/

ALTER TABLE Q_USER DROP COLUMN EQUIPPED_NAME_COLOR
/execute/

ALTER TABLE Q_USER DROP COLUMN EQUIPPED_FRAME
/execute/

DROP TABLE Q_USER_ITEM
/execute/

DROP SEQUENCE MISTAKE_SEQ
/execute/

DROP TABLE Q_MISTAKE
/execute/

DROP TABLE Q_DUEL_ROUND
/execute/

DROP SEQUENCE DUEL_SEQ
/execute/

DROP TABLE Q_DUEL
/execute/
