-- // create_message_sequence
CREATE SEQUENCE message_seq
    START WITH     1
    INCREMENT BY   1
    NOCACHE
    NOCYCLE
/execute/

-- //@UNDO
DROP SEQUENCE message_seq
/execute/



