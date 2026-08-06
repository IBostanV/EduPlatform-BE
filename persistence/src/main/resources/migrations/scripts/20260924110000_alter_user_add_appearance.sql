-- // alter_user_add_appearance
-- How a player has changed the look of the site for themselves (accent colour, text size, where
-- the friends panels sit, ...), as JSON (com.play.quiz.appearance.Appearance). Null is the site as
-- it comes, which is where everybody starts and where "reset" puts them back.
ALTER TABLE Q_USER ADD APPEARANCE VARCHAR2(1000)
/execute/

-- //@UNDO
ALTER TABLE Q_USER DROP COLUMN APPEARANCE
/execute/
