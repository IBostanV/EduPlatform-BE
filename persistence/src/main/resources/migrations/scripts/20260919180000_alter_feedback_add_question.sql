-- // alter_feedback_add_question
-- A problem reported from inside a quiz names the question it is about: which table, its id and
-- its text as the player saw it ("Question #12: ..." or "Custom question #4: ...").
ALTER TABLE Q_FEEDBACK
    ADD QUESTION VARCHAR(1100)
/execute/

-- //@UNDO
ALTER TABLE Q_FEEDBACK DROP COLUMN QUESTION
/execute/
