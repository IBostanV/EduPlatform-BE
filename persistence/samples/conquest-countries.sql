-- Not a migration: run it by hand against a dev database (e.g. from SQL Developer or IntelliJ,
-- as one statement from DECLARE to END). It commits itself and refuses to run twice.
--
-- The countries of the conquest game (/conquest). Each country is a category under COUNTRIES
-- whose natural id is its ISO 3166 alpha-3 code — the same code the browser's map already knows
-- its shapes by, which is the whole bridge between the two sides.
--
-- Questions are grouped by what they ask, not by country: all the capitals share one glossary
-- type, all the currencies another, and so on for six kinds. Wrong options are drawn from the
-- right answer's type and nowhere else, so "What is the capital of Moldova?" is answered
-- against other capitals rather than a currency and two languages, which would give it away.
--
-- Values are kept unique within a type on purpose: two countries both answering "Euro" would
-- collapse into one option, leaving the question short.
--
-- Runs with or without map-questions.sql: Geography > Countries is reused if it is already there.
--
-- EXCLUDE_TYPE 254 = every quiz type except SINGLE_CHOICE (1).
DECLARE
    v_parent NUMBER; v_cat NUMBER; v_type NUMBER; v_existing NUMBER;
    v_capitals NUMBER; v_currencies NUMBER; v_languages NUMBER; v_landmarks NUMBER;
    v_cities NUMBER; v_dishes NUMBER;

    FUNCTION add_category(p_name VARCHAR2, p_natural VARCHAR2, p_parent NUMBER) RETURN NUMBER IS
        v_id NUMBER := CATEGORIES_SEQ.NEXTVAL;
    BEGIN
        INSERT INTO Q_CATEGORY (CAT_ID, NAME, NATURAL_ID, SUBCATEGORY_ID, VISIBLE, CREATED_BY, CREATED_DATE)
        VALUES (v_id, p_name, p_natural, p_parent, 1, 1, SYSDATE);
        RETURN v_id;
    END;

    FUNCTION add_type(p_name VARCHAR2) RETURN NUMBER IS
        v_id NUMBER := GLOSSARY_TYPE_SEQ.NEXTVAL;
    BEGIN
        INSERT INTO Q_GLOSSARY_TYPE (ID, NAME, OPTIONS, IS_ACTIVE, CREATED_BY, CREATED_DATE)
        VALUES (v_id, p_name, NULL, 1, 1, SYSDATE);
        RETURN v_id;
    END;

    -- A term in the type currently in v_type, under the country currently in v_cat. The key is
    -- only an identifier; the value is what the player reads as an option.
    PROCEDURE term(p_key VARCHAR2, p_value VARCHAR2) IS
    BEGIN
        INSERT INTO Q_GLOSSARY (TERM_ID, KEY, VALUE, CAT_ID, TYPE_ID, OPTIONS, IS_ACTIVE, CREATED_BY, CREATED_DATE)
        VALUES (GLOSSARIES_SEQ.NEXTVAL, p_key, p_value, v_cat, v_type, NULL, 1, 1, SYSDATE);
    END;

    -- A question whose one right answer is the term with this key, in the type in v_type.
    PROCEDURE ask(p_content VARCHAR2, p_key VARCHAR2) IS
        v_question NUMBER := QUESTIONS_SEQ.NEXTVAL;
        v_term NUMBER; v_value VARCHAR2(100);
    BEGIN
        SELECT TERM_ID, VALUE INTO v_term, v_value FROM Q_GLOSSARY WHERE KEY = p_key AND TYPE_ID = v_type;
        INSERT INTO Q_QUESTION (QUESTION_ID, ACCOUNT_ID, TYPE, CAT_ID, IS_ACTIVE, COMPLEXITY_LEVEL, PRIORITY, CONTENT, ATTRIBUTES, EXCLUDE_TYPE, CREATED_BY, CREATED_DATE)
        VALUES (v_question, 1, 'CREATED', v_cat, 1, 1, 1, p_content, 'ANSWER_BY_VALUE', 254, 1, SYSDATE);
        INSERT INTO Q_ANSWER (ANS_ID, QUESTION_ID, CONTENT, TERM_ID, CREATED_BY, CREATED_DATE)
        VALUES (ANSWERS_SEQ.NEXTVAL, v_question, v_value, v_term, 1, SYSDATE);
    END;

    -- One country: its category, then the six things the game asks about it. Six rather than a
    -- couple, because a conquest is decided on time once everyone has answered everything right.
    PROCEDURE country(p_name VARCHAR2, p_iso3 VARCHAR2, p_capital VARCHAR2, p_currency VARCHAR2,
                      p_language VARCHAR2, p_landmark VARCHAR2, p_city VARCHAR2, p_dish VARCHAR2) IS
    BEGIN
        v_cat := add_category(p_name, p_iso3, v_parent);

        v_type := v_capitals;
        term('CAP_' || p_iso3, p_capital);
        ask('What is the capital of ' || p_name || '?', 'CAP_' || p_iso3);

        v_type := v_currencies;
        term('CUR_' || p_iso3, p_currency);
        ask('Which currency is used in ' || p_name || '?', 'CUR_' || p_iso3);

        v_type := v_languages;
        term('LAN_' || p_iso3, p_language);
        ask('Which language is spoken in ' || p_name || '?', 'LAN_' || p_iso3);

        v_type := v_landmarks;
        term('LMK_' || p_iso3, p_landmark);
        ask('Which of these landmarks is in ' || p_name || '?', 'LMK_' || p_iso3);

        v_type := v_cities;
        term('CTY_' || p_iso3, p_city);
        ask('Which is the largest city in ' || p_name || '?', 'CTY_' || p_iso3);

        v_type := v_dishes;
        term('DSH_' || p_iso3, p_dish);
        ask('Which dish comes from ' || p_name || '?', 'DSH_' || p_iso3);
    END;
BEGIN
    -- Geography > Countries, reused when map-questions.sql has already made it.
    BEGIN
        SELECT CAT_ID INTO v_parent FROM Q_CATEGORY WHERE NATURAL_ID = 'COUNTRIES';
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            BEGIN
                SELECT CAT_ID INTO v_cat FROM Q_CATEGORY WHERE NATURAL_ID = 'GEOGRAPHY';
            EXCEPTION
                WHEN NO_DATA_FOUND THEN v_cat := add_category('Geography', 'GEOGRAPHY', NULL);
            END;
            v_parent := add_category('Countries', 'COUNTRIES', v_cat);
    END;

    SELECT COUNT(*) INTO v_existing FROM Q_CATEGORY
    WHERE SUBCATEGORY_ID = v_parent AND LENGTH(NATURAL_ID) = 3;
    IF v_existing > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Conquest countries already exist, samples not added again');
    END IF;

    v_capitals := add_type('Capital cities');
    v_currencies := add_type('Currencies');
    v_languages := add_type('Official languages');
    v_landmarks := add_type('Landmarks');
    v_cities := add_type('Largest cities');
    v_dishes := add_type('National dishes');

    --       name            ISO3   capital         currency            language      landmark                  largest city   dish
    country('Moldova',       'MDA', 'Chisinau',     'Moldovan leu',     'Romanian',   'Orheiul Vechi',          'Chisinau',    'Mamaliga');
    country('France',        'FRA', 'Paris',        'Euro',             'French',     'The Eiffel Tower',       'Paris',       'Ratatouille');
    country('Brazil',        'BRA', 'Brasilia',     'Brazilian real',   'Portuguese', 'Christ the Redeemer',    'Sao Paulo',   'Feijoada');
    country('Japan',         'JPN', 'Tokyo',        'Japanese yen',     'Japanese',   'Mount Fuji',             'Tokyo',       'Sushi');
    country('Kenya',         'KEN', 'Nairobi',      'Kenyan shilling',  'Swahili',    'The Maasai Mara',        'Nairobi',     'Ugali');
    country('Norway',        'NOR', 'Oslo',         'Norwegian krone',  'Norwegian',  'The Geirangerfjord',     'Oslo',        'Rakfisk');
    country('Mexico',        'MEX', 'Mexico City',  'Mexican peso',     'Spanish',    'Chichen Itza',           'Mexico City', 'Tacos');
    country('India',         'IND', 'New Delhi',    'Indian rupee',     'Hindi',      'The Taj Mahal',          'Mumbai',      'Biryani');
    country('Egypt',         'EGY', 'Cairo',        'Egyptian pound',   'Arabic',     'The Pyramids of Giza',   'Cairo',       'Koshari');
    country('Australia',     'AUS', 'Canberra',     'Australian dollar','English',    'The Sydney Opera House', 'Sydney',      'Lamingtons');
    country('Peru',          'PER', 'Lima',         'Peruvian sol',     'Quechua',    'Machu Picchu',           'Lima',        'Ceviche');
    country('Iceland',       'ISL', 'Reykjavik',    'Icelandic krona',  'Icelandic',  'The Blue Lagoon',        'Reykjavik',   'Hakarl');

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Added 12 conquest countries with 6 questions each.');
END;
