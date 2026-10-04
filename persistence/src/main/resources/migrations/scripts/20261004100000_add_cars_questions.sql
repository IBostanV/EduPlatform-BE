-- // add_cars_questions
-- Cars category (already exists, looked up by name): 100 single-answer questions. Every answer is a glossary term, and terms are
-- grouped by glossary type (brands, countries, parts, people, models, motorsport), so the wrong
-- options a quiz draws (GlossaryRepository.findWrongOptions) are always the same kind of thing.
-- Terms nobody asks about are there to widen the pool of wrong options.
--
-- EXCLUDE_TYPE 32 = every quiz type except MAP_CHOICE.
DECLARE
    v_type NUMBER;
    v_cat NUMBER;

    PROCEDURE add_type(p_name VARCHAR2) IS
    BEGIN
        v_type := GLOSSARY_TYPE_SEQ.NEXTVAL;
        INSERT INTO Q_GLOSSARY_TYPE (ID, NAME, IS_ACTIVE, CREATED_BY, CREATED_DATE)
        VALUES (v_type, p_name, 1, 1, SYSDATE);
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
        SELECT TERM_ID, VALUE INTO v_term, v_value FROM Q_GLOSSARY WHERE KEY = p_key AND CAT_ID = v_cat AND TYPE_ID = v_type;
        INSERT INTO Q_QUESTION (QUESTION_ID, ACCOUNT_ID, TYPE, CAT_ID, IS_ACTIVE, COMPLEXITY_LEVEL, PRIORITY, CONTENT, ATTRIBUTES, EXCLUDE_TYPE, CREATED_BY, CREATED_DATE)
        VALUES (v_question, 1, 'CREATED', v_cat, 1, 1, 1, p_content, 'ANSWER_BY_VALUE', 32, 1, SYSDATE);
        INSERT INTO Q_ANSWER (ANS_ID, QUESTION_ID, CONTENT, TERM_ID, CREATED_BY, CREATED_DATE)
        VALUES (ANSWERS_SEQ.NEXTVAL, v_question, v_value, v_term, 1, SYSDATE);
    END;
BEGIN
    SELECT CAT_ID INTO v_cat FROM Q_CATEGORY WHERE NAME = 'Cars';

    add_type('Car brands');
    term('CAR_BRAND_FERRARI', 'Ferrari'); term('CAR_BRAND_LAMBORGHINI', 'Lamborghini');
    term('CAR_BRAND_PORSCHE', 'Porsche'); term('CAR_BRAND_BMW', 'BMW');
    term('CAR_BRAND_MERCEDES', 'Mercedes-Benz'); term('CAR_BRAND_AUDI', 'Audi');
    term('CAR_BRAND_VOLKSWAGEN', 'Volkswagen'); term('CAR_BRAND_TOYOTA', 'Toyota');
    term('CAR_BRAND_HONDA', 'Honda'); term('CAR_BRAND_NISSAN', 'Nissan');
    term('CAR_BRAND_FORD', 'Ford'); term('CAR_BRAND_CHEVROLET', 'Chevrolet');
    term('CAR_BRAND_TESLA', 'Tesla'); term('CAR_BRAND_VOLVO', 'Volvo');
    term('CAR_BRAND_ROLLS_ROYCE', 'Rolls-Royce'); term('CAR_BRAND_BUGATTI', 'Bugatti');
    term('CAR_BRAND_ASTON_MARTIN', 'Aston Martin'); term('CAR_BRAND_JAGUAR', 'Jaguar');
    term('CAR_BRAND_SUBARU', 'Subaru'); term('CAR_BRAND_MAZDA', 'Mazda');
    term('CAR_BRAND_PEUGEOT', 'Peugeot'); term('CAR_BRAND_RENAULT', 'Renault');
    term('CAR_BRAND_CITROEN', 'Citroen'); term('CAR_BRAND_HYUNDAI', 'Hyundai');
    term('CAR_BRAND_KIA', 'Kia'); term('CAR_BRAND_SKODA', 'Skoda');
    term('CAR_BRAND_DACIA', 'Dacia'); term('CAR_BRAND_DODGE', 'Dodge');
    term('CAR_BRAND_JEEP', 'Jeep'); term('CAR_BRAND_LAND_ROVER', 'Land Rover');
    term('CAR_BRAND_LEXUS', 'Lexus'); term('CAR_BRAND_ALFA_ROMEO', 'Alfa Romeo');
    term('CAR_BRAND_MCLAREN', 'McLaren'); term('CAR_BRAND_BENTLEY', 'Bentley');
    term('CAR_BRAND_MASERATI', 'Maserati'); term('CAR_BRAND_MINI', 'Mini');
    term('CAR_BRAND_KOENIGSEGG', 'Koenigsegg'); term('CAR_BRAND_LOTUS', 'Lotus');
    term('CAR_BRAND_LADA', 'Lada'); term('CAR_BRAND_FIAT', 'Fiat');
    ask('Which carmaker''s emblem is known as the "Cavallino Rampante" (prancing horse)?', 'CAR_BRAND_FERRARI');
    ask('Which carmaker has a raging bull as its logo?', 'CAR_BRAND_LAMBORGHINI');
    ask('Which carmaker''s logo is four interlocking rings?', 'CAR_BRAND_AUDI');
    ask('Which carmaker''s logo is a three-pointed star?', 'CAR_BRAND_MERCEDES');
    ask('Which carmaker''s logo is a blue and white roundel?', 'CAR_BRAND_BMW');
    ask('Which carmaker builds the 911 sports car?', 'CAR_BRAND_PORSCHE');
    ask('Which carmaker built the Model T?', 'CAR_BRAND_FORD');
    ask('Which carmaker builds the Corolla?', 'CAR_BRAND_TOYOTA');
    ask('Which carmaker builds the Civic?', 'CAR_BRAND_HONDA');
    ask('Which carmaker builds the Golf and built the original Beetle?', 'CAR_BRAND_VOLKSWAGEN');
    ask('Which carmaker builds the Model S and Model 3?', 'CAR_BRAND_TESLA');
    ask('Which carmaker builds the Corvette?', 'CAR_BRAND_CHEVROLET');
    ask('Which carmaker''s cars carry the "Spirit of Ecstasy" bonnet mascot?', 'CAR_BRAND_ROLLS_ROYCE');
    ask('Which carmaker built the Veyron and the Chiron?', 'CAR_BRAND_BUGATTI');
    ask('Which carmaker built the DB5 driven by James Bond in Goldfinger?', 'CAR_BRAND_ASTON_MARTIN');
    ask('Which carmaker''s logo shows six stars of the Pleiades cluster?', 'CAR_BRAND_SUBARU');
    ask('Which carmaker builds the MX-5 roadster?', 'CAR_BRAND_MAZDA');
    ask('Which carmaker introduced the three-point seat belt in 1959?', 'CAR_BRAND_VOLVO');
    ask('Which Czech carmaker is part of the Volkswagen Group?', 'CAR_BRAND_SKODA');
    ask('Which Romanian carmaker is owned by Renault?', 'CAR_BRAND_DACIA');
    ask('Which carmaker builds the Wrangler off-roader?', 'CAR_BRAND_JEEP');
    ask('Which British carmaker builds the Defender and the Range Rover?', 'CAR_BRAND_LAND_ROVER');
    ask('Which carmaker is the luxury division of Toyota?', 'CAR_BRAND_LEXUS');
    ask('Which Italian carmaker''s badge shows a serpent (the Biscione) swallowing a man?', 'CAR_BRAND_ALFA_ROMEO');
    ask('Which carmaker built the F1 road car with a central driving seat?', 'CAR_BRAND_MCLAREN');
    ask('Which carmaker''s logo is Neptune''s trident?', 'CAR_BRAND_MASERATI');
    ask('Which Swedish carmaker builds the Agera and the Jesko hypercars?', 'CAR_BRAND_KOENIGSEGG');
    ask('Which carmaker builds the GT-R, nicknamed "Godzilla"?', 'CAR_BRAND_NISSAN');
    ask('Which carmaker''s logo is a lion?', 'CAR_BRAND_PEUGEOT');
    ask('Which carmaker''s logo is a double chevron?', 'CAR_BRAND_CITROEN');
    ask('Which carmaker builds the Clio?', 'CAR_BRAND_RENAULT');
    ask('Which carmaker builds the Charger and the Challenger muscle cars?', 'CAR_BRAND_DODGE');
    ask('Which carmaker''s logo is a leaping cat?', 'CAR_BRAND_JAGUAR');
    ask('Which carmaker builds the Elise and the Exige?', 'CAR_BRAND_LOTUS');
    ask('Which South Korean carmaker builds the Sportage and the Ceed?', 'CAR_BRAND_KIA');
    ask('Which Russian carmaker builds the Niva off-roader?', 'CAR_BRAND_LADA');

    add_type('Car countries');
    term('CAR_COUNTRY_GERMANY', 'Germany'); term('CAR_COUNTRY_ITALY', 'Italy');
    term('CAR_COUNTRY_JAPAN', 'Japan'); term('CAR_COUNTRY_USA', 'United States');
    term('CAR_COUNTRY_UK', 'United Kingdom'); term('CAR_COUNTRY_FRANCE', 'France');
    term('CAR_COUNTRY_SWEDEN', 'Sweden'); term('CAR_COUNTRY_SOUTH_KOREA', 'South Korea');
    term('CAR_COUNTRY_CZECHIA', 'Czech Republic'); term('CAR_COUNTRY_ROMANIA', 'Romania');
    term('CAR_COUNTRY_INDIA', 'India'); term('CAR_COUNTRY_CHINA', 'China');
    ask('Which country does Volvo come from?', 'CAR_COUNTRY_SWEDEN');
    ask('Which country does Skoda come from?', 'CAR_COUNTRY_CZECHIA');
    ask('Which country does Hyundai come from?', 'CAR_COUNTRY_SOUTH_KOREA');
    ask('Which country does Tata Motors come from?', 'CAR_COUNTRY_INDIA');
    ask('Which country does BYD come from?', 'CAR_COUNTRY_CHINA');
    ask('Which country does Peugeot come from?', 'CAR_COUNTRY_FRANCE');
    ask('Which country does Mazda come from?', 'CAR_COUNTRY_JAPAN');
    ask('Which country does Porsche come from?', 'CAR_COUNTRY_GERMANY');
    ask('Which country does Rolls-Royce come from?', 'CAR_COUNTRY_UK');
    ask('Which country does Chevrolet come from?', 'CAR_COUNTRY_USA');
    ask('Which country does Lamborghini come from?', 'CAR_COUNTRY_ITALY');
    ask('Which country does Dacia come from?', 'CAR_COUNTRY_ROMANIA');

    add_type('Car parts');
    term('CAR_PART_CRANKSHAFT', 'Crankshaft'); term('CAR_PART_RADIATOR', 'Radiator');
    term('CAR_PART_ALTERNATOR', 'Alternator'); term('CAR_PART_SPARK_PLUG', 'Spark plug');
    term('CAR_PART_CATALYTIC', 'Catalytic converter'); term('CAR_PART_TURBO', 'Turbocharger');
    term('CAR_PART_DIFFERENTIAL', 'Differential'); term('CAR_PART_CLUTCH', 'Clutch');
    term('CAR_PART_SHOCK', 'Shock absorber'); term('CAR_PART_MUFFLER', 'Muffler');
    term('CAR_PART_TIMING_BELT', 'Timing belt'); term('CAR_PART_INJECTOR', 'Fuel injector');
    term('CAR_PART_STARTER', 'Starter motor'); term('CAR_PART_CARBURETOR', 'Carburetor');
    term('CAR_PART_CALIPER', 'Brake caliper'); term('CAR_PART_GEARBOX', 'Gearbox');
    ask('Which part turns the up-and-down motion of the pistons into rotation?', 'CAR_PART_CRANKSHAFT');
    ask('Which part cools the engine coolant with the air flowing through it?', 'CAR_PART_RADIATOR');
    ask('Which part charges the battery while the engine is running?', 'CAR_PART_ALTERNATOR');
    ask('Which part ignites the air-fuel mixture in a petrol engine?', 'CAR_PART_SPARK_PLUG');
    ask('Which part cleans exhaust gases using metals like platinum and palladium?', 'CAR_PART_CATALYTIC');
    ask('Which part uses the exhaust gases to force more air into the engine?', 'CAR_PART_TURBO');
    ask('Which part lets the driven wheels turn at different speeds in a corner?', 'CAR_PART_DIFFERENTIAL');
    ask('Which part disconnects the engine from the gearbox when you press the left pedal of a manual car?', 'CAR_PART_CLUTCH');
    ask('Which part damps the bouncing of the suspension springs?', 'CAR_PART_SHOCK');
    ask('Which part of the exhaust system reduces engine noise?', 'CAR_PART_MUFFLER');
    ask('Which part keeps the camshaft in sync with the crankshaft?', 'CAR_PART_TIMING_BELT');
    ask('Which part sprays fuel into the engine in modern cars?', 'CAR_PART_INJECTOR');
    ask('Which electric motor cranks the engine to get it running?', 'CAR_PART_STARTER');
    ask('Which device mixed air and fuel in older engines before fuel injection took over?', 'CAR_PART_CARBURETOR');
    ask('Which part presses the brake pads against the brake disc?', 'CAR_PART_CALIPER');

    add_type('Automotive people');
    term('CAR_PERSON_BENZ', 'Karl Benz'); term('CAR_PERSON_FORD', 'Henry Ford');
    term('CAR_PERSON_ENZO_FERRARI', 'Enzo Ferrari'); term('CAR_PERSON_LAMBORGHINI', 'Ferruccio Lamborghini');
    term('CAR_PERSON_PORSCHE', 'Ferdinand Porsche'); term('CAR_PERSON_DIESEL', 'Rudolf Diesel');
    term('CAR_PERSON_OTTO', 'Nikolaus Otto'); term('CAR_PERSON_HONDA', 'Soichiro Honda');
    term('CAR_PERSON_TOYODA', 'Kiichiro Toyoda'); term('CAR_PERSON_CHEVROLET', 'Louis Chevrolet');
    term('CAR_PERSON_SCHUMACHER', 'Michael Schumacher'); term('CAR_PERSON_SENNA', 'Ayrton Senna');
    term('CAR_PERSON_HAMILTON', 'Lewis Hamilton'); term('CAR_PERSON_DAIMLER', 'Gottlieb Daimler');
    term('CAR_PERSON_CITROEN', 'Andre Citroen'); term('CAR_PERSON_MUSK', 'Elon Musk');
    ask('Who built the Patent-Motorwagen of 1886, often called the first automobile?', 'CAR_PERSON_BENZ');
    ask('Who introduced the moving assembly line for car production in 1913?', 'CAR_PERSON_FORD');
    ask('Who founded the Scuderia Ferrari racing team?', 'CAR_PERSON_ENZO_FERRARI');
    ask('Which tractor maker started building supercars after a quarrel with Enzo Ferrari?', 'CAR_PERSON_LAMBORGHINI');
    ask('Which engineer designed the original Volkswagen Beetle?', 'CAR_PERSON_PORSCHE');
    ask('Who invented the compression-ignition engine that bears his name?', 'CAR_PERSON_DIESEL');
    ask('Who built the first four-stroke internal combustion engine in 1876?', 'CAR_PERSON_OTTO');
    ask('Who founded the Honda Motor Company?', 'CAR_PERSON_HONDA');
    ask('Who founded the Toyota Motor Corporation?', 'CAR_PERSON_TOYODA');
    ask('Which Swiss-born racing driver co-founded an American car brand in 1911?', 'CAR_PERSON_CHEVROLET');
    ask('Which seven-time Formula 1 champion won five of his titles with Ferrari?', 'CAR_PERSON_SCHUMACHER');
    ask('Which Brazilian three-time Formula 1 champion died at Imola in 1994?', 'CAR_PERSON_SENNA');
    ask('Which seven-time Formula 1 champion won six of his titles with Mercedes?', 'CAR_PERSON_HAMILTON');
    ask('Who, together with Wilhelm Maybach, built one of the first high-speed petrol engines?', 'CAR_PERSON_DAIMLER');
    ask('Which French engineer founded a car brand in 1919 with a double chevron logo?', 'CAR_PERSON_CITROEN');

    add_type('Car models');
    term('CAR_MODEL_COROLLA', 'Toyota Corolla'); term('CAR_MODEL_DELOREAN', 'DeLorean DMC-12');
    term('CAR_MODEL_PRIUS', 'Toyota Prius'); term('CAR_MODEL_MUSTANG', 'Ford Mustang');
    term('CAR_MODEL_MODEL_T', 'Ford Model T'); term('CAR_MODEL_F40', 'Ferrari F40');
    term('CAR_MODEL_COUNTACH', 'Lamborghini Countach'); term('CAR_MODEL_VEYRON', 'Bugatti Veyron');
    term('CAR_MODEL_BEETLE', 'Volkswagen Beetle'); term('CAR_MODEL_MINI', 'Mini Cooper');
    term('CAR_MODEL_CORVETTE', 'Chevrolet Corvette'); term('CAR_MODEL_GOLF', 'Volkswagen Golf');
    term('CAR_MODEL_MODEL_Y', 'Tesla Model Y'); term('CAR_MODEL_LAND_CRUISER', 'Toyota Land Cruiser');
    term('CAR_MODEL_911', 'Porsche 911'); term('CAR_MODEL_2CV', 'Citroen 2CV');
    ask('Which car is the best-selling nameplate of all time?', 'CAR_MODEL_COROLLA');
    ask('Which car became a time machine in the film Back to the Future?', 'CAR_MODEL_DELOREAN');
    ask('Which car, launched in 1997, was the first mass-produced hybrid?', 'CAR_MODEL_PRIUS');
    ask('Which car, launched in 1964, created the American "pony car" class?', 'CAR_MODEL_MUSTANG');
    ask('Which car, built from 1908 to 1927, put America on wheels?', 'CAR_MODEL_MODEL_T');
    ask('Which car, launched in 1987, was the last one Enzo Ferrari personally approved?', 'CAR_MODEL_F40');
    ask('Which wedge-shaped supercar with scissor doors went on sale in 1974?', 'CAR_MODEL_COUNTACH');
    ask('Which car became the first production car to exceed 400 km/h, in 2005?', 'CAR_MODEL_VEYRON');
    ask('Which car played Herbie in the "Love Bug" films?', 'CAR_MODEL_BEETLE');
    ask('Which small car designed by Alec Issigonis won the 1964 Monte Carlo Rally?', 'CAR_MODEL_MINI');
    ask('Which American sports car with a fibreglass body debuted at the 1953 Motorama?', 'CAR_MODEL_CORVETTE');
    ask('Which hatchback, launched in 1974, made the "GTI" badge famous?', 'CAR_MODEL_GOLF');
    ask('Which electric car was the best-selling car in the world in 2023?', 'CAR_MODEL_MODEL_Y');
    ask('Which off-roader, built since 1951, is famous for its reliability in remote regions?', 'CAR_MODEL_LAND_CRUISER');
    ask('Which sports car with a rear-mounted flat-six engine debuted in 1963?', 'CAR_MODEL_911');
    ask('Which French car was designed to carry eggs across a ploughed field without breaking them?', 'CAR_MODEL_2CV');

    add_type('Motorsport');
    term('CAR_RACE_LE_MANS', '24 Hours of Le Mans'); term('CAR_RACE_INDY_500', 'Indianapolis 500');
    term('CAR_RACE_MONACO', 'Monaco Grand Prix'); term('CAR_RACE_DAKAR', 'Dakar Rally');
    term('CAR_RACE_NURBURGRING', 'Nurburgring'); term('CAR_RACE_MONZA', 'Monza');
    term('CAR_RACE_SILVERSTONE', 'Silverstone'); term('CAR_RACE_SPA', 'Spa-Francorchamps');
    ask('In which race does the winner traditionally drink a bottle of milk?', 'CAR_RACE_INDY_500');
    ask('Which endurance race is held on the Circuit de la Sarthe?', 'CAR_RACE_LE_MANS');
    ask('Which Formula 1 race runs through the streets of Monte Carlo?', 'CAR_RACE_MONACO');
    ask('Which off-road rally originally ran from Paris to the capital of Senegal?', 'CAR_RACE_DAKAR');
    ask('Which German circuit is nicknamed "The Green Hell"?', 'CAR_RACE_NURBURGRING');
    ask('Which Italian circuit is known as the "Temple of Speed"?', 'CAR_RACE_MONZA');
END;
/execute/

-- //@UNDO
-- Removes only what this migration added: its glossary types, their terms and the questions
-- answered by those terms. The Cars category and anything else in it stay.
DECLARE
    v_cat NUMBER;
BEGIN
    SELECT CAT_ID INTO v_cat FROM Q_CATEGORY WHERE NAME = 'Cars';
    FOR t IN (SELECT DISTINCT gt.ID FROM Q_GLOSSARY_TYPE gt JOIN Q_GLOSSARY g ON g.TYPE_ID = gt.ID
              WHERE g.CAT_ID = v_cat
                AND gt.NAME IN ('Car brands', 'Car countries', 'Car parts', 'Automotive people', 'Car models', 'Motorsport')) LOOP
        DELETE FROM Q_QUESTION WHERE QUESTION_ID IN (
            SELECT a.QUESTION_ID FROM Q_ANSWER a JOIN Q_GLOSSARY g ON g.TERM_ID = a.TERM_ID WHERE g.TYPE_ID = t.ID);
        DELETE FROM Q_GLOSSARY WHERE TYPE_ID = t.ID;
        DELETE FROM Q_GLOSSARY_TYPE WHERE ID = t.ID;
    END LOOP;
END;
/execute/
