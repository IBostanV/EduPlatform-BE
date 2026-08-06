-- // add_quiz_types
INSERT INTO Q_QUIZ_TYPE VALUES(1, 'SINGLE_CHOICE', 1, 'single_choice_desc', 1, NULL, NULL, sysdate, null)/execute/
INSERT INTO Q_QUIZ_TYPE VALUES(2, 'MULTIPLE_CHOICE', 1, 'multiple_choice_desc', 2, NULL, NULL, sysdate, null)/execute/
INSERT INTO Q_QUIZ_TYPE VALUES(3, 'ONE_FROM_TWO', 1, 'one_from_two_desc', 4, NULL, NULL, sysdate, null)/execute/
INSERT INTO Q_QUIZ_TYPE VALUES(4, 'INPUT', 1, 'input_desc', 8, NULL, NULL, sysdate, null)/execute/
INSERT INTO Q_QUIZ_TYPE VALUES(5, 'DRAG_AND_DROP', 1, 'drag_and_drop_desc', 16, NULL, NULL, sysdate, null)/execute/
INSERT INTO Q_QUIZ_TYPE VALUES(6, 'MAP_CHOICE', 1, 'map_choice_desc', 32, NULL, NULL, sysdate, null)/execute/
INSERT INTO Q_QUIZ_TYPE VALUES(7, 'VALUES_RANGE', 1, 'value_range_desc', 64, NULL, NULL, sysdate, null)/execute/
INSERT INTO Q_QUIZ_TYPE VALUES(8, 'IN_ORDER', 1, 'in_order_desc', 128, NULL, NULL, sysdate, null)/execute/


-- //@UNDO
DELETE FROM Q_QUIZ_TYPE WHERE 1 = 1/execute/
