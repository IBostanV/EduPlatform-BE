-- // create_custom_question_sequence
CREATE SEQUENCE custom_question_seq
    START WITH     1
    INCREMENT BY   1
    NOCACHE
    NOCYCLE
/execute/

-- //@UNDO
DROP SEQUENCE custom_question_seq
/execute/
