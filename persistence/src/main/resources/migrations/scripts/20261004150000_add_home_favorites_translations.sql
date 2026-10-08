-- // add_home_favorites_translations
-- The home page's grid of the player's own categories (favourites first, then the most played).
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1900, 'home_favorites', 'Favorite Categories', 'ACTIVE', SYSDATE, 'Favorite Categories', 'Избранные категории', 'Categorii preferate', 'Lieblingskategorien')
/execute/

-- //@UNDO
DELETE FROM Q_TRANSLATION WHERE ID = 1900
/execute/
