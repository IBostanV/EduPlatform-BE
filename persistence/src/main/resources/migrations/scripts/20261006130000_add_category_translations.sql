-- // add_category_translations
-- Name and description of every category in EN, RU, DE and RO.
-- Idempotent: rows are skipped when the category is missing or already has that language.
DECLARE
    PROCEDURE add_one(p_natural_id VARCHAR2, p_lang VARCHAR2, p_name VARCHAR2, p_description VARCHAR2) IS
    BEGIN
        INSERT INTO Q_CATEGORY_TRANSLATIONS (TRANSL_ID, NAME, DESCRIPTION, LANG_ID, CAT_ID)
        SELECT cat_transl_seq.NEXTVAL, p_name, p_description, l.LANG_ID, c.CAT_ID
        FROM Q_CATEGORY c, Q_LANGUAGE l
        WHERE c.NATURAL_ID = p_natural_id AND l.LANG_CODE = p_lang
          AND NOT EXISTS (SELECT 1 FROM Q_CATEGORY_TRANSLATIONS t WHERE t.CAT_ID = c.CAT_ID AND t.LANG_ID = l.LANG_ID);
    END;

    PROCEDURE t(p_natural_id VARCHAR2,
                p_en VARCHAR2, p_en_d VARCHAR2, p_ru VARCHAR2, p_ru_d VARCHAR2,
                p_de VARCHAR2, p_de_d VARCHAR2, p_ro VARCHAR2, p_ro_d VARCHAR2) IS
    BEGIN
        add_one(p_natural_id, 'EN', p_en, p_en_d);
        add_one(p_natural_id, 'RU', p_ru, p_ru_d);
        add_one(p_natural_id, 'DE', p_de, p_de_d);
        add_one(p_natural_id, 'RO', p_ro, p_ro_d);
    END;
BEGIN
    t('EXPRESS',
      'Express', 'A quick quiz with questions from all categories',
      'Экспресс', 'Быстрая викторина с вопросами из всех категорий',
      'Express', 'Ein schnelles Quiz mit Fragen aus allen Kategorien',
      'Expres', 'Un test rapid cu întrebări din toate categoriile');
    t('GENERAL_KNOWLEDGE',
      'General knowledge', 'A bit of everything',
      'Общие знания', 'Всего понемногу',
      'Allgemeinwissen', 'Von allem ein bisschen',
      'Cultură generală', 'Câte puțin din toate');
    t('HISTORY',
      'History', 'Events, people and eras that shaped the world',
      'История', 'События, люди и эпохи, изменившие мир',
      'Geschichte', 'Ereignisse, Menschen und Epochen, die die Welt prägten',
      'Istorie', 'Evenimente, oameni și epoci care au schimbat lumea');

    -- Cars
    t('CARS',
      'Cars', 'Brands, models and automotive history',
      'Автомобили', 'Марки, модели и история автомобилей',
      'Autos', 'Marken, Modelle und Automobilgeschichte',
      'Mașini', 'Mărci, modele și istoria automobilelor');
    t('AUDI',
      'Audi', 'Models, history and technology of Audi',
      'Audi', 'Модели, история и технологии Audi',
      'Audi', 'Modelle, Geschichte und Technik von Audi',
      'Audi', 'Modelele, istoria și tehnologia Audi');
    t('BMW',
      'BMW', 'Models, history and technology of BMW',
      'BMW', 'Модели, история и технологии BMW',
      'BMW', 'Modelle, Geschichte und Technik von BMW',
      'BMW', 'Modelele, istoria și tehnologia BMW');
    t('RENAULT',
      'Renault', 'Models, history and technology of Renault',
      'Renault', 'Модели, история и технологии Renault',
      'Renault', 'Modelle, Geschichte und Technik von Renault',
      'Renault', 'Modelele, istoria și tehnologia Renault');
    t('BUGATTI',
      'Bugatti', 'Models, history and technology of Bugatti',
      'Bugatti', 'Модели, история и технологии Bugatti',
      'Bugatti', 'Modelle, Geschichte und Technik von Bugatti',
      'Bugatti', 'Modelele, istoria și tehnologia Bugatti');

    -- Universe
    t('UNIVERSE',
      'Universe', 'Stars, galaxies and the mysteries of space',
      'Вселенная', 'Звёзды, галактики и тайны космоса',
      'Universum', 'Sterne, Galaxien und die Rätsel des Weltalls',
      'Univers', 'Stele, galaxii și misterele cosmosului');
    t('PLANETS',
      'Planets', 'The planets of the Solar System',
      'Планеты', 'Планеты Солнечной системы',
      'Planeten', 'Die Planeten des Sonnensystems',
      'Planete', 'Planetele Sistemului Solar');
    t('MERCURY',
      'Mercury', 'The planet closest to the Sun',
      'Меркурий', 'Ближайшая к Солнцу планета',
      'Merkur', 'Der sonnennächste Planet',
      'Mercur', 'Planeta cea mai apropiată de Soare');
    t('VENUS',
      'Venus', 'The hottest planet of the Solar System',
      'Венера', 'Самая горячая планета Солнечной системы',
      'Venus', 'Der heißeste Planet des Sonnensystems',
      'Venus', 'Cea mai fierbinte planetă din Sistemul Solar');
    t('EARTH',
      'Earth', 'Our home planet',
      'Земля', 'Наша родная планета',
      'Erde', 'Unser Heimatplanet',
      'Pământ', 'Planeta noastră natală');
    t('MARS',
      'Mars', 'The Red Planet',
      'Марс', 'Красная планета',
      'Mars', 'Der Rote Planet',
      'Marte', 'Planeta Roșie');
    t('JUPITER',
      'Jupiter', 'The largest planet of the Solar System',
      'Юпитер', 'Крупнейшая планета Солнечной системы',
      'Jupiter', 'Der größte Planet des Sonnensystems',
      'Jupiter', 'Cea mai mare planetă din Sistemul Solar');
    t('SATURN',
      'Saturn', 'The planet with the famous rings',
      'Сатурн', 'Планета со знаменитыми кольцами',
      'Saturn', 'Der Planet mit den berühmten Ringen',
      'Saturn', 'Planeta cu inelele celebre');
    t('URANUS',
      'Uranus', 'The ice giant that rolls on its side',
      'Уран', 'Ледяной гигант, лежащий на боку',
      'Uranus', 'Der Eisriese, der auf der Seite rollt',
      'Uranus', 'Gigantul de gheață care se rostogolește pe o parte');
    t('NEPTUNE',
      'Neptune', 'The windiest planet of the Solar System',
      'Нептун', 'Самая ветреная планета Солнечной системы',
      'Neptun', 'Der windigste Planet des Sonnensystems',
      'Neptun', 'Cea mai vântoasă planetă din Sistemul Solar');

    -- Geography
    t('GEOGRAPHY',
      'Geography', 'Countries, cities and continents of the world',
      'География', 'Страны, города и континенты мира',
      'Geografie', 'Länder, Städte und Kontinente der Welt',
      'Geografie', 'Țări, orașe și continente ale lumii');
    t('COUNTRIES',
      'Countries', 'Flags, people and facts about countries',
      'Страны', 'Флаги, люди и факты о странах',
      'Länder', 'Flaggen, Menschen und Fakten über Länder',
      'Țări', 'Steaguri, oameni și fapte despre țări');
    t('CAPITALS',
      'Capitals', 'Capital cities of the world',
      'Столицы', 'Столицы стран мира',
      'Hauptstädte', 'Hauptstädte der Welt',
      'Capitale', 'Capitalele lumii');
    t('CITIES',
      'Cities', 'Famous cities and their landmarks',
      'Города', 'Знаменитые города и их достопримечательности',
      'Städte', 'Berühmte Städte und ihre Sehenswürdigkeiten',
      'Orașe', 'Orașe celebre și obiectivele lor');
    t('CONTINENTS',
      'Continents', 'The seven continents of our planet',
      'Континенты', 'Семь континентов нашей планеты',
      'Kontinente', 'Die sieben Kontinente unseres Planeten',
      'Continente', 'Cele șapte continente ale planetei');

    t('MDA',
      'Moldova', 'Facts about Moldova',
      'Молдова', 'Факты о Молдове',
      'Moldau', 'Fakten über die Republik Moldau',
      'Moldova', 'Fapte despre Moldova');
    t('FRA',
      'France', 'Facts about France',
      'Франция', 'Факты о Франции',
      'Frankreich', 'Fakten über Frankreich',
      'Franța', 'Fapte despre Franța');
    t('BRA',
      'Brazil', 'Facts about Brazil',
      'Бразилия', 'Факты о Бразилии',
      'Brasilien', 'Fakten über Brasilien',
      'Brazilia', 'Fapte despre Brazilia');
    t('JPN',
      'Japan', 'Facts about Japan',
      'Япония', 'Факты о Японии',
      'Japan', 'Fakten über Japan',
      'Japonia', 'Fapte despre Japonia');
    t('KEN',
      'Kenya', 'Facts about Kenya',
      'Кения', 'Факты о Кении',
      'Kenia', 'Fakten über Kenia',
      'Kenya', 'Fapte despre Kenya');
    t('NOR',
      'Norway', 'Facts about Norway',
      'Норвегия', 'Факты о Норвегии',
      'Norwegen', 'Fakten über Norwegen',
      'Norvegia', 'Fapte despre Norvegia');
    t('MEX',
      'Mexico', 'Facts about Mexico',
      'Мексика', 'Факты о Мексике',
      'Mexiko', 'Fakten über Mexiko',
      'Mexic', 'Fapte despre Mexic');
    t('IND',
      'India', 'Facts about India',
      'Индия', 'Факты об Индии',
      'Indien', 'Fakten über Indien',
      'India', 'Fapte despre India');
    t('EGY',
      'Egypt', 'Facts about Egypt',
      'Египет', 'Факты о Египте',
      'Ägypten', 'Fakten über Ägypten',
      'Egipt', 'Fapte despre Egipt');
    t('AUS',
      'Australia', 'Facts about Australia',
      'Австралия', 'Факты об Австралии',
      'Australien', 'Fakten über Australien',
      'Australia', 'Fapte despre Australia');
    t('PER',
      'Peru', 'Facts about Peru',
      'Перу', 'Факты о Перу',
      'Peru', 'Fakten über Peru',
      'Peru', 'Fapte despre Peru');
    t('ISL',
      'Iceland', 'Facts about Iceland',
      'Исландия', 'Факты об Исландии',
      'Island', 'Fakten über Island',
      'Islanda', 'Fapte despre Islanda');

    t('CHISINAU',
      'Chisinau', 'The capital of Moldova',
      'Кишинёв', 'Столица Молдовы',
      'Chisinau', 'Die Hauptstadt der Republik Moldau',
      'Chișinău', 'Capitala Moldovei');
    t('PARIS',
      'Paris', 'The capital of France',
      'Париж', 'Столица Франции',
      'Paris', 'Die Hauptstadt Frankreichs',
      'Paris', 'Capitala Franței');
    t('TOKYO',
      'Tokyo', 'The capital of Japan',
      'Токио', 'Столица Японии',
      'Tokio', 'Die Hauptstadt Japans',
      'Tokyo', 'Capitala Japoniei');
    t('RIO_DE_JANEIRO',
      'Rio de Janeiro', 'Brazil''s city of carnival',
      'Рио-де-Жанейро', 'Бразильский город карнавала',
      'Rio de Janeiro', 'Brasiliens Stadt des Karnevals',
      'Rio de Janeiro', 'Orașul carnavalului din Brazilia');
    t('NAIROBI',
      'Nairobi', 'The capital of Kenya',
      'Найроби', 'Столица Кении',
      'Nairobi', 'Die Hauptstadt Kenias',
      'Nairobi', 'Capitala Kenyei');
    t('OSLO',
      'Oslo', 'The capital of Norway',
      'Осло', 'Столица Норвегии',
      'Oslo', 'Die Hauptstadt Norwegens',
      'Oslo', 'Capitala Norvegiei');
    t('MEXICO_CITY',
      'Mexico City', 'The capital of Mexico',
      'Мехико', 'Столица Мексики',
      'Mexiko-Stadt', 'Die Hauptstadt Mexikos',
      'Ciudad de México', 'Capitala Mexicului');
    t('NEW_DELHI',
      'New Delhi', 'The capital of India',
      'Нью-Дели', 'Столица Индии',
      'Neu-Delhi', 'Die Hauptstadt Indiens',
      'New Delhi', 'Capitala Indiei');
    t('CAIRO',
      'Cairo', 'The capital of Egypt',
      'Каир', 'Столица Египта',
      'Kairo', 'Die Hauptstadt Ägyptens',
      'Cairo', 'Capitala Egiptului');
    t('SYDNEY',
      'Sydney', 'Australia''s largest city',
      'Сидней', 'Крупнейший город Австралии',
      'Sydney', 'Die größte Stadt Australiens',
      'Sydney', 'Cel mai mare oraș al Australiei');
    t('LIMA',
      'Lima', 'The capital of Peru',
      'Лима', 'Столица Перу',
      'Lima', 'Die Hauptstadt Perus',
      'Lima', 'Capitala Peru');
    t('REYKJAVIK',
      'Reykjavik', 'The capital of Iceland',
      'Рейкьявик', 'Столица Исландии',
      'Reykjavík', 'Die Hauptstadt Islands',
      'Reykjavík', 'Capitala Islandei');

    t('AFRICA',
      'Africa', 'Countries, nature and history of Africa',
      'Африка', 'Страны, природа и история Африки',
      'Afrika', 'Länder, Natur und Geschichte Afrikas',
      'Africa', 'Țările, natura și istoria Africii');
    t('ANTARCTICA',
      'Antarctica', 'The frozen continent at the South Pole',
      'Антарктида', 'Ледяной континент у Южного полюса',
      'Antarktika', 'Der gefrorene Kontinent am Südpol',
      'Antarctida', 'Continentul înghețat de la Polul Sud');
    t('ASIA',
      'Asia', 'Countries, nature and history of Asia',
      'Азия', 'Страны, природа и история Азии',
      'Asien', 'Länder, Natur und Geschichte Asiens',
      'Asia', 'Țările, natura și istoria Asiei');
    t('EUROPE',
      'Europe', 'Countries, nature and history of Europe',
      'Европа', 'Страны, природа и история Европы',
      'Europa', 'Länder, Natur und Geschichte Europas',
      'Europa', 'Țările, natura și istoria Europei');
    t('NORTH_AMERICA',
      'North America', 'Countries, nature and history of North America',
      'Северная Америка', 'Страны, природа и история Северной Америки',
      'Nordamerika', 'Länder, Natur und Geschichte Nordamerikas',
      'America de Nord', 'Țările, natura și istoria Americii de Nord');
    t('SOUTH_AMERICA',
      'South America', 'Countries, nature and history of South America',
      'Южная Америка', 'Страны, природа и история Южной Америки',
      'Südamerika', 'Länder, Natur und Geschichte Südamerikas',
      'America de Sud', 'Țările, natura și istoria Americii de Sud');
    t('OCEANIA',
      'Oceania', 'Australia and the islands of the Pacific',
      'Океания', 'Австралия и острова Тихого океана',
      'Ozeanien', 'Australien und die Inseln des Pazifiks',
      'Oceania', 'Australia și insulele Pacificului');

    -- Science
    t('SCIENCE',
      'Science', 'How the world works',
      'Наука', 'Как устроен мир',
      'Wissenschaft', 'Wie die Welt funktioniert',
      'Știință', 'Cum funcționează lumea');
    t('PHYSICS',
      'Physics', 'Forces, energy and the laws of nature',
      'Физика', 'Силы, энергия и законы природы',
      'Physik', 'Kräfte, Energie und Naturgesetze',
      'Fizică', 'Forțe, energie și legile naturii');
    t('CHEMISTRY',
      'Chemistry', 'Elements, molecules and reactions',
      'Химия', 'Элементы, молекулы и реакции',
      'Chemie', 'Elemente, Moleküle und Reaktionen',
      'Chimie', 'Elemente, molecule și reacții');
    t('BIOLOGY',
      'Biology', 'Life and living organisms',
      'Биология', 'Жизнь и живые организмы',
      'Biologie', 'Das Leben und lebende Organismen',
      'Biologie', 'Viața și organismele vii');
    t('MATHEMATICS',
      'Mathematics', 'Numbers, shapes and puzzles',
      'Математика', 'Числа, фигуры и головоломки',
      'Mathematik', 'Zahlen, Formen und Rätsel',
      'Matematică', 'Numere, forme și enigme');

    -- Sports
    t('SPORTS',
      'Sports', 'Games, athletes and records',
      'Спорт', 'Игры, спортсмены и рекорды',
      'Sport', 'Spiele, Sportler und Rekorde',
      'Sport', 'Jocuri, sportivi și recorduri');
    t('FOOTBALL',
      'Football', 'Clubs, players and tournaments',
      'Футбол', 'Клубы, игроки и турниры',
      'Fußball', 'Vereine, Spieler und Turniere',
      'Fotbal', 'Cluburi, jucători și turnee');
    t('BASKETBALL',
      'Basketball', 'Teams, players and leagues',
      'Баскетбол', 'Команды, игроки и лиги',
      'Basketball', 'Teams, Spieler und Ligen',
      'Baschet', 'Echipe, jucători și ligi');
    t('TENNIS',
      'Tennis', 'Players, tournaments and Grand Slams',
      'Теннис', 'Игроки, турниры и Большой шлем',
      'Tennis', 'Spieler, Turniere und Grand Slams',
      'Tenis', 'Jucători, turnee și Grand Slam-uri');
    t('OLYMPIC_GAMES',
      'Olympic Games', 'Champions and history of the Olympics',
      'Олимпийские игры', 'Чемпионы и история Олимпиад',
      'Olympische Spiele', 'Sieger und Geschichte der Olympischen Spiele',
      'Jocurile Olimpice', 'Campionii și istoria Jocurilor Olimpice');

    -- Music
    t('MUSIC',
      'Music', 'Artists, songs and genres',
      'Музыка', 'Исполнители, песни и жанры',
      'Musik', 'Künstler, Lieder und Genres',
      'Muzică', 'Artiști, cântece și genuri');
    t('CLASSICAL_MUSIC',
      'Classical Music', 'Composers and their masterpieces',
      'Классическая музыка', 'Композиторы и их шедевры',
      'Klassische Musik', 'Komponisten und ihre Meisterwerke',
      'Muzică clasică', 'Compozitori și capodoperele lor');
    t('ROCK',
      'Rock', 'Bands, albums and legends of rock',
      'Рок', 'Группы, альбомы и легенды рока',
      'Rock', 'Bands, Alben und Legenden des Rock',
      'Rock', 'Trupe, albume și legende ale rockului');
    t('POP',
      'Pop', 'Hits and stars of pop music',
      'Поп', 'Хиты и звёзды поп-музыки',
      'Pop', 'Hits und Stars der Popmusik',
      'Pop', 'Hituri și vedete ale muzicii pop');

    -- Movies & TV
    t('MOVIES_TV',
      'Movies & TV', 'Films, series and the people behind them',
      'Кино и ТВ', 'Фильмы, сериалы и люди, которые их создают',
      'Film & TV', 'Filme, Serien und die Menschen dahinter',
      'Filme și TV', 'Filme, seriale și oamenii din spatele lor');
    t('MOVIES',
      'Movies', 'Films, actors and directors',
      'Фильмы', 'Фильмы, актёры и режиссёры',
      'Filme', 'Filme, Schauspieler und Regisseure',
      'Filme', 'Filme, actori și regizori');
    t('TV_SERIES',
      'TV Series', 'Shows, characters and episodes',
      'Сериалы', 'Шоу, персонажи и эпизоды',
      'Serien', 'Serien, Figuren und Episoden',
      'Seriale', 'Seriale, personaje și episoade');
    t('ANIMATION',
      'Animation', 'Cartoons and animated films',
      'Мультфильмы', 'Мультфильмы и анимационное кино',
      'Animation', 'Zeichentrick- und Animationsfilme',
      'Animație', 'Desene animate și filme de animație');

    -- Animals
    t('ANIMALS',
      'Animals', 'The animal kingdom',
      'Животные', 'Животный мир',
      'Tiere', 'Die Tierwelt',
      'Animale', 'Regnul animal');
    t('MAMMALS',
      'Mammals', 'From mice to whales',
      'Млекопитающие', 'От мышей до китов',
      'Säugetiere', 'Von Mäusen bis zu Walen',
      'Mamifere', 'De la șoareci la balene');
    t('BIRDS',
      'Birds', 'Feathered inhabitants of the planet',
      'Птицы', 'Пернатые обитатели планеты',
      'Vögel', 'Gefiederte Bewohner des Planeten',
      'Păsări', 'Locuitorii înaripați ai planetei');
    t('MARINE_LIFE',
      'Marine Life', 'Creatures of the seas and oceans',
      'Морская жизнь', 'Обитатели морей и океанов',
      'Meeresleben', 'Bewohner der Meere und Ozeane',
      'Viața marină', 'Viețuitoarele mărilor și oceanelor');
    t('INSECTS',
      'Insects', 'The small world of bugs',
      'Насекомые', 'Маленький мир букашек',
      'Insekten', 'Die kleine Welt der Krabbeltiere',
      'Insecte', 'Lumea mică a gâzelor');

    -- Art & Literature
    t('ART_LITERATURE',
      'Art & Literature', 'Great works and their creators',
      'Искусство и литература', 'Великие произведения и их авторы',
      'Kunst & Literatur', 'Große Werke und ihre Schöpfer',
      'Artă și literatură', 'Opere mari și creatorii lor');
    t('PAINTING',
      'Painting', 'Painters and famous paintings',
      'Живопись', 'Художники и знаменитые картины',
      'Malerei', 'Maler und berühmte Gemälde',
      'Pictură', 'Pictori și tablouri celebre');
    t('LITERATURE',
      'Literature', 'Writers, books and characters',
      'Литература', 'Писатели, книги и персонажи',
      'Literatur', 'Schriftsteller, Bücher und Figuren',
      'Literatură', 'Scriitori, cărți și personaje');
    t('ARCHITECTURE',
      'Architecture', 'Buildings, styles and architects',
      'Архитектура', 'Здания, стили и архитекторы',
      'Architektur', 'Bauwerke, Stile und Architekten',
      'Arhitectură', 'Clădiri, stiluri și arhitecți');

    -- Technology
    t('TECHNOLOGY',
      'Technology', 'Gadgets, computers and innovation',
      'Технологии', 'Гаджеты, компьютеры и инновации',
      'Technologie', 'Geräte, Computer und Innovation',
      'Tehnologie', 'Gadgeturi, calculatoare și inovație');
    t('COMPUTERS',
      'Computers', 'Hardware, software and their history',
      'Компьютеры', 'Железо, программы и их история',
      'Computer', 'Hardware, Software und ihre Geschichte',
      'Calculatoare', 'Hardware, software și istoria lor');
    t('INTERNET',
      'Internet', 'The web, sites and online life',
      'Интернет', 'Сеть, сайты и онлайн-жизнь',
      'Internet', 'Das Netz, Websites und das Online-Leben',
      'Internet', 'Rețeaua, site-urile și viața online');
    t('INVENTIONS',
      'Inventions', 'Inventions that changed the world',
      'Изобретения', 'Изобретения, изменившие мир',
      'Erfindungen', 'Erfindungen, die die Welt veränderten',
      'Invenții', 'Invenții care au schimbat lumea');
END;
/execute/

-- //@UNDO
DELETE FROM Q_CATEGORY_TRANSLATIONS WHERE 1 = 1
/execute/
