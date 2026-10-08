-- // create_announcements
-- What an admin announces to every player: shown once over whatever page they are on, held back
-- while they are in a quiz. Each player keeps the newest one they have dismissed.
CREATE TABLE Q_ANNOUNCEMENT (
                        ANNOUNCEMENT_ID NUMERIC(20,0) NOT NULL,
                        TITLE VARCHAR2(200) NOT NULL,
                        CONTENT VARCHAR2(4000),
                        CREATED_BY NUMERIC(20, 0),
                        CREATED_DATE DATE NOT NULL
)
/execute/

ALTER TABLE Q_ANNOUNCEMENT ADD CONSTRAINT Q_ANNOUNCEMENT_PK PRIMARY KEY (ANNOUNCEMENT_ID)
/execute/

CREATE SEQUENCE ANNOUNCEMENT_SEQ START WITH 1 INCREMENT BY 1 NOCACHE
/execute/

ALTER TABLE Q_USER ADD ANNOUNCEMENT_SEEN_ID NUMERIC(20,0) DEFAULT 0 NOT NULL
/execute/

-- //@UNDO
ALTER TABLE Q_USER DROP COLUMN ANNOUNCEMENT_SEEN_ID
/execute/

DROP SEQUENCE ANNOUNCEMENT_SEQ
/execute/

DROP TABLE Q_ANNOUNCEMENT
/execute/
