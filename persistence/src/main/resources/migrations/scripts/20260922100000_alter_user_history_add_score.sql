-- // alter_user_history_add_score
-- The run's score, written once when it is recorded. It was worked out anyway to pay experience,
-- and keeping it means the profile's quiz history and its statistics are a read rather than a
-- re-marking of every run the player has ever taken.
-- Null on rows recorded before this: they show a dash instead of a score.
ALTER TABLE Q_USER_HISTORY
    ADD RIGHT_ANSWERS NUMERIC(10,0)
/execute/

ALTER TABLE Q_USER_HISTORY
    ADD TOTAL_ANSWERS NUMERIC(10,0)
/execute/

-- //@UNDO
ALTER TABLE Q_USER_HISTORY DROP COLUMN TOTAL_ANSWERS
/execute/
ALTER TABLE Q_USER_HISTORY DROP COLUMN RIGHT_ANSWERS
/execute/
