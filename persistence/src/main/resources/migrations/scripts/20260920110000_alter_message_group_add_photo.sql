-- // alter_message_group_add_photo
-- A group's own picture, shown instead of its initials. Uploaded already shrunk to avatar size,
-- so it can travel with the group list.
ALTER TABLE Q_MESSAGE_GROUP
    ADD PHOTO BLOB
/execute/

ALTER TABLE Q_MESSAGE_GROUP
    ADD PHOTO_TYPE VARCHAR(100)
/execute/

-- //@UNDO
ALTER TABLE Q_MESSAGE_GROUP DROP COLUMN PHOTO_TYPE
/execute/
ALTER TABLE Q_MESSAGE_GROUP DROP COLUMN PHOTO
/execute/
