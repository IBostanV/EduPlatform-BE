-- // alter_feedback_widen_question
-- A reported question now carries the options the player had, one per line under the question,
-- which can outgrow the 1100 it was sized for.
ALTER TABLE Q_FEEDBACK MODIFY QUESTION VARCHAR(4000)
/execute/

-- //@UNDO
ALTER TABLE Q_FEEDBACK MODIFY QUESTION VARCHAR(1100)
/execute/
