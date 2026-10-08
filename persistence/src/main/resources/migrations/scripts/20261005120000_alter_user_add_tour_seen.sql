-- // alter_user_add_tour_seen
-- Whether the player has been through the site tour (components/tour/site-tour in the front end),
-- kept on the account so it follows them to every device. Accounts that exist already know the
-- site, so they start as seen; new ones get the default and are shown it on first arrival.
ALTER TABLE Q_USER ADD TOUR_SEEN NUMERIC(1,0) DEFAULT 0 NOT NULL
/execute/

UPDATE Q_USER SET TOUR_SEEN = 1
/execute/

-- //@UNDO
ALTER TABLE Q_USER DROP COLUMN TOUR_SEEN
/execute/
