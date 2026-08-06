-- // alter_conquest_attempt_add_bonus_paid
-- Whether the conquest bonus has been paid for this attempt. The standing conqueror is worked out
-- rather than stored, so there is no moment at which a round is settled and somebody could be
-- paid; this is what stops the bonus being paid again every time the map is read.
ALTER TABLE Q_CONQUEST_ATTEMPT
    ADD BONUS_PAID NUMBER(1,0) DEFAULT 0 NOT NULL
/execute/

-- //@UNDO
ALTER TABLE Q_CONQUEST_ATTEMPT DROP COLUMN BONUS_PAID
/execute/
