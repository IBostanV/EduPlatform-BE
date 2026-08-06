-- // alter_user_drop_theme
-- A free-text "theme" a player could type on their profile, which nothing ever read. How the site
-- looks for a player is Q_USER.APPEARANCE now. (Q_USER_GROUP.THEME is a chat group's, and stays.)
ALTER TABLE Q_USER DROP COLUMN THEME
/execute/

-- //@UNDO
ALTER TABLE Q_USER ADD THEME VARCHAR(20)
/execute/
