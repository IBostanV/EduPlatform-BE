-- // create_user_background_table
-- The one picture a player may upload as the site's background (Appearance). One row per player:
-- a new upload replaces it. PUBLIC_ID is what the picture's address carries: random, and new with
-- every upload, so the browser can keep it for good and nobody can walk the ids to other people's.
-- The browser has already scaled and compressed it (≤ 1 MB, checked again on the way in).
CREATE TABLE Q_USER_BACKGROUND (
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        PUBLIC_ID VARCHAR2(36) NOT NULL,
                        CONTENT_TYPE VARCHAR2(20) NOT NULL,
                        IMAGE BLOB NOT NULL,
                        CREATED_DATE DATE NOT NULL
)
/execute/

ALTER TABLE Q_USER_BACKGROUND ADD CONSTRAINT Q_USER_BACKGROUND_PK PRIMARY KEY (ACCOUNT_ID)
/execute/

ALTER TABLE Q_USER_BACKGROUND ADD CONSTRAINT Q_USER_BACKGROUND_UK UNIQUE (PUBLIC_ID)
/execute/

-- Goes with the account.
ALTER TABLE Q_USER_BACKGROUND
    ADD CONSTRAINT Q_USER_BACKGROUND_USER FOREIGN KEY (ACCOUNT_ID) REFERENCES Q_USER(ACCOUNT_ID) ON DELETE CASCADE
/execute/

-- //@UNDO
DROP TABLE Q_USER_BACKGROUND
/execute/
