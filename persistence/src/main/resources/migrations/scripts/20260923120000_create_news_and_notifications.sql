-- // create_news_and_notifications
-- Notifications and news are worked out from what is already stored (trophies earned, wiki
-- articles, questions, conquest attempts) and from the clock (conquest rounds). Three things are
-- not stored anywhere yet, and these keep them:

-- The patch notes an admin writes for the news feed.
CREATE TABLE Q_NEWS (
                        NEWS_ID NUMERIC(20,0) NOT NULL,
                        TITLE VARCHAR2(200) NOT NULL,
                        CONTENT VARCHAR2(4000),
                        CREATED_BY NUMERIC(20, 0),
                        CREATED_DATE DATE NOT NULL
)
/execute/

ALTER TABLE Q_NEWS ADD CONSTRAINT Q_NEWS_PK PRIMARY KEY (NEWS_ID)
/execute/

CREATE SEQUENCE NEWS_SEQ START WITH 1 INCREMENT BY 1 NOCACHE
/execute/

-- When a player reached each level. The level itself is worked out from EXPERIENCE, which says
-- where a player is but not when they got there, and friends' news needs the when.
CREATE TABLE Q_LEVEL_UP (
                        LEVEL_UP_ID NUMERIC(20,0) NOT NULL,
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        LEVEL_NO NUMERIC(10,0) NOT NULL,
                        REACHED_DATE DATE NOT NULL
)
/execute/

ALTER TABLE Q_LEVEL_UP ADD CONSTRAINT Q_LEVEL_UP_PK PRIMARY KEY (LEVEL_UP_ID)
/execute/

ALTER TABLE Q_LEVEL_UP
    ADD CONSTRAINT Q_LEVEL_UP_USER FOREIGN KEY (ACCOUNT_ID) REFERENCES Q_USER(ACCOUNT_ID)
/execute/

CREATE INDEX Q_LEVEL_UP_ACCOUNT_IX ON Q_LEVEL_UP (ACCOUNT_ID, REACHED_DATE)
/execute/

CREATE SEQUENCE LEVEL_UP_SEQ START WITH 1 INCREMENT BY 1 NOCACHE
/execute/

-- Everything that happened after this moment is an unread notification.
ALTER TABLE Q_USER ADD NOTIFICATIONS_READ_AT DATE
/execute/

-- //@UNDO
ALTER TABLE Q_USER DROP COLUMN NOTIFICATIONS_READ_AT
/execute/

DROP SEQUENCE LEVEL_UP_SEQ
/execute/

DROP TABLE Q_LEVEL_UP
/execute/

DROP SEQUENCE NEWS_SEQ
/execute/

DROP TABLE Q_NEWS
/execute/
