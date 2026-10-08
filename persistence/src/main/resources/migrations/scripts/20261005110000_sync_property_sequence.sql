-- // sync_property_sequence
-- Q_PROPERTY rows were seeded with hardcoded IDs while PROPERTY_SEQ still started at 1,
-- so move the sequence past the highest existing ID.
DECLARE
    v_next NUMBER;
BEGIN
    SELECT NVL(MAX(PROPERTY_ID), 0) + 1 INTO v_next FROM Q_PROPERTY;
    EXECUTE IMMEDIATE 'DROP SEQUENCE property_seq';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE property_seq START WITH ' || v_next || ' INCREMENT BY 1 NOCACHE NOCYCLE';
END;
/execute/

-- //@UNDO
-- Nothing to undo: the sequence is just ahead of the table again.
