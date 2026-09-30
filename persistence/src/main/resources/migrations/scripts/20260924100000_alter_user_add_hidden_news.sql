-- // alter_user_add_hidden_news
-- The kinds of news a player has switched off, as a comma-separated list of
-- FeedItem.Type names (PATCH,WORLD). Empty means they see everything, which is where everybody starts.
ALTER TABLE Q_USER ADD HIDDEN_NEWS VARCHAR2(200)
/execute/

-- //@UNDO
ALTER TABLE Q_USER DROP COLUMN HIDDEN_NEWS
/execute/
