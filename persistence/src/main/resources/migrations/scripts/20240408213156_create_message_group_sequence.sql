-- // create_message_group_sequence
CREATE SEQUENCE user_group_seq
    START WITH     1
    INCREMENT BY   1
    NOCACHE
    NOCYCLE
/execute/


-- //@UNDO
DROP SEQUENCE user_group_seq
/execute/


