-- // create_custom_answer_sequence
CREATE SEQUENCE custom_answer_seq
    START WITH     1
    INCREMENT BY   1
    NOCACHE
    NOCYCLE
/execute/

-- //@UNDO
DROP SEQUENCE custom_answer_seq
/execute/
