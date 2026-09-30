-- // alter_feedback_add_contact_email
-- Guests can send feedback too; they have no account to answer, so they may leave an address.
ALTER TABLE Q_FEEDBACK
    ADD CONTACT_EMAIL VARCHAR(254)
/execute/

-- //@UNDO
ALTER TABLE Q_FEEDBACK DROP COLUMN CONTACT_EMAIL
/execute/
