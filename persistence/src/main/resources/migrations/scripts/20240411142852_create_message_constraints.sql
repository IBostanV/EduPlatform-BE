-- // create_message_constraints
ALTER TABLE Q_MESSAGE
    ADD CONSTRAINT MESSAGE_USER_GROUP_FK
        FOREIGN KEY (DESTINATION)
            REFERENCES Q_USER_GROUP(ID)
/execute/

-- //@UNDO
-- SQL to undo the change goes here.
ALTER TABLE Q_MESSAGE DROP CONSTRAINT MESSAGE_USER_GROUP_FK
/execute/