-- // create_quiz_invite_sequence
CREATE SEQUENCE quiz_invite_seq
    START WITH     1
    INCREMENT BY   1
    NOCACHE
    NOCYCLE
/execute/

-- //@UNDO
DROP SEQUENCE quiz_invite_seq
/execute/
