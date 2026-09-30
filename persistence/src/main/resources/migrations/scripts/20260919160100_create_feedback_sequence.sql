-- // create_feedback_sequence
CREATE SEQUENCE feedback_seq
    START WITH     1
    INCREMENT BY   1
    NOCACHE
    NOCYCLE
/execute/

-- //@UNDO
DROP SEQUENCE feedback_seq
/execute/
