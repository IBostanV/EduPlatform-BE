-- // add_donate_qr_translations
-- The Donate page's PayPal QR code, and the chat composer's placeholder.
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1930, 'donate_paypal_scan', 'Scan the code with your phone to donate with PayPal.', NULL, 'ACTIVE', SYSDATE, 'Scan the code with your phone to donate with PayPal.', 'Отсканируй код телефоном, чтобы поддержать через PayPal.', 'Scanează codul cu telefonul pentru a dona cu PayPal.', 'Scanne den Code mit deinem Handy, um mit PayPal zu spenden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1931, 'donate_paypal_qr', 'PayPal donation QR code', NULL, 'ACTIVE', SYSDATE, 'PayPal donation QR code', 'QR-код для пожертвования через PayPal', 'Cod QR pentru donații PayPal', 'QR-Code für PayPal-Spenden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1932, 'write_message', 'Write a message…', NULL, 'ACTIVE', SYSDATE, 'Write a message…', 'Напиши сообщение…', 'Scrie un mesaj…', 'Schreib eine Nachricht…')
/execute/

-- //@UNDO
DELETE FROM Q_TRANSLATION WHERE ID BETWEEN 1930 AND 1932
/execute/
