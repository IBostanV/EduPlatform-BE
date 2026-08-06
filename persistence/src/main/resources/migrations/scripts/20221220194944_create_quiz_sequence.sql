-- // create_quiz_sequence
CREATE SEQUENCE quiz_seq
    START WITH     1
    INCREMENT BY   1
    NOCACHE
    NOCYCLE
/execute/


-- //@UNDO
DROP SEQUENCE quiz_seq
/execute/


