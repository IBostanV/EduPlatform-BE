-- Not a migration: run it by hand against a dev database (e.g. from SQL Developer or IntelliJ,
-- as one statement from DECLARE to END). It commits itself and refuses to run twice.
--
-- Sample single-answer questions: a General knowledge category. Answers are glossary terms
-- grouped by topic (planets, elements, painters, oceans), so the wrong options are drawn from
-- the same topic. They feed the regular quiz and the home page mini game.
--
-- EXCLUDE_TYPE 32 = every quiz type except MAP_CHOICE.
DECLARE
    v_found NUMBER; v_type NUMBER; v_cat NUMBER;

    FUNCTION add_type(p_name VARCHAR2) RETURN NUMBER IS
        v_id NUMBER := GLOSSARY_TYPE_SEQ.NEXTVAL;
    BEGIN
        INSERT INTO Q_GLOSSARY_TYPE (ID, NAME, IS_ACTIVE, CREATED_BY, CREATED_DATE)
        VALUES (v_id, p_name, 1, 1, SYSDATE);
        RETURN v_id;
    END;

    PROCEDURE term(p_key VARCHAR2, p_value VARCHAR2) IS
    BEGIN
        INSERT INTO Q_GLOSSARY (TERM_ID, KEY, VALUE, CAT_ID, TYPE_ID, IS_ACTIVE, CREATED_BY, CREATED_DATE)
        VALUES (GLOSSARIES_SEQ.NEXTVAL, p_key, p_value, v_cat, v_type, 1, 1, SYSDATE);
    END;

    -- A question whose one correct answer is the term with this key.
    PROCEDURE ask(p_content VARCHAR2, p_key VARCHAR2) IS
        v_question NUMBER := QUESTIONS_SEQ.NEXTVAL;
        v_term NUMBER; v_value VARCHAR2(100);
    BEGIN
        SELECT TERM_ID, VALUE INTO v_term, v_value FROM Q_GLOSSARY WHERE KEY = p_key;
        INSERT INTO Q_QUESTION (QUESTION_ID, ACCOUNT_ID, TYPE, CAT_ID, IS_ACTIVE, COMPLEXITY_LEVEL, PRIORITY, CONTENT, ATTRIBUTES, EXCLUDE_TYPE, CREATED_BY, CREATED_DATE)
        VALUES (v_question, 1, 'CREATED', v_cat, 1, 1, 1, p_content, 'ANSWER_BY_VALUE', 32, 1, SYSDATE);
        INSERT INTO Q_ANSWER (ANS_ID, QUESTION_ID, CONTENT, TERM_ID, CREATED_BY, CREATED_DATE)
        VALUES (ANSWERS_SEQ.NEXTVAL, v_question, v_value, v_term, 1, SYSDATE);
    END;
BEGIN
    SELECT COUNT(*) INTO v_found FROM Q_CATEGORY WHERE NAME = 'General knowledge';
    IF v_found > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'General knowledge already exists, samples not added again');
    END IF;

    v_cat := CATEGORIES_SEQ.NEXTVAL;
    INSERT INTO Q_CATEGORY (CAT_ID, NAME, NATURAL_ID, VISIBLE, CREATED_BY, CREATED_DATE)
    VALUES (v_cat, 'General knowledge', 'GENERAL_KNOWLEDGE', 1, 1, SYSDATE);

    v_type := add_type('Planets');
    term('PLANET_MERCURY', 'Mercury'); term('PLANET_VENUS', 'Venus'); term('PLANET_EARTH', 'Earth');
    term('PLANET_MARS', 'Mars'); term('PLANET_JUPITER', 'Jupiter'); term('PLANET_SATURN', 'Saturn');
    term('PLANET_URANUS', 'Uranus'); term('PLANET_NEPTUNE', 'Neptune');
    ask('Which planet is the largest in the Solar System?', 'PLANET_JUPITER');
    ask('Which planet is known as the Red Planet?', 'PLANET_MARS');
    ask('Which planet is closest to the Sun?', 'PLANET_MERCURY');
    ask('Which planet is the hottest, thanks to its thick atmosphere?', 'PLANET_VENUS');

    v_type := add_type('Chemical elements');
    term('ELEMENT_H', 'Hydrogen'); term('ELEMENT_HE', 'Helium'); term('ELEMENT_C', 'Carbon');
    term('ELEMENT_O', 'Oxygen'); term('ELEMENT_NA', 'Sodium'); term('ELEMENT_FE', 'Iron');
    term('ELEMENT_AG', 'Silver'); term('ELEMENT_AU', 'Gold');
    ask('Which element has the chemical symbol Au?', 'ELEMENT_AU');
    ask('Which element has the chemical symbol Fe?', 'ELEMENT_FE');
    ask('Which is the lightest element?', 'ELEMENT_H');
    ask('Which gas do plants give off during photosynthesis?', 'ELEMENT_O');

    v_type := add_type('Painters');
    term('PAINTER_LEONARDO', 'Leonardo da Vinci'); term('PAINTER_VAN_GOGH', 'Vincent van Gogh');
    term('PAINTER_PICASSO', 'Pablo Picasso'); term('PAINTER_MONET', 'Claude Monet');
    term('PAINTER_MICHELANGELO', 'Michelangelo'); term('PAINTER_REMBRANDT', 'Rembrandt');
    term('PAINTER_DALI', 'Salvador Dali');
    ask('Who painted the Mona Lisa?', 'PAINTER_LEONARDO');
    ask('Who painted The Starry Night?', 'PAINTER_VAN_GOGH');
    ask('Who painted the ceiling of the Sistine Chapel?', 'PAINTER_MICHELANGELO');
    ask('Who painted Guernica?', 'PAINTER_PICASSO');

    v_type := add_type('Oceans');
    term('OCEAN_PACIFIC', 'Pacific Ocean'); term('OCEAN_ATLANTIC', 'Atlantic Ocean');
    term('OCEAN_INDIAN', 'Indian Ocean'); term('OCEAN_ARCTIC', 'Arctic Ocean');
    term('OCEAN_SOUTHERN', 'Southern Ocean');
    ask('What is the largest ocean on Earth?', 'OCEAN_PACIFIC');
    ask('Which ocean lies between Africa and Australia?', 'OCEAN_INDIAN');
    ask('Which is the smallest and shallowest ocean?', 'OCEAN_ARCTIC');

    COMMIT;
END;
