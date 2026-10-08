-- // create_social_groups
-- Groups players create and post in (com.play.quiz.group). Not chat groups (Q_MESSAGE_GROUP):
-- those are conversations; these are communities. A public group is open to read and to join; a
-- private one shows only its name and description to outsiders, who ask to join and wait for the
-- owner. Everything here goes with the account it belongs to.
CREATE TABLE Q_SOCIAL_GROUP (
                        GROUP_ID NUMERIC(20,0) NOT NULL,
                        NAME VARCHAR2(80) NOT NULL,
                        DESCRIPTION VARCHAR2(500),
                        IS_PRIVATE NUMERIC(1,0) DEFAULT 0 NOT NULL,
                        OWNER_ID NUMERIC(20,0) NOT NULL,
                        CREATED_DATE DATE NOT NULL,
                        CONSTRAINT Q_SOCIAL_GROUP_PK PRIMARY KEY (GROUP_ID),
                        CONSTRAINT Q_SOCIAL_GROUP_OWNER FOREIGN KEY (OWNER_ID)
                            REFERENCES Q_USER(ACCOUNT_ID) ON DELETE CASCADE
)
/execute/

CREATE SEQUENCE SOCIAL_GROUP_SEQ START WITH 1 INCREMENT BY 1 NOCACHE
/execute/

-- STATUS: MEMBER, or PENDING while a request to join a private group waits for the owner.
-- The owner has a MEMBER row too, so "who is in it" is one table.
CREATE TABLE Q_SOCIAL_GROUP_MEMBER (
                        MEMBER_ID NUMERIC(20,0) NOT NULL,
                        GROUP_ID NUMERIC(20,0) NOT NULL,
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        STATUS VARCHAR2(10) NOT NULL,
                        JOINED_DATE DATE NOT NULL,
                        CONSTRAINT Q_SOCIAL_GROUP_MEMBER_PK PRIMARY KEY (MEMBER_ID),
                        CONSTRAINT Q_SOCIAL_GROUP_MEMBER_UK UNIQUE (GROUP_ID, ACCOUNT_ID),
                        CONSTRAINT Q_SOCIAL_GROUP_MEMBER_GROUP FOREIGN KEY (GROUP_ID)
                            REFERENCES Q_SOCIAL_GROUP(GROUP_ID) ON DELETE CASCADE,
                        CONSTRAINT Q_SOCIAL_GROUP_MEMBER_USER FOREIGN KEY (ACCOUNT_ID)
                            REFERENCES Q_USER(ACCOUNT_ID) ON DELETE CASCADE
)
/execute/

CREATE INDEX Q_SOCIAL_GROUP_MEMBER_USER_IX ON Q_SOCIAL_GROUP_MEMBER (ACCOUNT_ID)
/execute/

CREATE SEQUENCE SOCIAL_GROUP_MEMBER_SEQ START WITH 1 INCREMENT BY 1 NOCACHE
/execute/

CREATE TABLE Q_GROUP_POST (
                        POST_ID NUMERIC(20,0) NOT NULL,
                        GROUP_ID NUMERIC(20,0) NOT NULL,
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        CONTENT VARCHAR2(4000) NOT NULL,
                        CREATED_DATE DATE NOT NULL,
                        CONSTRAINT Q_GROUP_POST_PK PRIMARY KEY (POST_ID),
                        CONSTRAINT Q_GROUP_POST_GROUP FOREIGN KEY (GROUP_ID)
                            REFERENCES Q_SOCIAL_GROUP(GROUP_ID) ON DELETE CASCADE,
                        CONSTRAINT Q_GROUP_POST_USER FOREIGN KEY (ACCOUNT_ID)
                            REFERENCES Q_USER(ACCOUNT_ID) ON DELETE CASCADE
)
/execute/

CREATE INDEX Q_GROUP_POST_GROUP_IX ON Q_GROUP_POST (GROUP_ID, CREATED_DATE)
/execute/

CREATE SEQUENCE GROUP_POST_SEQ START WITH 1 INCREMENT BY 1 NOCACHE
/execute/

-- Who may see a player's activity on their profile (quiz history, reactions, posts, articles,
-- friends, groups): PUBLIC, FRIENDS or PRIVATE. Friends by default: the history was never shown
-- to anyone before, so nobody's is opened up without them choosing it.
ALTER TABLE Q_USER ADD PROFILE_VISIBILITY VARCHAR2(10) DEFAULT 'FRIENDS' NOT NULL
/execute/

-- //@UNDO
ALTER TABLE Q_USER DROP COLUMN PROFILE_VISIBILITY
/execute/

DROP SEQUENCE GROUP_POST_SEQ
/execute/

DROP TABLE Q_GROUP_POST
/execute/

DROP SEQUENCE SOCIAL_GROUP_MEMBER_SEQ
/execute/

DROP TABLE Q_SOCIAL_GROUP_MEMBER
/execute/

DROP SEQUENCE SOCIAL_GROUP_SEQ
/execute/

DROP TABLE Q_SOCIAL_GROUP
/execute/
