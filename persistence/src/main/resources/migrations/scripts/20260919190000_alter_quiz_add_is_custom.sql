-- // alter_quiz_add_is_custom
-- 1: a quiz a player built from questions they wrote (Q_CUSTOM_QUESTION); 0: one made from the
-- admins' or the system's questions (Q_QUESTION).
ALTER TABLE Q_QUIZ
    ADD IS_CUSTOM NUMBER(1, 0) DEFAULT 0 NOT NULL
/execute/

-- Custom quizzes made before the flag existed.
UPDATE Q_QUIZ
    SET IS_CUSTOM = 1
    WHERE QUIZ_ID IN (SELECT DISTINCT QUIZ_ID FROM Q_CUSTOM_QUESTION)
/execute/

-- //@UNDO
ALTER TABLE Q_QUIZ DROP COLUMN IS_CUSTOM
/execute/
