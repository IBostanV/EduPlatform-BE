-- // alter_user_add_is_blocked
-- Blocking is not the same as IS_ENABLED, which says whether the verification email was
-- answered: a blocked account is a verified one an admin has shut out, and unblocking must not
-- silently verify an address nobody ever confirmed.
ALTER TABLE Q_USER
    ADD IS_BLOCKED NUMBER(1,0) DEFAULT 0 NOT NULL
/execute/

-- //@UNDO
ALTER TABLE Q_USER DROP COLUMN IS_BLOCKED
/execute/
