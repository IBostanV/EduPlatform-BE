-- // add_iq_items
-- The IQ item bank: matrices, number series, odd-one-out and analogies, generated from
-- templates so the bank is the same everywhere it is installed. DIFFICULTY is in Rasch
-- logits, set from what each template's rules are worth and recalibrated from real
-- answers once an item has been seen enough times (IqService.recalibrate).

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-1-0', 'MATRIX', '{"c":[{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"none","n":1,"r":0,"z":"m"},{"s":"triangle","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"none","n":1,"r":0,"z":"m"},{"s":"triangle","f":"none","n":1,"r":0,"z":"m"},{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"triangle","f":"none","n":1,"r":0,"z":"m"},{"s":"circle","f":"none","n":1,"r":0,"z":"m"}],"o":[{"s":"diamond","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"solid","n":1,"r":0,"z":"m"},{"s":"square","f":"none","n":2,"r":0,"z":"m"},{"s":"square","f":"none","n":1,"r":0,"z":"l"},{"s":"square","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"half","n":1,"r":0,"z":"m"}]}', 4, -2.43, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-1-1', 'MATRIX', '{"c":[{"s":"triangle","f":"none","n":1,"r":0,"z":"m"},{"s":"diamond","f":"none","n":1,"r":0,"z":"m"},{"s":"hex","f":"none","n":1,"r":0,"z":"m"},{"s":"diamond","f":"none","n":1,"r":0,"z":"m"},{"s":"hex","f":"none","n":1,"r":0,"z":"m"},{"s":"triangle","f":"none","n":1,"r":0,"z":"m"},{"s":"hex","f":"none","n":1,"r":0,"z":"m"},{"s":"triangle","f":"none","n":1,"r":0,"z":"m"}],"o":[{"s":"cross","f":"none","n":1,"r":0,"z":"m"},{"s":"diamond","f":"none","n":1,"r":0,"z":"m"},{"s":"diamond","f":"half","n":1,"r":0,"z":"m"},{"s":"diamond","f":"none","n":3,"r":0,"z":"m"},{"s":"diamond","f":"none","n":1,"r":0,"z":"s"},{"s":"star","f":"none","n":1,"r":0,"z":"m"}]}', 1, -2.28, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-1-2', 'MATRIX', '{"c":[{"s":"hex","f":"none","n":1,"r":0,"z":"m"},{"s":"cross","f":"none","n":1,"r":0,"z":"m"},{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"cross","f":"none","n":1,"r":0,"z":"m"},{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"hex","f":"none","n":1,"r":0,"z":"m"},{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"hex","f":"none","n":1,"r":0,"z":"m"}],"o":[{"s":"triangle","f":"none","n":1,"r":0,"z":"m"},{"s":"cross","f":"none","n":1,"r":0,"z":"s"},{"s":"square","f":"none","n":1,"r":0,"z":"m"},{"s":"cross","f":"none","n":4,"r":0,"z":"m"},{"s":"cross","f":"none","n":1,"r":0,"z":"m"},{"s":"cross","f":"solid","n":1,"r":0,"z":"m"}]}', 4, -2.12, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-1-3', 'MATRIX', '{"c":[{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"none","n":1,"r":0,"z":"m"},{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"none","n":1,"r":0,"z":"m"},{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"none","n":1,"r":0,"z":"m"},{"s":"star","f":"none","n":1,"r":0,"z":"m"}],"o":[{"s":"circle","f":"none","n":4,"r":0,"z":"m"},{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"hex","f":"none","n":1,"r":0,"z":"m"},{"s":"circle","f":"solid","n":1,"r":0,"z":"m"},{"s":"circle","f":"none","n":1,"r":0,"z":"l"},{"s":"cross","f":"none","n":1,"r":0,"z":"m"}]}', 1, -1.98, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-2-0', 'MATRIX', '{"c":[{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"none","n":1,"r":0,"z":"m"},{"s":"triangle","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"solid","n":1,"r":0,"z":"m"},{"s":"triangle","f":"solid","n":1,"r":0,"z":"m"},{"s":"circle","f":"solid","n":1,"r":0,"z":"m"},{"s":"triangle","f":"half","n":1,"r":0,"z":"m"},{"s":"circle","f":"half","n":1,"r":0,"z":"m"}],"o":[{"s":"square","f":"half","n":3,"r":0,"z":"m"},{"s":"square","f":"half","n":2,"r":0,"z":"m"},{"s":"square","f":"half","n":1,"r":0,"z":"l"},{"s":"diamond","f":"half","n":1,"r":0,"z":"m"},{"s":"square","f":"solid","n":1,"r":0,"z":"m"},{"s":"square","f":"half","n":1,"r":0,"z":"m"}]}', 5, -1.23, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-2-1', 'MATRIX', '{"c":[{"s":"triangle","f":"solid","n":1,"r":0,"z":"m"},{"s":"diamond","f":"solid","n":1,"r":0,"z":"m"},{"s":"hex","f":"solid","n":1,"r":0,"z":"m"},{"s":"diamond","f":"half","n":1,"r":0,"z":"m"},{"s":"hex","f":"half","n":1,"r":0,"z":"m"},{"s":"triangle","f":"half","n":1,"r":0,"z":"m"},{"s":"hex","f":"none","n":1,"r":0,"z":"m"},{"s":"triangle","f":"none","n":1,"r":0,"z":"m"}],"o":[{"s":"diamond","f":"none","n":3,"r":0,"z":"m"},{"s":"diamond","f":"half","n":1,"r":0,"z":"m"},{"s":"diamond","f":"none","n":1,"r":0,"z":"m"},{"s":"diamond","f":"none","n":1,"r":0,"z":"s"},{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"diamond","f":"none","n":4,"r":0,"z":"m"}]}', 2, -1.07, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-2-2', 'MATRIX', '{"c":[{"s":"hex","f":"half","n":1,"r":0,"z":"m"},{"s":"cross","f":"half","n":1,"r":0,"z":"m"},{"s":"star","f":"half","n":1,"r":0,"z":"m"},{"s":"cross","f":"none","n":1,"r":0,"z":"m"},{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"hex","f":"none","n":1,"r":0,"z":"m"},{"s":"star","f":"solid","n":1,"r":0,"z":"m"},{"s":"hex","f":"solid","n":1,"r":0,"z":"m"}],"o":[{"s":"cross","f":"solid","n":1,"r":0,"z":"l"},{"s":"square","f":"solid","n":1,"r":0,"z":"m"},{"s":"cross","f":"solid","n":4,"r":0,"z":"m"},{"s":"triangle","f":"solid","n":1,"r":0,"z":"m"},{"s":"cross","f":"half","n":1,"r":0,"z":"m"},{"s":"cross","f":"solid","n":1,"r":0,"z":"m"}]}', 5, -0.93, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-2-3', 'MATRIX', '{"c":[{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"none","n":1,"r":0,"z":"m"},{"s":"circle","f":"solid","n":1,"r":0,"z":"m"},{"s":"square","f":"solid","n":1,"r":0,"z":"m"},{"s":"star","f":"solid","n":1,"r":0,"z":"m"},{"s":"square","f":"half","n":1,"r":0,"z":"m"},{"s":"star","f":"half","n":1,"r":0,"z":"m"}],"o":[{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"hex","f":"half","n":1,"r":0,"z":"m"},{"s":"circle","f":"half","n":1,"r":0,"z":"m"},{"s":"circle","f":"half","n":1,"r":0,"z":"l"},{"s":"cross","f":"half","n":1,"r":0,"z":"m"},{"s":"circle","f":"solid","n":1,"r":0,"z":"m"}]}', 2, -0.78, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-3-0', 'MATRIX', '{"c":[{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"none","n":2,"r":0,"z":"m"},{"s":"triangle","f":"none","n":3,"r":0,"z":"m"},{"s":"square","f":"solid","n":3,"r":0,"z":"m"},{"s":"triangle","f":"solid","n":1,"r":0,"z":"m"},{"s":"circle","f":"solid","n":2,"r":0,"z":"m"},{"s":"triangle","f":"half","n":2,"r":0,"z":"m"},{"s":"circle","f":"half","n":3,"r":0,"z":"m"}],"o":[{"s":"square","f":"half","n":1,"r":0,"z":"m"},{"s":"square","f":"half","n":1,"r":0,"z":"l"},{"s":"diamond","f":"half","n":1,"r":0,"z":"m"},{"s":"square","f":"solid","n":1,"r":0,"z":"m"},{"s":"square","f":"half","n":3,"r":0,"z":"m"},{"s":"square","f":"half","n":1,"r":0,"z":"s"}]}', 0, -0.02, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-3-1', 'MATRIX', '{"c":[{"s":"triangle","f":"solid","n":1,"r":0,"z":"m"},{"s":"diamond","f":"solid","n":2,"r":0,"z":"m"},{"s":"hex","f":"solid","n":3,"r":0,"z":"m"},{"s":"diamond","f":"half","n":3,"r":0,"z":"m"},{"s":"hex","f":"half","n":1,"r":0,"z":"m"},{"s":"triangle","f":"half","n":2,"r":0,"z":"m"},{"s":"hex","f":"none","n":2,"r":0,"z":"m"},{"s":"triangle","f":"none","n":3,"r":0,"z":"m"}],"o":[{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"diamond","f":"none","n":3,"r":0,"z":"m"},{"s":"diamond","f":"none","n":1,"r":0,"z":"s"},{"s":"diamond","f":"none","n":1,"r":0,"z":"m"},{"s":"diamond","f":"none","n":4,"r":0,"z":"m"},{"s":"circle","f":"none","n":1,"r":0,"z":"m"}]}', 3, 0.12, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-3-2', 'MATRIX', '{"c":[{"s":"hex","f":"half","n":1,"r":0,"z":"m"},{"s":"cross","f":"half","n":2,"r":0,"z":"m"},{"s":"star","f":"half","n":3,"r":0,"z":"m"},{"s":"cross","f":"none","n":3,"r":0,"z":"m"},{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"hex","f":"none","n":2,"r":0,"z":"m"},{"s":"star","f":"solid","n":2,"r":0,"z":"m"},{"s":"hex","f":"solid","n":3,"r":0,"z":"m"}],"o":[{"s":"cross","f":"solid","n":1,"r":0,"z":"m"},{"s":"cross","f":"solid","n":4,"r":0,"z":"m"},{"s":"triangle","f":"solid","n":1,"r":0,"z":"m"},{"s":"cross","f":"half","n":1,"r":0,"z":"m"},{"s":"cross","f":"solid","n":1,"r":0,"z":"l"},{"s":"diamond","f":"solid","n":1,"r":0,"z":"m"}]}', 0, 0.28, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-3-3', 'MATRIX', '{"c":[{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"circle","f":"none","n":2,"r":0,"z":"m"},{"s":"square","f":"none","n":3,"r":0,"z":"m"},{"s":"circle","f":"solid","n":3,"r":0,"z":"m"},{"s":"square","f":"solid","n":1,"r":0,"z":"m"},{"s":"star","f":"solid","n":2,"r":0,"z":"m"},{"s":"square","f":"half","n":2,"r":0,"z":"m"},{"s":"star","f":"half","n":3,"r":0,"z":"m"}],"o":[{"s":"circle","f":"half","n":1,"r":0,"z":"l"},{"s":"hex","f":"half","n":1,"r":0,"z":"m"},{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"circle","f":"half","n":1,"r":0,"z":"m"},{"s":"cross","f":"half","n":1,"r":0,"z":"m"},{"s":"circle","f":"solid","n":1,"r":0,"z":"m"}]}', 3, 0.42, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-4-0', 'MATRIX', '{"c":[{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"square","f":"none","n":2,"r":45,"z":"m"},{"s":"triangle","f":"none","n":3,"r":90,"z":"m"},{"s":"square","f":"solid","n":3,"r":45,"z":"m"},{"s":"triangle","f":"solid","n":1,"r":90,"z":"m"},{"s":"circle","f":"solid","n":2,"r":135,"z":"m"},{"s":"triangle","f":"half","n":2,"r":90,"z":"m"},{"s":"circle","f":"half","n":3,"r":135,"z":"m"}],"o":[{"s":"diamond","f":"half","n":1,"r":0,"z":"m"},{"s":"square","f":"half","n":1,"r":0,"z":"m"},{"s":"square","f":"solid","n":1,"r":0,"z":"m"},{"s":"square","f":"half","n":3,"r":0,"z":"m"},{"s":"square","f":"half","n":1,"r":0,"z":"s"},{"s":"hex","f":"half","n":1,"r":0,"z":"m"}]}', 1, 1.17, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-4-1', 'MATRIX', '{"c":[{"s":"triangle","f":"solid","n":1,"r":0,"z":"m"},{"s":"diamond","f":"solid","n":2,"r":45,"z":"m"},{"s":"hex","f":"solid","n":3,"r":90,"z":"m"},{"s":"diamond","f":"half","n":3,"r":45,"z":"m"},{"s":"hex","f":"half","n":1,"r":90,"z":"m"},{"s":"triangle","f":"half","n":2,"r":135,"z":"m"},{"s":"hex","f":"none","n":2,"r":90,"z":"m"},{"s":"triangle","f":"none","n":3,"r":135,"z":"m"}],"o":[{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"diamond","f":"none","n":1,"r":0,"z":"s"},{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"diamond","f":"none","n":4,"r":0,"z":"m"},{"s":"diamond","f":"none","n":1,"r":0,"z":"m"},{"s":"diamond","f":"solid","n":1,"r":0,"z":"m"}]}', 4, 1.32, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-4-2', 'MATRIX', '{"c":[{"s":"hex","f":"half","n":1,"r":0,"z":"m"},{"s":"cross","f":"half","n":2,"r":45,"z":"m"},{"s":"star","f":"half","n":3,"r":90,"z":"m"},{"s":"cross","f":"none","n":3,"r":45,"z":"m"},{"s":"star","f":"none","n":1,"r":90,"z":"m"},{"s":"hex","f":"none","n":2,"r":135,"z":"m"},{"s":"star","f":"solid","n":2,"r":90,"z":"m"},{"s":"hex","f":"solid","n":3,"r":135,"z":"m"}],"o":[{"s":"cross","f":"solid","n":4,"r":0,"z":"m"},{"s":"cross","f":"solid","n":1,"r":0,"z":"m"},{"s":"triangle","f":"solid","n":1,"r":0,"z":"m"},{"s":"cross","f":"half","n":1,"r":0,"z":"m"},{"s":"cross","f":"solid","n":1,"r":0,"z":"l"},{"s":"diamond","f":"solid","n":1,"r":0,"z":"m"}]}', 1, 1.47, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-4-3', 'MATRIX', '{"c":[{"s":"star","f":"none","n":1,"r":0,"z":"m"},{"s":"circle","f":"none","n":2,"r":45,"z":"m"},{"s":"square","f":"none","n":3,"r":90,"z":"m"},{"s":"circle","f":"solid","n":3,"r":45,"z":"m"},{"s":"square","f":"solid","n":1,"r":90,"z":"m"},{"s":"star","f":"solid","n":2,"r":135,"z":"m"},{"s":"square","f":"half","n":2,"r":90,"z":"m"},{"s":"star","f":"half","n":3,"r":135,"z":"m"}],"o":[{"s":"circle","f":"solid","n":1,"r":0,"z":"m"},{"s":"circle","f":"none","n":1,"r":0,"z":"m"},{"s":"circle","f":"half","n":1,"r":0,"z":"l"},{"s":"cross","f":"half","n":1,"r":0,"z":"m"},{"s":"circle","f":"half","n":1,"r":0,"z":"m"},{"s":"circle","f":"half","n":2,"r":0,"z":"m"}]}', 4, 1.62, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-5-0', 'MATRIX', '{"c":[{"s":"circle","f":"none","n":1,"r":0,"z":"s"},{"s":"square","f":"none","n":2,"r":45,"z":"m"},{"s":"triangle","f":"none","n":3,"r":90,"z":"l"},{"s":"square","f":"solid","n":3,"r":45,"z":"m"},{"s":"triangle","f":"solid","n":1,"r":90,"z":"l"},{"s":"circle","f":"solid","n":2,"r":135,"z":"s"},{"s":"triangle","f":"half","n":2,"r":90,"z":"l"},{"s":"circle","f":"half","n":3,"r":135,"z":"s"}],"o":[{"s":"square","f":"half","n":3,"r":0,"z":"m"},{"s":"square","f":"solid","n":1,"r":0,"z":"m"},{"s":"square","f":"half","n":1,"r":0,"z":"m"},{"s":"square","f":"half","n":1,"r":0,"z":"s"},{"s":"hex","f":"half","n":1,"r":0,"z":"m"},{"s":"square","f":"half","n":4,"r":0,"z":"m"}]}', 2, 2.17, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-5-1', 'MATRIX', '{"c":[{"s":"triangle","f":"solid","n":1,"r":0,"z":"m"},{"s":"diamond","f":"solid","n":2,"r":45,"z":"l"},{"s":"hex","f":"solid","n":3,"r":90,"z":"s"},{"s":"diamond","f":"half","n":3,"r":45,"z":"l"},{"s":"hex","f":"half","n":1,"r":90,"z":"s"},{"s":"triangle","f":"half","n":2,"r":135,"z":"m"},{"s":"hex","f":"none","n":2,"r":90,"z":"s"},{"s":"triangle","f":"none","n":3,"r":135,"z":"m"}],"o":[{"s":"diamond","f":"none","n":1,"r":0,"z":"s"},{"s":"star","f":"none","n":1,"r":0,"z":"l"},{"s":"diamond","f":"none","n":4,"r":0,"z":"l"},{"s":"circle","f":"none","n":1,"r":0,"z":"l"},{"s":"diamond","f":"solid","n":1,"r":0,"z":"l"},{"s":"diamond","f":"none","n":1,"r":0,"z":"l"}]}', 5, 2.32, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-5-2', 'MATRIX', '{"c":[{"s":"hex","f":"half","n":1,"r":0,"z":"l"},{"s":"cross","f":"half","n":2,"r":45,"z":"s"},{"s":"star","f":"half","n":3,"r":90,"z":"m"},{"s":"cross","f":"none","n":3,"r":45,"z":"s"},{"s":"star","f":"none","n":1,"r":90,"z":"m"},{"s":"hex","f":"none","n":2,"r":135,"z":"l"},{"s":"star","f":"solid","n":2,"r":90,"z":"m"},{"s":"hex","f":"solid","n":3,"r":135,"z":"l"}],"o":[{"s":"cross","f":"half","n":1,"r":0,"z":"s"},{"s":"triangle","f":"solid","n":1,"r":0,"z":"s"},{"s":"cross","f":"solid","n":1,"r":0,"z":"s"},{"s":"cross","f":"solid","n":1,"r":0,"z":"m"},{"s":"diamond","f":"solid","n":1,"r":0,"z":"s"},{"s":"cross","f":"none","n":1,"r":0,"z":"s"}]}', 2, 2.48, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'MTX-5-3', 'MATRIX', '{"c":[{"s":"star","f":"none","n":1,"r":0,"z":"s"},{"s":"circle","f":"none","n":2,"r":45,"z":"m"},{"s":"square","f":"none","n":3,"r":90,"z":"l"},{"s":"circle","f":"solid","n":3,"r":45,"z":"m"},{"s":"square","f":"solid","n":1,"r":90,"z":"l"},{"s":"star","f":"solid","n":2,"r":135,"z":"s"},{"s":"square","f":"half","n":2,"r":90,"z":"l"},{"s":"star","f":"half","n":3,"r":135,"z":"s"}],"o":[{"s":"circle","f":"half","n":1,"r":0,"z":"s"},{"s":"circle","f":"half","n":1,"r":0,"z":"l"},{"s":"cross","f":"half","n":1,"r":0,"z":"m"},{"s":"circle","f":"solid","n":1,"r":0,"z":"m"},{"s":"circle","f":"half","n":2,"r":0,"z":"m"},{"s":"circle","f":"half","n":1,"r":0,"z":"m"}]}', 5, 2.62, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-add-0', 'SERIES', '{"t":[2,4,6,8,10],"o":[14,12,10,16,6]}', 1, -2.60, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-add-1', 'SERIES', '{"t":[5,8,11,14,17],"o":[11,23,17,26,20]}', 4, -2.40, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-add-2', 'SERIES', '{"t":[8,12,16,20,24],"o":[24,32,28,36,16]}', 2, -2.20, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-step-0', 'SERIES', '{"t":[2,4,8,14,22],"o":[34,32,30,36,26]}', 1, -1.40, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-step-1', 'SERIES', '{"t":[5,8,14,23,35],"o":[41,53,47,56,50]}', 4, -1.20, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-step-2', 'SERIES', '{"t":[8,12,20,32,48],"o":[64,72,68,76,56]}', 2, -1.00, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-mul-0', 'SERIES', '{"t":[2,4,8,16,32],"o":[66,64,62,68,58]}', 1, -1.20, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-mul-1', 'SERIES', '{"t":[5,15,45,135,405],"o":[1206,1218,1212,1221,1215]}', 4, -1.00, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-mul-2', 'SERIES', '{"t":[8,32,128,512,2048],"o":[8188,8196,8192,8200,8180]}', 2, -0.80, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-alt-0', 'SERIES', '{"t":[2,6,4,8,6],"o":[12,10,8,14,4]}', 1, -0.40, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-alt-1', 'SERIES', '{"t":[5,10,7,12,9],"o":[5,17,11,20,14]}', 4, -0.20, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-alt-2', 'SERIES', '{"t":[8,14,10,16,12],"o":[14,22,18,26,6]}', 2, 0.00, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-square-0', 'SERIES', '{"t":[6,11,18,27,38],"o":[53,51,49,55,45]}', 1, 0.40, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-square-1', 'SERIES', '{"t":[14,21,30,41,54],"o":[60,72,66,75,69]}', 4, 0.60, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-square-2', 'SERIES', '{"t":[24,33,44,57,72],"o":[85,93,89,97,77]}', 2, 0.80, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-fib-0', 'SERIES', '{"t":[2,2,4,6,10],"o":[18,16,14,20,10]}', 1, 0.80, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-fib-1', 'SERIES', '{"t":[5,3,8,11,19],"o":[21,33,27,36,30]}', 4, 1.00, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-fib-2', 'SERIES', '{"t":[8,4,12,16,28],"o":[40,48,44,52,32]}', 2, 1.20, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-weave-0', 'SERIES', '{"t":[2,40,4,38,6],"o":[38,36,34,40,30]}', 1, 1.60, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-weave-1', 'SERIES', '{"t":[5,40,8,37,11],"o":[25,37,31,40,34]}', 4, 1.80, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-weave-2', 'SERIES', '{"t":[8,40,12,36,16],"o":[28,36,32,40,20]}', 2, 2.00, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-quad-0', 'SERIES', '{"t":[2,5,12,23,38],"o":[59,57,55,61,51]}', 1, 2.00, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-quad-1', 'SERIES', '{"t":[5,9,19,35,57],"o":[76,88,82,91,85]}', 4, 2.20, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'SER-quad-2', 'SERIES', '{"t":[8,13,26,47,76],"o":[109,117,113,121,101]}', 2, 2.40, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-shape-0', 'ODD', '{"g":[{"s":"circle","f":"none","n":1,"r":0,"z":"s"},{"s":"diamond","f":"none","n":1,"r":45,"z":"m"},{"s":"circle","f":"none","n":1,"r":90,"z":"l"},{"s":"circle","f":"none","n":1,"r":0,"z":"s"},{"s":"circle","f":"none","n":1,"r":45,"z":"m"}]}', 1, -2.20, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-shape-1', 'ODD', '{"g":[{"s":"square","f":"solid","n":2,"r":0,"z":"s"},{"s":"square","f":"solid","n":2,"r":45,"z":"m"},{"s":"square","f":"solid","n":2,"r":90,"z":"l"},{"s":"hex","f":"solid","n":2,"r":0,"z":"s"},{"s":"square","f":"solid","n":2,"r":45,"z":"m"}]}', 3, -2.00, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-shape-2', 'ODD', '{"g":[{"s":"cross","f":"half","n":3,"r":0,"z":"s"},{"s":"triangle","f":"half","n":3,"r":45,"z":"m"},{"s":"triangle","f":"half","n":3,"r":90,"z":"l"},{"s":"triangle","f":"half","n":3,"r":0,"z":"s"},{"s":"triangle","f":"half","n":3,"r":45,"z":"m"}]}', 0, -1.80, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-fill-0', 'ODD', '{"g":[{"s":"circle","f":"none","n":1,"r":0,"z":"s"},{"s":"circle","f":"solid","n":1,"r":45,"z":"m"},{"s":"circle","f":"none","n":1,"r":90,"z":"l"},{"s":"circle","f":"none","n":1,"r":0,"z":"s"},{"s":"circle","f":"none","n":1,"r":45,"z":"m"}]}', 1, -1.60, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-fill-1', 'ODD', '{"g":[{"s":"square","f":"solid","n":2,"r":0,"z":"s"},{"s":"square","f":"solid","n":2,"r":45,"z":"m"},{"s":"square","f":"solid","n":2,"r":90,"z":"l"},{"s":"square","f":"half","n":2,"r":0,"z":"s"},{"s":"square","f":"solid","n":2,"r":45,"z":"m"}]}', 3, -1.40, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-fill-2', 'ODD', '{"g":[{"s":"triangle","f":"none","n":3,"r":0,"z":"s"},{"s":"triangle","f":"half","n":3,"r":45,"z":"m"},{"s":"triangle","f":"half","n":3,"r":90,"z":"l"},{"s":"triangle","f":"half","n":3,"r":0,"z":"s"},{"s":"triangle","f":"half","n":3,"r":45,"z":"m"}]}', 0, -1.20, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-count-0', 'ODD', '{"g":[{"s":"circle","f":"none","n":1,"r":0,"z":"s"},{"s":"circle","f":"none","n":2,"r":45,"z":"m"},{"s":"circle","f":"none","n":1,"r":90,"z":"l"},{"s":"circle","f":"none","n":1,"r":0,"z":"s"},{"s":"circle","f":"none","n":1,"r":45,"z":"m"}]}', 1, -0.80, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-count-1', 'ODD', '{"g":[{"s":"square","f":"solid","n":2,"r":0,"z":"s"},{"s":"square","f":"solid","n":2,"r":45,"z":"m"},{"s":"square","f":"solid","n":2,"r":90,"z":"l"},{"s":"square","f":"solid","n":3,"r":0,"z":"s"},{"s":"square","f":"solid","n":2,"r":45,"z":"m"}]}', 3, -0.60, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-count-2', 'ODD', '{"g":[{"s":"triangle","f":"half","n":4,"r":0,"z":"s"},{"s":"triangle","f":"half","n":3,"r":45,"z":"m"},{"s":"triangle","f":"half","n":3,"r":90,"z":"l"},{"s":"triangle","f":"half","n":3,"r":0,"z":"s"},{"s":"triangle","f":"half","n":3,"r":45,"z":"m"}]}', 0, -0.40, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-rot-0', 'ODD', '{"g":[{"s":"circle","f":"none","n":1,"r":0,"z":"s"},{"s":"circle","f":"none","n":1,"r":30,"z":"m"},{"s":"circle","f":"none","n":1,"r":0,"z":"l"},{"s":"circle","f":"none","n":1,"r":0,"z":"s"},{"s":"circle","f":"none","n":1,"r":0,"z":"m"}]}', 1, 0.20, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-rot-1', 'ODD', '{"g":[{"s":"square","f":"solid","n":2,"r":0,"z":"s"},{"s":"square","f":"solid","n":2,"r":0,"z":"m"},{"s":"square","f":"solid","n":2,"r":0,"z":"l"},{"s":"square","f":"solid","n":2,"r":30,"z":"s"},{"s":"square","f":"solid","n":2,"r":0,"z":"m"}]}', 3, 0.40, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-rot-2', 'ODD', '{"g":[{"s":"triangle","f":"half","n":3,"r":30,"z":"s"},{"s":"triangle","f":"half","n":3,"r":0,"z":"m"},{"s":"triangle","f":"half","n":3,"r":0,"z":"l"},{"s":"triangle","f":"half","n":3,"r":0,"z":"s"},{"s":"triangle","f":"half","n":3,"r":0,"z":"m"}]}', 0, 0.60, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-pair-0', 'ODD', '{"g":[{"s":"circle","f":"none","n":1,"r":0,"z":"s"},{"s":"circle","f":"none","n":2,"r":45,"z":"m"},{"s":"circle","f":"none","n":1,"r":90,"z":"l"},{"s":"circle","f":"solid","n":2,"r":0,"z":"s"},{"s":"circle","f":"none","n":1,"r":45,"z":"m"}]}', 1, 1.00, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-pair-1', 'ODD', '{"g":[{"s":"square","f":"none","n":1,"r":0,"z":"s"},{"s":"square","f":"solid","n":2,"r":45,"z":"m"},{"s":"square","f":"none","n":1,"r":90,"z":"l"},{"s":"square","f":"none","n":2,"r":0,"z":"s"},{"s":"square","f":"none","n":1,"r":45,"z":"m"}]}', 3, 1.20, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ODD-pair-2', 'ODD', '{"g":[{"s":"triangle","f":"solid","n":1,"r":0,"z":"s"},{"s":"triangle","f":"solid","n":2,"r":45,"z":"m"},{"s":"triangle","f":"none","n":1,"r":90,"z":"l"},{"s":"triangle","f":"solid","n":2,"r":0,"z":"s"},{"s":"triangle","f":"none","n":1,"r":45,"z":"m"}]}', 0, 1.40, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-single-0', 'ANALOGY', '{"a":{"s":"circle","f":"none","n":1,"r":0,"z":"s"},"b":{"s":"circle","f":"none","n":2,"r":0,"z":"s"},"c":{"s":"triangle","f":"solid","n":2,"r":45,"z":"l"},"o":[{"s":"triangle","f":"solid","n":3,"r":45,"z":"l"},{"s":"triangle","f":"half","n":3,"r":45,"z":"l"},{"s":"triangle","f":"solid","n":4,"r":45,"z":"l"},{"s":"triangle","f":"solid","n":3,"r":45,"z":"s"},{"s":"hex","f":"solid","n":3,"r":45,"z":"l"}]}', 0, -1.83, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-single-1', 'ANALOGY', '{"a":{"s":"square","f":"solid","n":2,"r":45,"z":"m"},"b":{"s":"square","f":"solid","n":3,"r":45,"z":"m"},"c":{"s":"diamond","f":"half","n":3,"r":0,"z":"s"},"o":[{"s":"diamond","f":"half","n":1,"r":0,"z":"s"},{"s":"diamond","f":"half","n":3,"r":0,"z":"s"},{"s":"diamond","f":"half","n":1,"r":0,"z":"l"},{"s":"star","f":"half","n":1,"r":0,"z":"s"},{"s":"diamond","f":"half","n":4,"r":0,"z":"s"}]}', 0, -1.68, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-single-2', 'ANALOGY', '{"a":{"s":"triangle","f":"half","n":3,"r":0,"z":"l"},"b":{"s":"triangle","f":"half","n":1,"r":0,"z":"l"},"c":{"s":"hex","f":"none","n":1,"r":45,"z":"m"},"o":[{"s":"hex","f":"none","n":2,"r":45,"z":"m"},{"s":"square","f":"none","n":2,"r":45,"z":"m"},{"s":"hex","f":"solid","n":2,"r":45,"z":"m"},{"s":"hex","f":"none","n":2,"r":45,"z":"l"},{"s":"triangle","f":"none","n":2,"r":45,"z":"m"}]}', 0, -1.53, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-single-3', 'ANALOGY', '{"a":{"s":"diamond","f":"none","n":1,"r":45,"z":"s"},"b":{"s":"diamond","f":"none","n":2,"r":45,"z":"s"},"c":{"s":"cross","f":"solid","n":2,"r":0,"z":"l"},"o":[{"s":"cross","f":"solid","n":3,"r":0,"z":"l"},{"s":"diamond","f":"solid","n":3,"r":0,"z":"l"},{"s":"cross","f":"none","n":3,"r":0,"z":"l"},{"s":"cross","f":"solid","n":4,"r":0,"z":"l"},{"s":"cross","f":"solid","n":3,"r":0,"z":"m"}]}', 0, -1.38, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-double-0', 'ANALOGY', '{"a":{"s":"circle","f":"none","n":1,"r":0,"z":"s"},"b":{"s":"circle","f":"solid","n":2,"r":0,"z":"s"},"c":{"s":"triangle","f":"solid","n":2,"r":45,"z":"l"},"o":[{"s":"triangle","f":"half","n":3,"r":45,"z":"s"},{"s":"triangle","f":"half","n":4,"r":45,"z":"l"},{"s":"triangle","f":"half","n":3,"r":45,"z":"l"},{"s":"hex","f":"half","n":3,"r":45,"z":"l"},{"s":"triangle","f":"solid","n":3,"r":45,"z":"l"}]}', 2, -0.22, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-double-1', 'ANALOGY', '{"a":{"s":"square","f":"solid","n":2,"r":45,"z":"m"},"b":{"s":"square","f":"half","n":3,"r":45,"z":"m"},"c":{"s":"diamond","f":"half","n":3,"r":0,"z":"s"},"o":[{"s":"star","f":"none","n":1,"r":0,"z":"s"},{"s":"diamond","f":"none","n":1,"r":0,"z":"l"},{"s":"diamond","f":"none","n":1,"r":0,"z":"s"},{"s":"diamond","f":"none","n":4,"r":0,"z":"s"},{"s":"circle","f":"none","n":1,"r":0,"z":"s"}]}', 2, -0.07, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-double-2', 'ANALOGY', '{"a":{"s":"triangle","f":"half","n":3,"r":0,"z":"l"},"b":{"s":"triangle","f":"none","n":1,"r":0,"z":"l"},"c":{"s":"hex","f":"none","n":1,"r":45,"z":"m"},"o":[{"s":"hex","f":"half","n":2,"r":45,"z":"m"},{"s":"square","f":"solid","n":2,"r":45,"z":"m"},{"s":"hex","f":"solid","n":2,"r":45,"z":"m"},{"s":"hex","f":"solid","n":2,"r":45,"z":"l"},{"s":"triangle","f":"solid","n":2,"r":45,"z":"m"}]}', 2, 0.07, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-double-3', 'ANALOGY', '{"a":{"s":"diamond","f":"none","n":1,"r":45,"z":"s"},"b":{"s":"diamond","f":"solid","n":2,"r":45,"z":"s"},"c":{"s":"cross","f":"solid","n":2,"r":0,"z":"l"},"o":[{"s":"cross","f":"half","n":4,"r":0,"z":"l"},{"s":"cross","f":"solid","n":3,"r":0,"z":"l"},{"s":"cross","f":"half","n":3,"r":0,"z":"l"},{"s":"cross","f":"half","n":3,"r":0,"z":"m"},{"s":"hex","f":"half","n":3,"r":0,"z":"l"}]}', 2, 0.22, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-triple-0', 'ANALOGY', '{"a":{"s":"circle","f":"none","n":1,"r":0,"z":"s"},"b":{"s":"circle","f":"solid","n":2,"r":45,"z":"m"},"c":{"s":"triangle","f":"solid","n":2,"r":45,"z":"l"},"o":[{"s":"triangle","f":"half","n":1,"r":90,"z":"s"},{"s":"triangle","f":"half","n":3,"r":90,"z":"m"},{"s":"hex","f":"half","n":3,"r":90,"z":"s"},{"s":"triangle","f":"solid","n":3,"r":90,"z":"s"},{"s":"triangle","f":"half","n":3,"r":90,"z":"s"}]}', 4, 0.97, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-triple-1', 'ANALOGY', '{"a":{"s":"square","f":"solid","n":2,"r":45,"z":"m"},"b":{"s":"square","f":"half","n":3,"r":90,"z":"s"},"c":{"s":"diamond","f":"half","n":3,"r":0,"z":"s"},"o":[{"s":"diamond","f":"solid","n":1,"r":45,"z":"l"},{"s":"star","f":"none","n":1,"r":45,"z":"l"},{"s":"diamond","f":"none","n":4,"r":45,"z":"l"},{"s":"circle","f":"none","n":1,"r":45,"z":"l"},{"s":"diamond","f":"none","n":1,"r":45,"z":"l"}]}', 4, 1.12, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-triple-2', 'ANALOGY', '{"a":{"s":"triangle","f":"half","n":3,"r":0,"z":"l"},"b":{"s":"triangle","f":"none","n":1,"r":45,"z":"l"},"c":{"s":"hex","f":"none","n":1,"r":45,"z":"m"},"o":[{"s":"hex","f":"none","n":2,"r":90,"z":"m"},{"s":"hex","f":"half","n":2,"r":90,"z":"m"},{"s":"hex","f":"solid","n":2,"r":90,"z":"l"},{"s":"triangle","f":"solid","n":2,"r":90,"z":"m"},{"s":"hex","f":"solid","n":2,"r":90,"z":"m"}]}', 4, 1.27, SYSDATE)
/execute/

INSERT INTO Q_IQ_ITEM (ITEM_ID, CODE, ITEM_TYPE, PAYLOAD, ANSWER_INDEX, DIFFICULTY, CREATED_DATE)
    VALUES (iq_item_seq.NEXTVAL, 'ANL-triple-3', 'ANALOGY', '{"a":{"s":"diamond","f":"none","n":1,"r":45,"z":"s"},"b":{"s":"diamond","f":"solid","n":2,"r":90,"z":"m"},"c":{"s":"cross","f":"solid","n":2,"r":0,"z":"l"},"o":[{"s":"cross","f":"half","n":1,"r":45,"z":"s"},{"s":"cross","f":"half","n":4,"r":45,"z":"s"},{"s":"cross","f":"half","n":3,"r":45,"z":"l"},{"s":"hex","f":"half","n":3,"r":45,"z":"s"},{"s":"cross","f":"half","n":3,"r":45,"z":"s"}]}', 4, 1.42, SYSDATE)
/execute/

-- //@UNDO
DELETE FROM Q_IQ_ITEM
/execute/
