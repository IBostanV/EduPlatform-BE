-- // alter_feedback_add_screenshot
-- A picture of what went wrong, sent with a report. Its media type comes along, so it can be
-- served back as what it is.
ALTER TABLE Q_FEEDBACK
    ADD SCREENSHOT BLOB
/execute/

ALTER TABLE Q_FEEDBACK
    ADD SCREENSHOT_TYPE VARCHAR(100)
/execute/

-- //@UNDO
ALTER TABLE Q_FEEDBACK DROP COLUMN SCREENSHOT_TYPE
/execute/
ALTER TABLE Q_FEEDBACK DROP COLUMN SCREENSHOT
/execute/
