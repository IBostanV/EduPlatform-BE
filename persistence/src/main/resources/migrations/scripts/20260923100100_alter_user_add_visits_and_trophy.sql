-- // alter_user_add_visits_and_trophy
-- Visiting on consecutive days is worth experience and a trophy, so the run is kept on the
-- account: LAST_SEEN_DATE is the day it was last counted, LOGIN_STREAK the run it is on, and
-- BEST_STREAK the longest it has ever been — a trophy earned in March is not taken back by a
-- quiet April.
ALTER TABLE Q_USER ADD LAST_SEEN_DATE DATE
/execute/

ALTER TABLE Q_USER ADD LOGIN_STREAK NUMERIC(10,0) DEFAULT 0 NOT NULL
/execute/

ALTER TABLE Q_USER ADD BEST_STREAK NUMERIC(10,0) DEFAULT 0 NOT NULL
/execute/

-- The trophy the player has chosen to show beside their name: a trophy code, not a row, because
-- trophies are defined in code (category trophies come and go with their categories).
ALTER TABLE Q_USER ADD PREFERRED_TROPHY VARCHAR2(80)
/execute/

-- The trophy scaffolding that came with the first migrations and was never used: an empty table,
-- a column pointing at it, and a Q_HISTORY_TROPHY join table the code mapped but no migration
-- ever created. Superseded by Q_USER_TROPHY above.
ALTER TABLE Q_USER DROP COLUMN TROPHY_ID
/execute/

DROP TABLE Q_TROPHY
/execute/

-- //@UNDO
ALTER TABLE Q_USER DROP COLUMN PREFERRED_TROPHY
/execute/

ALTER TABLE Q_USER DROP COLUMN BEST_STREAK
/execute/

ALTER TABLE Q_USER DROP COLUMN LOGIN_STREAK
/execute/

ALTER TABLE Q_USER DROP COLUMN LAST_SEEN_DATE
/execute/

CREATE TABLE Q_TROPHY (
    TROPHY_ID NUMERIC(20,0) NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    ATTACHMENT BLOB NOT NULL,
    OPTIONS VARCHAR(500),
    CREATED_BY NUMERIC(20, 0),
    UPDATED_BY NUMERIC(20, 0),
    CREATED_DATE DATE NOT NULL,
    UPDATED_DATE DATE
)
/execute/

ALTER TABLE Q_TROPHY ADD CONSTRAINT Q_TROPHY_PK PRIMARY KEY (TROPHY_ID)
/execute/

ALTER TABLE Q_USER ADD TROPHY_ID NUMERIC(20,0)
/execute/
