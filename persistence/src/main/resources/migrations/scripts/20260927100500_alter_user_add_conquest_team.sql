-- // alter_user_add_conquest_team
-- The chat group a player plays Conquest for; a country they hold counts for that group. No
-- foreign key: a deleted group simply stops being shown as a team.
ALTER TABLE Q_USER ADD CONQUEST_TEAM NUMERIC(20,0)
/execute/

-- //@UNDO
ALTER TABLE Q_USER DROP COLUMN CONQUEST_TEAM
/execute/
