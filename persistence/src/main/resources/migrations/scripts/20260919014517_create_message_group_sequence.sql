-- // create_message_group_sequence
CREATE SEQUENCE message_group_seq
    START WITH     1
    INCREMENT BY   1
    NOCACHE
    NOCYCLE
/execute/


-- //@UNDO
DROP SEQUENCE message_group_seq
/execute/
