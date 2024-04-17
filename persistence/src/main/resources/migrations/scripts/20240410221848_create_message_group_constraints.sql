-- // create_message_group_constraints
ALTER TABLE Q_USER_GROUP
    ADD CONSTRAINT USER_GROUP_USER_FK
        FOREIGN KEY (PARTICIPANT)
            REFERENCES Q_USER(ACCOUNT_ID)
/execute/

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE Q_USER_GROUP DROP CONSTRAINT USER_GROUP_USER_FK
/execute/