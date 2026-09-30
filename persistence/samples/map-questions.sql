-- Not a migration: run it by hand against a dev database (e.g. from SQL Developer or IntelliJ,
-- as one statement from DECLARE to END). It commits itself and refuses to run twice.
--
-- Sample map questions: a Geography category with Countries / Cities / Continents under it.
-- Answers are glossary terms; each region gets its own glossary type so the wrong options are
-- drawn from the same region and the map stays zoomed in. Skipped if Geography already exists.
--
-- EXCLUDE_TYPE 222 = every quiz type except SINGLE_CHOICE (1) and MAP_CHOICE (32).
DECLARE
    v_geo NUMBER; v_countries NUMBER; v_cities NUMBER; v_continents NUMBER; v_type NUMBER; v_cat NUMBER;

    FUNCTION add_category(p_name VARCHAR2, p_parent NUMBER) RETURN NUMBER IS
        v_id NUMBER := CATEGORIES_SEQ.NEXTVAL;
    BEGIN
        INSERT INTO Q_CATEGORY (CAT_ID, NAME, NATURAL_ID, SUBCATEGORY_ID, VISIBLE, CREATED_BY, CREATED_DATE)
        VALUES (v_id, p_name, UPPER(REPLACE(p_name, ' ', '_')), p_parent, 1, 1, SYSDATE);
        RETURN v_id;
    END;

    FUNCTION add_type(p_name VARCHAR2, p_options VARCHAR2) RETURN NUMBER IS
        v_id NUMBER := GLOSSARY_TYPE_SEQ.NEXTVAL;
    BEGIN
        INSERT INTO Q_GLOSSARY_TYPE (ID, NAME, OPTIONS, IS_ACTIVE, CREATED_BY, CREATED_DATE)
        VALUES (v_id, p_name, p_options, 1, 1, SYSDATE);
        RETURN v_id;
    END;

    PROCEDURE term(p_key VARCHAR2, p_value VARCHAR2, p_options VARCHAR2 DEFAULT NULL) IS
    BEGIN
        INSERT INTO Q_GLOSSARY (TERM_ID, KEY, VALUE, CAT_ID, TYPE_ID, OPTIONS, IS_ACTIVE, CREATED_BY, CREATED_DATE)
        VALUES (GLOSSARIES_SEQ.NEXTVAL, p_key, p_value, v_cat, v_type, p_options, 1, 1, SYSDATE);
    END;

    -- A question whose one correct answer is the term with this key (in the current type).
    PROCEDURE ask(p_content VARCHAR2, p_key VARCHAR2) IS
        v_question NUMBER := QUESTIONS_SEQ.NEXTVAL;
        v_term NUMBER; v_value VARCHAR2(100);
    BEGIN
        SELECT TERM_ID, VALUE INTO v_term, v_value FROM Q_GLOSSARY WHERE KEY = p_key AND TYPE_ID = v_type;
        INSERT INTO Q_QUESTION (QUESTION_ID, ACCOUNT_ID, TYPE, CAT_ID, IS_ACTIVE, COMPLEXITY_LEVEL, PRIORITY, CONTENT, ATTRIBUTES, EXCLUDE_TYPE, CREATED_BY, CREATED_DATE)
        VALUES (v_question, 1, 'CREATED', v_cat, 1, 1, 1, p_content, 'ANSWER_BY_VALUE', 222, 1, SYSDATE);
        INSERT INTO Q_ANSWER (ANS_ID, QUESTION_ID, CONTENT, TERM_ID, CREATED_BY, CREATED_DATE)
        VALUES (ANSWERS_SEQ.NEXTVAL, v_question, v_value, v_term, 1, SYSDATE);
    END;
BEGIN
    SELECT COUNT(*) INTO v_geo FROM Q_CATEGORY WHERE NAME = 'Geography';
    IF v_geo > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Geography already exists, samples not added again');
    END IF;

    v_geo := add_category('Geography', NULL);
    v_countries := add_category('Countries', v_geo);
    v_cities := add_category('Cities', v_geo);
    v_continents := add_category('Continents', v_geo);

    -- ---- Countries (key: ISO 3166 alpha-3) ------------------------------------------------
    v_cat := v_countries;

    v_type := add_type('European countries', 'map:country');
    term('FRA', 'France'); term('DEU', 'Germany'); term('ITA', 'Italy'); term('ESP', 'Spain');
    term('POL', 'Poland'); term('MDA', 'Moldova'); term('ROU', 'Romania'); term('UKR', 'Ukraine');
    term('PRT', 'Portugal'); term('GRC', 'Greece'); term('CHE', 'Switzerland'); term('NOR', 'Norway');
    ask('Where is Moldova?', 'MDA');
    ask('Where is Portugal?', 'PRT');
    ask('Find Switzerland on the map', 'CHE');
    ask('Which of these is Poland?', 'POL');
    ask('Where is Greece?', 'GRC');

    v_type := add_type('South American countries', 'map:country');
    term('BRA', 'Brazil'); term('ARG', 'Argentina'); term('CHL', 'Chile'); term('PER', 'Peru');
    term('COL', 'Colombia'); term('BOL', 'Bolivia'); term('VEN', 'Venezuela'); term('URY', 'Uruguay');
    term('PRY', 'Paraguay'); term('ECU', 'Ecuador');
    ask('Where is Bolivia?', 'BOL');
    ask('Find Uruguay on the map', 'URY');
    ask('Which country is home to Machu Picchu?', 'PER');

    v_type := add_type('African countries', 'map:country');
    term('EGY', 'Egypt'); term('NGA', 'Nigeria'); term('KEN', 'Kenya'); term('ZAF', 'South Africa');
    term('MAR', 'Morocco'); term('ETH', 'Ethiopia'); term('MDG', 'Madagascar'); term('DZA', 'Algeria');
    ask('Where is Kenya?', 'KEN');
    ask('Find Madagascar on the map', 'MDG');
    ask('Which country has the pyramids of Giza?', 'EGY');

    v_type := add_type('Asian countries', 'map:country');
    term('JPN', 'Japan'); term('CHN', 'China'); term('IND', 'India'); term('MNG', 'Mongolia');
    term('VNM', 'Vietnam'); term('THA', 'Thailand'); term('KOR', 'South Korea'); term('KAZ', 'Kazakhstan');
    ask('Where is Mongolia?', 'MNG');
    ask('Find Vietnam on the map', 'VNM');
    ask('Which is the largest landlocked country in the world?', 'KAZ');

    -- ---- Continents (key: continent code) ------------------------------------------------
    v_cat := v_continents;
    v_type := add_type('Continents', 'map:continent');
    term('AF', 'Africa'); term('AN', 'Antarctica'); term('AS', 'Asia'); term('EU', 'Europe');
    term('NA', 'North America'); term('OC', 'Oceania'); term('SA', 'South America');
    ask('Where is South America?', 'SA');
    ask('Which continent is home to the Amazon rainforest?', 'SA');
    ask('Which continent has the most people?', 'AS');
    ask('Which continent is Kenya in?', 'AF');
    ask('Where is Oceania?', 'OC');
    ask('Which continent is covered almost entirely by ice?', 'AN');

    -- ---- Cities (options: "lat,lng") -----------------------------------------------------
    v_cat := v_cities;

    v_type := add_type('European capitals', 'map:city');
    term('Paris', 'Paris', '48.8566,2.3522'); term('Berlin', 'Berlin', '52.5200,13.4050');
    term('Rome', 'Rome', '41.9028,12.4964'); term('Madrid', 'Madrid', '40.4168,-3.7038');
    term('Chisinau', 'Chisinau', '47.0105,28.8638'); term('Bucharest', 'Bucharest', '44.4268,26.1025');
    term('Kyiv', 'Kyiv', '50.4501,30.5234'); term('Lisbon', 'Lisbon', '38.7223,-9.1393');
    term('Warsaw', 'Warsaw', '52.2297,21.0122'); term('Vienna', 'Vienna', '48.2082,16.3738');
    term('Athens', 'Athens', '37.9838,23.7275'); term('Oslo', 'Oslo', '59.9139,10.7522');
    ask('Where is Chisinau?', 'Chisinau');
    ask('Where is Lisbon?', 'Lisbon');
    ask('Find Vienna on the map', 'Vienna');
    ask('Which of these cities is the capital of Norway?', 'Oslo');

    v_type := add_type('World cities', 'map:city');
    term('Tokyo', 'Tokyo', '35.6762,139.6503'); term('New York', 'New York', '40.7128,-74.0060');
    term('Rio de Janeiro', 'Rio de Janeiro', '-22.9068,-43.1729'); term('Sydney', 'Sydney', '-33.8688,151.2093');
    term('Cairo', 'Cairo', '30.0444,31.2357'); term('Mumbai', 'Mumbai', '19.0760,72.8777');
    term('Mexico City', 'Mexico City', '19.4326,-99.1332'); term('Cape Town', 'Cape Town', '-33.9249,18.4241');
    term('Moscow', 'Moscow', '55.7558,37.6173'); term('Buenos Aires', 'Buenos Aires', '-34.6037,-58.3816');
    ask('Where is Tokyo?', 'Tokyo');
    ask('Where is Rio de Janeiro?', 'Rio de Janeiro');
    ask('Where is Cape Town?', 'Cape Town');
    ask('Which city is home to the Opera House on the harbour?', 'Sydney');
    ask('Where is Buenos Aires?', 'Buenos Aires');

    COMMIT;
END;
