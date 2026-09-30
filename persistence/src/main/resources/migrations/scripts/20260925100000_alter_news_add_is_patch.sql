-- // alter_news_add_is_patch
-- Any player can post on the News page now. An admin's post is a patch note, shown to everyone
-- (1); anyone else's is a friend post, shown to them and their friends (0). Everything already
-- here was written by an admin, so it stays a patch note.
ALTER TABLE Q_NEWS ADD IS_PATCH NUMBER(1) DEFAULT 1 NOT NULL
/execute/

-- //@UNDO
ALTER TABLE Q_NEWS DROP COLUMN IS_PATCH
/execute/
