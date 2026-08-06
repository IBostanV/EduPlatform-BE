-- // create_quiz_type_sequence
CREATE SEQUENCE quiz_type_seq
    START WITH     1
    INCREMENT BY   1
    NOCACHE
    NOCYCLE
/execute/

-- //@UNDO
DROP SEQUENCE quiz_type_seq
/execute/
