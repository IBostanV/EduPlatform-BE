-- // alter_quiz_category_nullable
-- An express quiz and the daily challenge are general knowledge, not one category, but CAT_ID
-- being NOT NULL had them stored under category 1 (a real category), so their runs read as that
-- category in the history, the statistics and the category trophies. From now on they are stored
-- with no category. The ones stored before cannot be told apart from real runs of category 1 and
-- are left as they are.
ALTER TABLE Q_QUIZ MODIFY CAT_ID NULL
/execute/

-- //@UNDO
UPDATE Q_QUIZ SET CAT_ID = 1 WHERE CAT_ID IS NULL
/execute/

ALTER TABLE Q_QUIZ MODIFY CAT_ID NOT NULL
/execute/
