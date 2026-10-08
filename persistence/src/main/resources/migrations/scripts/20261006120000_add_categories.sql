-- // add_categories
-- Fills Cities and Continents and adds new top-level categories with subcategories.
-- Idempotent: a category whose NATURAL_ID already exists is skipped, so parents created
-- through the admin UI on some environments are reused instead of duplicated.
DECLARE
    PROCEDURE add_category(p_name VARCHAR2, p_natural_id VARCHAR2, p_parent VARCHAR2 DEFAULT NULL) IS
        v_parent_id NUMBER;
    BEGIN
        IF p_parent IS NOT NULL THEN
            SELECT CAT_ID INTO v_parent_id FROM Q_CATEGORY WHERE NATURAL_ID = p_parent;
        END IF;
        INSERT INTO Q_CATEGORY (CAT_ID, NAME, NATURAL_ID, SUBCATEGORY_ID, VISIBLE, CREATED_DATE)
        SELECT categories_seq.NEXTVAL, p_name, p_natural_id, v_parent_id, 1, trunc(SYSDATE) FROM DUAL
        WHERE NOT EXISTS (SELECT 1 FROM Q_CATEGORY WHERE NATURAL_ID = p_natural_id);
    END;
BEGIN
    add_category('Geography', 'GEOGRAPHY');
    add_category('Cities', 'CITIES', 'GEOGRAPHY');
    add_category('Continents', 'CONTINENTS', 'GEOGRAPHY');

    add_category('Chisinau', 'CHISINAU', 'CITIES');
    add_category('Paris', 'PARIS', 'CITIES');
    add_category('Tokyo', 'TOKYO', 'CITIES');
    add_category('Rio de Janeiro', 'RIO_DE_JANEIRO', 'CITIES');
    add_category('Nairobi', 'NAIROBI', 'CITIES');
    add_category('Oslo', 'OSLO', 'CITIES');
    add_category('Mexico City', 'MEXICO_CITY', 'CITIES');
    add_category('New Delhi', 'NEW_DELHI', 'CITIES');
    add_category('Cairo', 'CAIRO', 'CITIES');
    add_category('Sydney', 'SYDNEY', 'CITIES');
    add_category('Lima', 'LIMA', 'CITIES');
    add_category('Reykjavik', 'REYKJAVIK', 'CITIES');

    add_category('Africa', 'AFRICA', 'CONTINENTS');
    add_category('Antarctica', 'ANTARCTICA', 'CONTINENTS');
    add_category('Asia', 'ASIA', 'CONTINENTS');
    add_category('Europe', 'EUROPE', 'CONTINENTS');
    add_category('North America', 'NORTH_AMERICA', 'CONTINENTS');
    add_category('South America', 'SOUTH_AMERICA', 'CONTINENTS');
    add_category('Oceania', 'OCEANIA', 'CONTINENTS');

    add_category('Science', 'SCIENCE');
    add_category('Physics', 'PHYSICS', 'SCIENCE');
    add_category('Chemistry', 'CHEMISTRY', 'SCIENCE');
    add_category('Biology', 'BIOLOGY', 'SCIENCE');
    add_category('Mathematics', 'MATHEMATICS', 'SCIENCE');

    add_category('Sports', 'SPORTS');
    add_category('Football', 'FOOTBALL', 'SPORTS');
    add_category('Basketball', 'BASKETBALL', 'SPORTS');
    add_category('Tennis', 'TENNIS', 'SPORTS');
    add_category('Olympic Games', 'OLYMPIC_GAMES', 'SPORTS');

    add_category('Music', 'MUSIC');
    add_category('Classical Music', 'CLASSICAL_MUSIC', 'MUSIC');
    add_category('Rock', 'ROCK', 'MUSIC');
    add_category('Pop', 'POP', 'MUSIC');

    add_category('Movies & TV', 'MOVIES_TV');
    add_category('Movies', 'MOVIES', 'MOVIES_TV');
    add_category('TV Series', 'TV_SERIES', 'MOVIES_TV');
    add_category('Animation', 'ANIMATION', 'MOVIES_TV');

    add_category('Animals', 'ANIMALS');
    add_category('Mammals', 'MAMMALS', 'ANIMALS');
    add_category('Birds', 'BIRDS', 'ANIMALS');
    add_category('Marine Life', 'MARINE_LIFE', 'ANIMALS');
    add_category('Insects', 'INSECTS', 'ANIMALS');

    add_category('Art & Literature', 'ART_LITERATURE');
    add_category('Painting', 'PAINTING', 'ART_LITERATURE');
    add_category('Literature', 'LITERATURE', 'ART_LITERATURE');
    add_category('Architecture', 'ARCHITECTURE', 'ART_LITERATURE');

    add_category('Technology', 'TECHNOLOGY');
    add_category('Computers', 'COMPUTERS', 'TECHNOLOGY');
    add_category('Internet', 'INTERNET', 'TECHNOLOGY');
    add_category('Inventions', 'INVENTIONS', 'TECHNOLOGY');
END;
/execute/

-- //@UNDO
-- Geography, Cities and Continents are left in place: they may predate this script.
DELETE FROM Q_CATEGORY WHERE NATURAL_ID IN (
    'CHISINAU', 'PARIS', 'TOKYO', 'RIO_DE_JANEIRO', 'NAIROBI', 'OSLO', 'MEXICO_CITY', 'NEW_DELHI',
    'CAIRO', 'SYDNEY', 'LIMA', 'REYKJAVIK',
    'AFRICA', 'ANTARCTICA', 'ASIA', 'EUROPE', 'NORTH_AMERICA', 'SOUTH_AMERICA', 'OCEANIA',
    'PHYSICS', 'CHEMISTRY', 'BIOLOGY', 'MATHEMATICS',
    'FOOTBALL', 'BASKETBALL', 'TENNIS', 'OLYMPIC_GAMES',
    'CLASSICAL_MUSIC', 'ROCK', 'POP',
    'MOVIES', 'TV_SERIES', 'ANIMATION',
    'MAMMALS', 'BIRDS', 'MARINE_LIFE', 'INSECTS',
    'PAINTING', 'LITERATURE', 'ARCHITECTURE',
    'COMPUTERS', 'INTERNET', 'INVENTIONS')
/execute/
DELETE FROM Q_CATEGORY WHERE NATURAL_ID IN (
    'SCIENCE', 'SPORTS', 'MUSIC', 'MOVIES_TV', 'ANIMALS', 'ART_LITERATURE', 'TECHNOLOGY')
/execute/
