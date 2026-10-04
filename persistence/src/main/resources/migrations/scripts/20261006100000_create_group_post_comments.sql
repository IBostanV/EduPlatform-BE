-- // create_group_post_comments
-- Comments under a group post (com.play.quiz.group). Read by whoever may read the group, written
-- by its members; they go with the post, and with the account that wrote them.
CREATE TABLE Q_GROUP_POST_COMMENT (
                        COMMENT_ID NUMERIC(20,0) NOT NULL,
                        POST_ID NUMERIC(20,0) NOT NULL,
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        CONTENT VARCHAR2(1000) NOT NULL,
                        CREATED_DATE DATE NOT NULL,
                        CONSTRAINT Q_GROUP_POST_COMMENT_PK PRIMARY KEY (COMMENT_ID),
                        CONSTRAINT Q_GROUP_POST_COMMENT_POST FOREIGN KEY (POST_ID)
                            REFERENCES Q_GROUP_POST(POST_ID) ON DELETE CASCADE,
                        CONSTRAINT Q_GROUP_POST_COMMENT_USER FOREIGN KEY (ACCOUNT_ID)
                            REFERENCES Q_USER(ACCOUNT_ID) ON DELETE CASCADE
)
/execute/

CREATE INDEX Q_GROUP_POST_COMMENT_POST_IX ON Q_GROUP_POST_COMMENT (POST_ID, CREATED_DATE)
/execute/

CREATE SEQUENCE GROUP_POST_COMMENT_SEQ START WITH 1 INCREMENT BY 1 NOCACHE
/execute/

-- //@UNDO
DROP SEQUENCE GROUP_POST_COMMENT_SEQ
/execute/

DROP TABLE Q_GROUP_POST_COMMENT
/execute/
