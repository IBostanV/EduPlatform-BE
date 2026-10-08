-- // exclude_unfit_quiz_types
-- Multiple choice (2), drag and drop (16) and in order (128) only fit some questions, so every
-- question excludes those its answers cannot be played as (new ones do it in
-- Question.excludeUnfitTypes). x | bits is written x + bits - BITAND(x, bits): Oracle has no BITOR.
-- Fewer than two answers: none of the three.
UPDATE Q_QUESTION q
SET q.EXCLUDE_TYPE = NVL(q.EXCLUDE_TYPE, 0) + 146 - BITAND(NVL(q.EXCLUDE_TYPE, 0), 146)
WHERE (SELECT COUNT(*) FROM Q_ANSWER a WHERE a.QUESTION_ID = q.QUESTION_ID) < 2
/execute/

-- Drag and drop pairs each glossary key with its value: every answer a term's value.
UPDATE Q_QUESTION q
SET q.EXCLUDE_TYPE = NVL(q.EXCLUDE_TYPE, 0) + 16 - BITAND(NVL(q.EXCLUDE_TYPE, 0), 16)
WHERE EXISTS (
    SELECT 1 FROM Q_ANSWER a
    WHERE a.QUESTION_ID = q.QUESTION_ID
      AND NOT EXISTS (SELECT 1 FROM Q_GLOSSARY g WHERE g.TERM_ID = a.TERM_ID AND a.CONTENT = g.VALUE)
)
/execute/

-- In order sorts the terms by value: every answer a term's value, and a number.
UPDATE Q_QUESTION q
SET q.EXCLUDE_TYPE = NVL(q.EXCLUDE_TYPE, 0) + 128 - BITAND(NVL(q.EXCLUDE_TYPE, 0), 128)
WHERE EXISTS (
    SELECT 1 FROM Q_ANSWER a
    WHERE a.QUESTION_ID = q.QUESTION_ID
      AND NOT EXISTS (
          SELECT 1 FROM Q_GLOSSARY g
          WHERE g.TERM_ID = a.TERM_ID AND a.CONTENT = g.VALUE
            AND REGEXP_LIKE(TRIM(g.VALUE), '^-?[0-9][0-9 ,]*([.][0-9]+)?$')
      )
)
/execute/

-- //@UNDO
-- Nothing to undo: the bits set here cannot be told apart from those an editor chose.
