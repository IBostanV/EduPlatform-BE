-- // add_occupations
-- What the occupation list on the profile offers. DOMAIN is what an occupation is close to: a
-- regular expression of word beginnings matched, ignoring case, against category names (QuestionRepository
-- .findOccupationQuestions), so a doctor's express quiz leans to medicine and biology. Tuning it
-- is an UPDATE; a category whose name matches nothing simply never comes up this way.
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Accountant', 'account|financ|econom|math', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Actor / Actress', 'film|movie|cinema|theat|art', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Architect', 'architect|build|art|design|history', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Artist / Painter', 'art|paint|museum|culture', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Astronomer', 'astronom|space|planet|physic|science', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Athlete', 'sport|football|olymp|game', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Biologist', 'biolog|nature|animal|plant|science', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Chef / Cook', 'food|cook|cuisine|drink', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Chemist', 'chemi|element|science', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Civil servant', 'politic|law|government|history', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Dentist', 'medic|health|anatom|biolog', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Designer', 'design|art|fashion', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Doctor / Physician', 'medic|health|anatom|biolog', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Driver', 'car|auto|transport|geograph', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Economist', 'econom|financ|business|math', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Electrician', 'electr|physic|technolog', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Engineer', 'engineer|technolog|physic|math', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Entrepreneur', 'business|econom|financ|brand', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Farmer', 'agricult|nature|plant|animal|food', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Firefighter', 'safety|chemi|health', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Geologist', 'geolog|geograph|nature|science', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Historian', 'histor|war|archaeolog|culture', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'IT specialist / Programmer', 'program|comput|technolog|internet|software', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Journalist', 'news|media|politic|literat', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Lawyer / Judge', 'law|legal|politic|history', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Librarian', 'literat|book|writer|history', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Manager', 'business|econom|management', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Marketing specialist', 'market|brand|business|media', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Mathematician', 'math|logic|science', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Mechanic', 'car|auto|engine|technolog', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Military', 'militar|war|history|weapon', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Musician', 'music|song|band|art', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Nurse', 'medic|health|anatom|biolog', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Pharmacist', 'pharma|medic|chemi|health', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Philosopher', 'philosoph|religio|literat|history', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Photographer', 'photo|art|film', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Physicist', 'physic|science|space|math', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Pilot', 'aviat|plane|geograph|transport', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Police officer', 'law|crime|safety', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Psychologist', 'psycholog|mind|health', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Retired', 'history|general|culture', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Sales representative', 'business|market|brand', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Scientist / Researcher', 'science|physic|chemi|biolog', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Student', 'general|science|history|math', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Teacher', 'general|literat|history|science', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Translator / Linguist', 'language|linguist|literat|word', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Travel / Tourism', 'geograph|countr|capital|travel|culture', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Unemployed', 'general', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Veterinarian', 'animal|biolog|medic|nature', 'ACTIVE', SYSDATE)
/execute/
INSERT INTO Q_OCCUPATION (ID, NAME, DOMAIN, STATUS, CREATED_DATE) VALUES (occupation_seq.NEXTVAL, 'Writer', 'literat|book|writer|language', 'ACTIVE', SYSDATE)
/execute/

-- Whether a player's express quizzes lean to their occupations; on unless they turn it off.
ALTER TABLE Q_USER ADD OCCUPATION_QUIZZES NUMERIC(1,0) DEFAULT 1 NOT NULL
/execute/

-- //@UNDO
ALTER TABLE Q_USER DROP COLUMN OCCUPATION_QUIZZES
/execute/

DELETE FROM Q_OCCUPATION WHERE STATUS = 'ACTIVE' AND CREATED_BY IS NULL
/execute/
