-- // add_missing_ui_translations
-- Every text the site shows through t(key, default) had only its English default: 27 keys were
-- in this table. These are the rest, in Russian, Romanian and German, generated from a scan of the
-- front end's t() calls. KEY grows to fit the longer keys, and the texts are counted in characters,
-- not bytes, so a Cyrillic or diacritic text gets its full length.
ALTER TABLE Q_TRANSLATION MODIFY ("KEY" VARCHAR2(100), DEFAULT_VALUE VARCHAR2(1000 CHAR), EN VARCHAR2(1000 CHAR),
    RU VARCHAR2(1000 CHAR), RO VARCHAR2(1000 CHAR), DE VARCHAR2(1000 CHAR))
/execute/

INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1000, 'DRAG_AND_DROP', 'Drag and drop', 'ACTIVE', SYSDATE, 'Drag and drop', 'Перетаскивание', 'Trage și plasează', 'Drag & Drop')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1001, 'INPUT', 'Input', 'ACTIVE', SYSDATE, 'Input', 'Ввод ответа', 'Răspuns scris', 'Eingabe')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1002, 'IN_ORDER', 'In order', 'ACTIVE', SYSDATE, 'In order', 'По порядку', 'În ordine', 'In Reihenfolge')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1003, 'MAP_CHOICE', 'Map choice', 'ACTIVE', SYSDATE, 'Map choice', 'Выбор на карте', 'Alegere pe hartă', 'Kartenauswahl')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1004, 'MULTIPLE_CHOICE', 'Multiple choice', 'ACTIVE', SYSDATE, 'Multiple choice', 'Несколько вариантов', 'Alegere multiplă', 'Mehrfachauswahl')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1005, 'ONE_FROM_TWO', 'One from two', 'ACTIVE', SYSDATE, 'One from two', 'Один из двух', 'Unul din două', 'Eins aus zwei')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1006, 'SINGLE_CHOICE', 'Single choice', 'ACTIVE', SYSDATE, 'Single choice', 'Один вариант', 'Alegere unică', 'Einfachauswahl')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1007, 'VALUES_RANGE', 'Values range', 'ACTIVE', SYSDATE, 'Values range', 'Диапазон значений', 'Interval de valori', 'Wertebereich')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1008, 'accent_amber', 'Amber', 'ACTIVE', SYSDATE, 'Amber', 'Янтарный', 'Chihlimbar', 'Bernstein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1009, 'accent_colour', 'Accent colour', 'ACTIVE', SYSDATE, 'Accent colour', 'Акцентный цвет', 'Culoare de accent', 'Akzentfarbe')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1010, 'accent_colour_hint', 'The colour of buttons, links, highlights and glows.', 'ACTIVE', SYSDATE, 'The colour of buttons, links, highlights and glows.', 'Цвет кнопок, ссылок, выделений и подсветки.', 'Culoarea butoanelor, linkurilor, evidențierilor și strălucirilor.', 'Die Farbe von Buttons, Links, Hervorhebungen und Leuchteffekten.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1011, 'accent_custom', 'Pick your own', 'ACTIVE', SYSDATE, 'Pick your own', 'Свой цвет', 'Alege-o singur', 'Eigene wählen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1012, 'accent_default', 'Cyan (default)', 'ACTIVE', SYSDATE, 'Cyan (default)', 'Голубой (по умолчанию)', 'Cyan (implicit)', 'Cyan (Standard)')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1013, 'accent_emerald', 'Emerald', 'ACTIVE', SYSDATE, 'Emerald', 'Изумрудный', 'Smarald', 'Smaragd')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1014, 'accent_lime', 'Lime', 'ACTIVE', SYSDATE, 'Lime', 'Лаймовый', 'Lime', 'Limette')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1015, 'accent_rose', 'Rose', 'ACTIVE', SYSDATE, 'Rose', 'Розовый', 'Roz', 'Rosé')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1016, 'accent_text', 'Text on the accent', 'ACTIVE', SYSDATE, 'Text on the accent', 'Текст на акцентном цвете', 'Text pe culoarea de accent', 'Text auf der Akzentfarbe')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1017, 'accent_text_auto', 'Automatic (default)', 'ACTIVE', SYSDATE, 'Automatic (default)', 'Автоматически (по умолчанию)', 'Automat (implicit)', 'Automatisch (Standard)')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1018, 'accent_text_black', 'Black', 'ACTIVE', SYSDATE, 'Black', 'Чёрный', 'Negru', 'Schwarz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1019, 'accent_text_custom', 'Pick your own', 'ACTIVE', SYSDATE, 'Pick your own', 'Свой цвет', 'Alege-o singur', 'Eigene wählen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1020, 'accent_text_dark', 'Dark', 'ACTIVE', SYSDATE, 'Dark', 'Тёмный', 'Închis', 'Dunkel')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1021, 'accent_text_hint', 'The colour of the words on buttons and highlights. Pick one that reads on your accent.', 'ACTIVE', SYSDATE, 'The colour of the words on buttons and highlights. Pick one that reads on your accent.', 'Цвет надписей на кнопках и выделениях. Выбери тот, что хорошо читается на твоём акцентном цвете.', 'Culoarea textului de pe butoane și evidențieri. Alege una care se citește bine pe culoarea ta de accent.', 'Die Farbe der Schrift auf Buttons und Hervorhebungen. Wähl eine, die auf deiner Akzentfarbe gut lesbar ist.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1022, 'accent_text_white', 'White', 'ACTIVE', SYSDATE, 'White', 'Белый', 'Alb', 'Weiß')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1023, 'accent_violet', 'Violet', 'ACTIVE', SYSDATE, 'Violet', 'Фиолетовый', 'Violet', 'Violett')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1024, 'accepted_answer_number', 'Accepted answer {{number}}', 'ACTIVE', SYSDATE, 'Accepted answer {{number}}', 'Принимаемый ответ {{number}}', 'Răspuns acceptat {{number}}', 'Akzeptierte Antwort {{number}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1025, 'accepted_answers', 'Accepted answers', 'ACTIVE', SYSDATE, 'Accepted answers', 'Принимаемые ответы', 'Răspunsuri acceptate', 'Akzeptierte Antworten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1026, 'account_created', 'Your account has been successfully created.', 'ACTIVE', SYSDATE, 'Your account has been successfully created.', 'Твой аккаунт успешно создан.', 'Contul tău a fost creat cu succes.', 'Dein Konto wurde erfolgreich erstellt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1027, 'accuracy', 'Accuracy', 'ACTIVE', SYSDATE, 'Accuracy', 'Точность', 'Precizie', 'Genauigkeit')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1028, 'add_accepted_answer', 'Add another accepted spelling', 'ACTIVE', SYSDATE, 'Add another accepted spelling', 'Добавить ещё один вариант написания', 'Adaugă încă o variantă de scriere acceptată', 'Weitere akzeptierte Schreibweise hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1029, 'add_answer', 'Add another right answer', 'ACTIVE', SYSDATE, 'Add another right answer', 'Добавить ещё один правильный ответ', 'Adaugă încă un răspuns corect', 'Weitere richtige Antwort hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1030, 'add_friend', 'Add friend', 'ACTIVE', SYSDATE, 'Add friend', 'Добавить в друзья', 'Adaugă prieten', 'Freund hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1031, 'add_item', 'Add another item', 'ACTIVE', SYSDATE, 'Add another item', 'Добавить ещё один элемент', 'Adaugă încă un element', 'Weiteres Element hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1032, 'add_members', 'Add members', 'ACTIVE', SYSDATE, 'Add members', 'Добавить участников', 'Adaugă membri', 'Mitglieder hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1033, 'add_question', 'Add question', 'ACTIVE', SYSDATE, 'Add question', 'Добавить вопрос', 'Adaugă întrebare', 'Frage hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1034, 'add_screenshot', 'Add a screenshot (optional)', 'ACTIVE', SYSDATE, 'Add a screenshot (optional)', 'Добавить скриншот (необязательно)', 'Adaugă o captură de ecran (opțional)', 'Screenshot hinzufügen (optional)')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1035, 'add_wrong_option', 'Add another wrong option', 'ACTIVE', SYSDATE, 'Add another wrong option', 'Добавить ещё один неверный вариант', 'Adaugă încă o variantă greșită', 'Weitere falsche Option hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1036, 'admin_a_player', 'a player', 'ACTIVE', SYSDATE, 'a player', 'игрок', 'un jucător', 'einem Spieler')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1037, 'admin_access', 'Access', 'ACTIVE', SYSDATE, 'Access', 'Доступ', 'Acces', 'Zugriff')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1038, 'admin_access_hint', 'Every account can play; these open the dashboards on top of that.', 'ACTIVE', SYSDATE, 'Every account can play; these open the dashboards on top of that.', 'Играть может любой аккаунт; эти роли дополнительно открывают панели управления.', 'Orice cont poate juca; acestea deschid în plus panourile de administrare.', 'Jedes Konto kann spielen; diese Rollen öffnen zusätzlich die Dashboards.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1039, 'admin_accounts', '{{count}} accounts', 'ACTIVE', SYSDATE, '{{count}} accounts', 'Аккаунтов: {{count}}', '{{count}} conturi', '{{count}} Konten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1040, 'admin_accounts_one', '{{count}} account', 'ACTIVE', SYSDATE, '{{count}} account', '{{count}} аккаунт', '{{count}} cont', '{{count}} Konto')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1041, 'admin_add_user', 'Add user', 'ACTIVE', SYSDATE, 'Add user', 'Добавить пользователя', 'Adaugă utilizator', 'Benutzer hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1042, 'admin_add_wallet', 'Add wallet', 'ACTIVE', SYSDATE, 'Add wallet', 'Добавить кошелёк', 'Adaugă portofel', 'Wallet hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1043, 'admin_admins_count', '{{count}} admin', 'ACTIVE', SYSDATE, '{{count}} admin', 'Администраторов: {{count}}', 'Administratori: {{count}}', 'Admins: {{count}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1044, 'admin_block', 'Block', 'ACTIVE', SYSDATE, 'Block', 'Заблокировать', 'Blochează', 'Sperren')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1045, 'admin_blocked_count', '{{count}} blocked', 'ACTIVE', SYSDATE, '{{count}} blocked', 'Заблокировано: {{count}}', 'Blocați: {{count}}', 'Gesperrt: {{count}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1046, 'admin_clear_all', 'Clear all', 'ACTIVE', SYSDATE, 'Clear all', 'Очистить всё', 'Șterge tot', 'Alle löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1047, 'admin_clear_errors_message', 'All {{count}} stored errors will be deleted. This can''t be undone.', 'ACTIVE', SYSDATE, 'All {{count}} stored errors will be deleted. This can''t be undone.', 'Все сохранённые ошибки ({{count}}) будут удалены. Это действие нельзя отменить.', 'Toate cele {{count}} erori salvate vor fi șterse. Acțiunea nu poate fi anulată.', 'Alle {{count}} gespeicherten Fehler werden gelöscht. Dies kann nicht rückgängig gemacht werden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1048, 'admin_clear_errors_title', 'Clear all errors?', 'ACTIVE', SYSDATE, 'Clear all errors?', 'Удалить все ошибки?', 'Ștergi toate erorile?', 'Alle Fehler löschen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1049, 'admin_col_actions', 'Actions', 'ACTIVE', SYSDATE, 'Actions', 'Действия', 'Acțiuni', 'Aktionen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1050, 'admin_col_from', 'From', 'ACTIVE', SYSDATE, 'From', 'От кого', 'De la', 'Von')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1051, 'admin_col_invited', 'Invited', 'ACTIVE', SYSDATE, 'Invited', 'Приглашены', 'Invitați', 'Eingeladen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1052, 'admin_col_kind', 'Kind', 'ACTIVE', SYSDATE, 'Kind', 'Вид', 'Tip', 'Art')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1053, 'admin_col_made', 'Made', 'ACTIVE', SYSDATE, 'Made', 'Создана', 'Creat', 'Erstellt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1054, 'admin_col_made_by', 'Made by', 'ACTIVE', SYSDATE, 'Made by', 'Автор', 'Creat de', 'Erstellt von')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1055, 'admin_col_message', 'Message', 'ACTIVE', SYSDATE, 'Message', 'Сообщение', 'Mesaj', 'Nachricht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1056, 'admin_col_page', 'Page', 'ACTIVE', SYSDATE, 'Page', 'Страница', 'Pagină', 'Seite')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1057, 'admin_col_questions', 'Questions', 'ACTIVE', SYSDATE, 'Questions', 'Вопросы', 'Întrebări', 'Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1058, 'admin_col_registered', 'Registered', 'ACTIVE', SYSDATE, 'Registered', 'Регистрация', 'Înregistrat', 'Registriert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1059, 'admin_col_role', 'Role', 'ACTIVE', SYSDATE, 'Role', 'Роль', 'Rol', 'Rolle')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1060, 'admin_col_seconds_each', 'Seconds each', 'ACTIVE', SYSDATE, 'Seconds each', 'Секунд на вопрос', 'Secunde per întrebare', 'Sekunden pro Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1061, 'admin_col_sent', 'Sent', 'ACTIVE', SYSDATE, 'Sent', 'Отправлено', 'Trimis', 'Gesendet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1062, 'admin_col_status', 'Status', 'ACTIVE', SYSDATE, 'Status', 'Статус', 'Stare', 'Status')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1063, 'admin_col_type', 'Type', 'ACTIVE', SYSDATE, 'Type', 'Тип', 'Tip', 'Typ')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1064, 'admin_count_open', '{{count}} open', 'ACTIVE', SYSDATE, '{{count}} open', 'Открытых: {{count}}', 'Deschise: {{count}}', 'Offen: {{count}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1065, 'admin_create_user', 'Create user', 'ACTIVE', SYSDATE, 'Create user', 'Создать пользователя', 'Creează utilizator', 'Benutzer erstellen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1066, 'admin_crypto_wallets', 'Crypto wallets', 'ACTIVE', SYSDATE, 'Crypto wallets', 'Криптокошельки', 'Portofele crypto', 'Krypto-Wallets')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1067, 'admin_custom_quizzes', 'Custom quizzes', 'ACTIVE', SYSDATE, 'Custom quizzes', 'Пользовательские викторины', 'Quizuri personalizate', 'Eigene Quizze')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1068, 'admin_dashboard_feedback', '{{name}}, {{count}} open feedback', 'ACTIVE', SYSDATE, '{{name}}, {{count}} open feedback', '{{name}}, открытых отзывов: {{count}}', '{{name}}, feedback deschis: {{count}}', '{{name}}, offenes Feedback: {{count}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1069, 'admin_delete_quiz_message', 'The quiz by {{name}} will be deleted, with its questions, its invitations and everyone’s results for it. This can’t be undone.', 'ACTIVE', SYSDATE, 'The quiz by {{name}} will be deleted, with its questions, its invitations and everyone’s results for it. This can’t be undone.', 'Викторина пользователя {{name}} будет удалена вместе с вопросами, приглашениями и результатами всех участников. Это действие нельзя отменить.', 'Quizul creat de {{name}} va fi șters, împreună cu întrebările, invitațiile și rezultatele tuturor jucătorilor. Acțiunea nu poate fi anulată.', 'Das Quiz von {{name}} wird gelöscht, samt Fragen, Einladungen und allen Ergebnissen dazu. Dies kann nicht rückgängig gemacht werden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1070, 'admin_delete_quiz_title', 'Delete this quiz?', 'ACTIVE', SYSDATE, 'Delete this quiz?', 'Удалить эту викторину?', 'Ștergi acest quiz?', 'Dieses Quiz löschen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1071, 'admin_delete_user_message', 'and everything tied to the account will be permanently deleted. This can’t be undone — block the account instead to only keep them out.', 'ACTIVE', SYSDATE, 'and everything tied to the account will be permanently deleted. This can’t be undone — block the account instead to only keep them out.', 'и всё, что связано с аккаунтом, будет удалено безвозвратно. Это действие нельзя отменить — чтобы просто закрыть доступ, заблокируйте аккаунт.', 'și tot ce ține de cont vor fi șterse definitiv. Acțiunea nu poate fi anulată — pentru a restricționa doar accesul, blochează contul.', 'und alles, was mit dem Konto verbunden ist, werden endgültig gelöscht. Dies kann nicht rückgängig gemacht werden — um nur den Zugang zu verwehren, stattdessen das Konto sperren.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1072, 'admin_delete_user_title', 'Delete user?', 'ACTIVE', SYSDATE, 'Delete user?', 'Удалить пользователя?', 'Ștergi utilizatorul?', 'Benutzer löschen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1073, 'admin_display_name', 'Display name', 'ACTIVE', SYSDATE, 'Display name', 'Отображаемое имя', 'Nume afișat', 'Anzeigename')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1074, 'admin_display_name_hint', 'What other players see. Left empty, the account is known by its email.', 'ACTIVE', SYSDATE, 'What other players see. Left empty, the account is known by its email.', 'То, что видят другие игроки. Если оставить пустым, аккаунт будет отображаться по email.', 'Ce văd ceilalți jucători. Dacă rămâne gol, contul este afișat după email.', 'Was andere Spieler sehen. Bleibt es leer, wird das Konto mit seiner E-Mail angezeigt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1075, 'admin_donation_saved', 'Donation details saved', 'ACTIVE', SYSDATE, 'Donation details saved', 'Данные для пожертвований сохранены', 'Detaliile pentru donații au fost salvate', 'Spendendaten gespeichert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1076, 'admin_donations', 'Donations', 'ACTIVE', SYSDATE, 'Donations', 'Пожертвования', 'Donații', 'Spenden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1077, 'admin_donations_intro_after', 'page, linked from the footer. Leave a field empty to leave it off the page.', 'ACTIVE', SYSDATE, 'page, linked from the footer. Leave a field empty to leave it off the page.', ', ссылка на которую есть в подвале. Оставьте поле пустым, чтобы не показывать его на странице.', ', la care duce un link din subsol. Lasă un câmp gol pentru a nu-l afișa pe pagină.', ' angezeigt, verlinkt in der Fußzeile. Ein Feld leer lassen, um es auf der Seite auszublenden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1078, 'admin_donations_intro_before', 'Shown to everyone on the', 'ACTIVE', SYSDATE, 'Shown to everyone on the', 'Показывается всем на странице', 'Afișat tuturor pe pagina', 'Wird allen auf der Seite')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1079, 'admin_donations_intro_link', 'Donate', 'ACTIVE', SYSDATE, 'Donate', '«Поддержать»', 'Donează', 'Spenden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1080, 'admin_edit_user', 'Edit user', 'ACTIVE', SYSDATE, 'Edit user', 'Редактировать пользователя', 'Editează utilizatorul', 'Benutzer bearbeiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1081, 'admin_email', 'Email', 'ACTIVE', SYSDATE, 'Email', 'Email', 'Email', 'E-Mail')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1082, 'admin_email_hint', 'The owner''s sign-in name; it cannot be changed here.', 'ACTIVE', SYSDATE, 'The owner''s sign-in name; it cannot be changed here.', 'Логин владельца; здесь его изменить нельзя.', 'Numele de autentificare al proprietarului; nu poate fi modificat aici.', 'Der Anmeldename des Inhabers; kann hier nicht geändert werden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1083, 'admin_error_details', 'Details', 'ACTIVE', SYSDATE, 'Details', 'Подробности', 'Detalii', 'Details')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1084, 'admin_errors', 'Errors', 'ACTIVE', SYSDATE, 'Errors', 'Ошибки', 'Erori', 'Fehler')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1085, 'admin_errors_newest_shown', 'the newest {{count}} shown', 'ACTIVE', SYSDATE, 'the newest {{count}} shown', 'показаны последние {{count}}', 'sunt afișate cele mai noi {{count}}', 'die neuesten {{count}} angezeigt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1086, 'admin_feedback', 'Feedback', 'ACTIVE', SYSDATE, 'Feedback', 'Отзывы', 'Feedback', 'Feedback')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1087, 'admin_feedback_type_bug', 'Bug', 'ACTIVE', SYSDATE, 'Bug', 'Ошибка', 'Eroare', 'Fehler')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1088, 'admin_feedback_type_other', 'Other', 'ACTIVE', SYSDATE, 'Other', 'Другое', 'Altele', 'Sonstiges')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1089, 'admin_feedback_type_question', 'Question', 'ACTIVE', SYSDATE, 'Question', 'Вопрос', 'Întrebare', 'Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1090, 'admin_feedback_type_suggestion', 'Suggestion', 'ACTIVE', SYSDATE, 'Suggestion', 'Предложение', 'Sugestie', 'Vorschlag')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1091, 'admin_give_email', 'Give an email address', 'ACTIVE', SYSDATE, 'Give an email address', 'Укажите адрес email', 'Introdu o adresă de email', 'E-Mail-Adresse angeben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1092, 'admin_guest', 'Guest', 'ACTIVE', SYSDATE, 'Guest', 'Гость', 'Vizitator', 'Gast')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1093, 'admin_in_total', '{{count}} in total', 'ACTIVE', SYSDATE, '{{count}} in total', 'всего: {{count}}', '{{count}} în total', '{{count}} insgesamt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1094, 'admin_new_user', 'New user', 'ACTIVE', SYSDATE, 'New user', 'Новый пользователь', 'Utilizator nou', 'Neuer Benutzer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1095, 'admin_no_custom_quizzes', 'No custom quizzes yet.', 'ACTIVE', SYSDATE, 'No custom quizzes yet.', 'Пользовательских викторин пока нет.', 'Încă nu există quizuri personalizate.', 'Noch keine eigenen Quizze.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1096, 'admin_no_errors', 'No errors reported.', 'ACTIVE', SYSDATE, 'No errors reported.', 'Ошибок не зарегистрировано.', 'Nicio eroare raportată.', 'Keine Fehler gemeldet.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1097, 'admin_no_feedback', 'No feedback yet.', 'ACTIVE', SYSDATE, 'No feedback yet.', 'Отзывов пока нет.', 'Încă nu există feedback.', 'Noch kein Feedback.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1098, 'admin_nothing_open', 'Nothing open', 'ACTIVE', SYSDATE, 'Nothing open', 'Открытых нет', 'Nimic deschis', 'Nichts offen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1099, 'admin_open', 'Open', 'ACTIVE', SYSDATE, 'Open', 'Открыть', 'Deschide', 'Öffnen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1100, 'admin_password', 'Password', 'ACTIVE', SYSDATE, 'Password', 'Пароль', 'Parolă', 'Passwort')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1101, 'admin_password_hint', 'The first password for the account; its owner can change it afterwards.', 'ACTIVE', SYSDATE, 'The first password for the account; its owner can change it afterwards.', 'Начальный пароль аккаунта; владелец сможет изменить его позже.', 'Prima parolă a contului; proprietarul o poate schimba ulterior.', 'Das erste Passwort des Kontos; der Inhaber kann es später ändern.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1102, 'admin_password_min', 'At least 8 characters', 'ACTIVE', SYSDATE, 'At least 8 characters', 'Не менее 8 символов', 'Cel puțin 8 caractere', 'Mindestens 8 Zeichen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1103, 'admin_paypal_email', 'PayPal email', 'ACTIVE', SYSDATE, 'PayPal email', 'Email PayPal', 'Email PayPal', 'PayPal-E-Mail')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1104, 'admin_paypal_email_hint', 'Shown for sending money by address.', 'ACTIVE', SYSDATE, 'Shown for sending money by address.', 'Показывается для перевода денег по адресу.', 'Afișat pentru trimiterea banilor pe adresă.', 'Wird für Zahlungen per Adresse angezeigt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1105, 'admin_paypal_link', 'PayPal link', 'ACTIVE', SYSDATE, 'PayPal link', 'Ссылка PayPal', 'Link PayPal', 'PayPal-Link')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1106, 'admin_paypal_link_hint', 'A paypal.me link or a PayPal donate button link; it must start with https://', 'ACTIVE', SYSDATE, 'A paypal.me link or a PayPal donate button link; it must start with https://', 'Ссылка paypal.me или ссылка кнопки пожертвования PayPal; должна начинаться с https://', 'Un link paypal.me sau linkul unui buton de donație PayPal; trebuie să înceapă cu https://', 'Ein paypal.me-Link oder der Link eines PayPal-Spendenbuttons; muss mit https:// beginnen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1107, 'admin_quizzes_made', '{{count}} quizzes made by players', 'ACTIVE', SYSDATE, '{{count}} quizzes made by players', 'Викторин, созданных игроками: {{count}}', '{{count}} quizuri create de jucători', '{{count}} von Spielern erstellte Quizze')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1108, 'admin_quizzes_made_one', '{{count}} quiz made by players', 'ACTIVE', SYSDATE, '{{count}} quiz made by players', '{{count}} викторина, созданная игроками', '{{count}} quiz creat de jucători', '{{count}} von Spielern erstelltes Quiz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1109, 'admin_reopen', 'Reopen', 'ACTIVE', SYSDATE, 'Reopen', 'Открыть снова', 'Redeschide', 'Wieder öffnen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1110, 'admin_resolve', 'Resolve', 'ACTIVE', SYSDATE, 'Resolve', 'Отметить решённым', 'Rezolvă', 'Erledigen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1111, 'admin_role_admin', 'Administrator', 'ACTIVE', SYSDATE, 'Administrator', 'Администратор', 'Administrator', 'Administrator')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1112, 'admin_role_admin_hint', 'The admin dashboard: custom quizzes, feedback and these accounts.', 'ACTIVE', SYSDATE, 'The admin dashboard: custom quizzes, feedback and these accounts.', 'Панель администратора: пользовательские викторины, отзывы и эти аккаунты.', 'Panoul de administrare: quizuri personalizate, feedback și aceste conturi.', 'Das Admin-Dashboard: eigene Quizze, Feedback und diese Konten.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1113, 'admin_role_content_editor', 'Content editor', 'ACTIVE', SYSDATE, 'Content editor', 'Редактор контента', 'Editor de conținut', 'Content-Editor')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1114, 'admin_role_content_editor_hint', 'The content dashboard: categories, glossaries, questions, knowledge base.', 'ACTIVE', SYSDATE, 'The content dashboard: categories, glossaries, questions, knowledge base.', 'Панель контента: категории, глоссарии, вопросы, база знаний.', 'Panoul de conținut: categorii, glosare, întrebări, bază de cunoștințe.', 'Das Content-Dashboard: Kategorien, Glossare, Fragen, Wissensdatenbank.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1115, 'admin_role_content_publisher', 'Content publisher', 'ACTIVE', SYSDATE, 'Content publisher', 'Публикатор контента', 'Publicator de conținut', 'Content-Publisher')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1116, 'admin_role_content_publisher_hint', 'The content dashboard as well; nothing tells it apart from an editor yet.', 'ACTIVE', SYSDATE, 'The content dashboard as well; nothing tells it apart from an editor yet.', 'Также панель контента; пока ничем не отличается от редактора.', 'Tot panoul de conținut; deocamdată nu diferă cu nimic de editor.', 'Ebenfalls das Content-Dashboard; bisher kein Unterschied zum Editor.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1117, 'admin_role_player', 'Player', 'ACTIVE', SYSDATE, 'Player', 'Игрок', 'Jucător', 'Spieler')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1118, 'admin_save_changes', 'Save changes', 'ACTIVE', SYSDATE, 'Save changes', 'Сохранить изменения', 'Salvează modificările', 'Änderungen speichern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1119, 'admin_save_donation_details', 'Save donation details', 'ACTIVE', SYSDATE, 'Save donation details', 'Сохранить данные для пожертвований', 'Salvează detaliile pentru donații', 'Spendendaten speichern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1120, 'admin_screenshot', 'Screenshot', 'ACTIVE', SYSDATE, 'Screenshot', 'Скриншот', 'Captură de ecran', 'Screenshot')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1121, 'admin_screenshot_alt', 'The screenshot sent with this message', 'ACTIVE', SYSDATE, 'The screenshot sent with this message', 'Скриншот, отправленный с этим сообщением', 'Captura de ecran trimisă cu acest mesaj', 'Der mit dieser Nachricht gesendete Screenshot')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1122, 'admin_search_users', 'Search users', 'ACTIVE', SYSDATE, 'Search users', 'Поиск пользователей', 'Caută utilizatori', 'Benutzer suchen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1123, 'admin_search_users_placeholder', 'Search by email, name or role…', 'ACTIVE', SYSDATE, 'Search by email, name or role…', 'Поиск по email, имени или роли…', 'Caută după email, nume sau rol…', 'Nach E-Mail, Name oder Rolle suchen…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1124, 'admin_this_error', 'this error', 'ACTIVE', SYSDATE, 'this error', 'эту ошибку', 'această eroare', 'diesen Fehler')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1125, 'admin_this_quiz_by', 'this quiz by {{name}}', 'ACTIVE', SYSDATE, 'this quiz by {{name}}', 'эту викторину от {{name}}', 'acest quiz creat de {{name}}', 'dieses Quiz von {{name}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1126, 'admin_unblock', 'Unblock', 'ACTIVE', SYSDATE, 'Unblock', 'Разблокировать', 'Deblochează', 'Entsperren')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1127, 'admin_user_created', 'User created', 'ACTIVE', SYSDATE, 'User created', 'Пользователь создан', 'Utilizator creat', 'Benutzer erstellt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1128, 'admin_user_deleted', 'User deleted', 'ACTIVE', SYSDATE, 'User deleted', 'Пользователь удалён', 'Utilizator șters', 'Benutzer gelöscht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1129, 'admin_user_updated', 'User updated', 'ACTIVE', SYSDATE, 'User updated', 'Пользователь обновлён', 'Utilizator actualizat', 'Benutzer aktualisiert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1130, 'admin_users', 'Users', 'ACTIVE', SYSDATE, 'Users', 'Пользователи', 'Utilizatori', 'Benutzer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1131, 'admin_users_pages', 'Users pages', 'ACTIVE', SYSDATE, 'Users pages', 'Страницы пользователей', 'Paginile de utilizatori', 'Benutzerseiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1132, 'admin_users_what', 'users', 'ACTIVE', SYSDATE, 'users', 'пользователей', 'utilizatori', 'Benutzer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1133, 'admin_wallet', 'wallet', 'ACTIVE', SYSDATE, 'wallet', 'кошелёк', 'portofel', 'Wallet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1134, 'admin_wallet_address', 'Address', 'ACTIVE', SYSDATE, 'Address', 'Адрес', 'Adresă', 'Adresse')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1135, 'admin_wallet_coin', 'Coin', 'ACTIVE', SYSDATE, 'Coin', 'Монета', 'Monedă', 'Coin')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1136, 'admin_wallet_n', 'Wallet {{number}}', 'ACTIVE', SYSDATE, 'Wallet {{number}}', 'Кошелёк {{number}}', 'Portofel {{number}}', 'Wallet {{number}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1137, 'admin_wallet_network', 'Network', 'ACTIVE', SYSDATE, 'Network', 'Сеть', 'Rețea', 'Netzwerk')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1138, 'admin_wallet_remove_row', 'Remove the {{name}} row', 'ACTIVE', SYSDATE, 'Remove the {{name}} row', 'Удалить строку «{{name}}»', 'Elimină rândul {{name}}', 'Zeile {{name}} entfernen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1139, 'admin_wallet_symbol', 'Symbol', 'ACTIVE', SYSDATE, 'Symbol', 'Символ', 'Simbol', 'Symbol')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1140, 'all', 'All', 'ACTIVE', SYSDATE, 'All', 'Все', 'Toate', 'Alle')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1141, 'all_time', 'All time', 'ACTIVE', SYSDATE, 'All time', 'За всё время', 'Dintotdeauna', 'Gesamt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1142, 'all_trophies', 'All trophies', 'ACTIVE', SYSDATE, 'All trophies', 'Все трофеи', 'Toate trofeele', 'Alle Trophäen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1143, 'all_trophies_earned', 'Every trophy in sight is yours.', 'ACTIVE', SYSDATE, 'Every trophy in sight is yours.', 'Все доступные трофеи уже твои.', 'Toate trofeele la vedere sunt ale tale.', 'Alle Trophäen in Sicht gehören dir.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1144, 'answer_placeholder', 'e.g. Chisinau', 'ACTIVE', SYSDATE, 'e.g. Chisinau', 'напр. Кишинёв', 'ex. Chișinău', 'z. B. Chișinău')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1145, 'any_difficulty', 'Any', 'ACTIVE', SYSDATE, 'Any', 'Любая', 'Oricare', 'Beliebig')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1146, 'appearance', 'Appearance', 'ACTIVE', SYSDATE, 'Appearance', 'Оформление', 'Aspect', 'Darstellung')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1147, 'appearance_lead', 'Make the site yours. Changes show at once and are only for you.', 'ACTIVE', SYSDATE, 'Make the site yours. Changes show at once and are only for you.', 'Настрой сайт под себя. Изменения применяются сразу и видны только тебе.', 'Fă site-ul pe gustul tău. Modificările apar imediat și sunt doar pentru tine.', 'Gestalte die Seite nach deinem Geschmack. Änderungen wirken sofort und gelten nur für dich.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1148, 'appearance_sign_in', 'Sign in to change how the site looks for you.', 'ACTIVE', SYSDATE, 'Sign in to change how the site looks for you.', 'Войди, чтобы изменить внешний вид сайта под себя.', 'Autentifică-te ca să schimbi cum arată site-ul pentru tine.', 'Melde dich an, um das Aussehen der Seite für dich anzupassen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1149, 'back_to_map', 'Back to map', 'ACTIVE', SYSDATE, 'Back to map', 'Назад к карте', 'Înapoi la hartă', 'Zurück zur Karte')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1150, 'back_to_top', 'Back to top', 'ACTIVE', SYSDATE, 'Back to top', 'Наверх', 'Înapoi sus', 'Nach oben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1151, 'best_score', 'Best score', 'ACTIVE', SYSDATE, 'Best score', 'Лучший результат', 'Cel mai bun scor', 'Bestes Ergebnis')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1152, 'breadcrumbs', 'Breadcrumbs', 'ACTIVE', SYSDATE, 'Breadcrumbs', 'Навигационная цепочка', 'Fir de navigare', 'Brotkrümelnavigation')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1153, 'cancel', 'Cancel', 'ACTIVE', SYSDATE, 'Cancel', 'Отмена', 'Anulează', 'Abbrechen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1154, 'categories', 'Categories', 'ACTIVE', SYSDATE, 'Categories', 'Категории', 'Categorii', 'Kategorien')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1155, 'challenge_accept', 'Take it on', 'ACTIVE', SYSDATE, 'Take it on', 'Принять вызов', 'Acceptă provocarea', 'Annehmen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1156, 'challenge_friends', 'Challenge friends', 'ACTIVE', SYSDATE, 'Challenge friends', 'Бросить вызов друзьям', 'Provoacă-ți prietenii', 'Freunde herausfordern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1157, 'challenge_friends_hint', 'They get the same questions. Whoever gets more right wins; the faster one if it is a tie.', 'ACTIVE', SYSDATE, 'They get the same questions. Whoever gets more right wins; the faster one if it is a tie.', 'Они получат те же вопросы. Побеждает тот, у кого больше правильных ответов, а при ничьей — кто быстрее.', 'Primesc aceleași întrebări. Câștigă cine are mai multe răspunsuri corecte; la egalitate, cel mai rapid.', 'Sie bekommen dieselben Fragen. Wer mehr richtig hat, gewinnt; bei Gleichstand der Schnellere.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1158, 'challenge_from', 'Challenge from {{name}}', 'ACTIVE', SYSDATE, 'Challenge from {{name}}', 'Вызов от {{name}}', 'Provocare de la {{name}}', 'Herausforderung von {{name}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1159, 'challenge_no_friends', 'Add some friends first, from the friends panel.', 'ACTIVE', SYSDATE, 'Add some friends first, from the friends panel.', 'Сначала добавь друзей на панели друзей.', 'Adaugă mai întâi câțiva prieteni, din panoul de prieteni.', 'Füg zuerst ein paar Freunde über das Freunde-Panel hinzu.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1160, 'challenge_send', 'Send challenge', 'ACTIVE', SYSDATE, 'Send challenge', 'Отправить вызов', 'Trimite provocarea', 'Herausforderung senden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1161, 'challenge_sent', 'Challenge sent', 'ACTIVE', SYSDATE, 'Challenge sent', 'Вызов отправлен', 'Provocare trimisă', 'Herausforderung gesendet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1162, 'challenges', 'Challenges', 'ACTIVE', SYSDATE, 'Challenges', 'Вызовы', 'Provocări', 'Herausforderungen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1163, 'challenges_none', 'No challenges in the last month yet.', 'ACTIVE', SYSDATE, 'No challenges in the last month yet.', 'За последний месяц вызовов пока нет.', 'Încă nicio provocare în ultima lună.', 'Im letzten Monat gab es noch keine Herausforderungen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1164, 'challenges_received', 'For you', 'ACTIVE', SYSDATE, 'For you', 'Тебе', 'Pentru tine', 'Für dich')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1165, 'challenges_sent', 'Sent by you', 'ACTIVE', SYSDATE, 'Sent by you', 'Отправленные тобой', 'Trimise de tine', 'Von dir gesendet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1166, 'challenges_sub', 'Send one from any quiz result: your friend gets the same questions.', 'ACTIVE', SYSDATE, 'Send one from any quiz result: your friend gets the same questions.', 'Отправь вызов с экрана результатов любой викторины: твой друг получит те же вопросы.', 'Trimite una din rezultatul oricărui quiz: prietenul tău primește aceleași întrebări.', 'Sende eine aus jedem Quiz-Ergebnis: Dein Freund bekommt dieselben Fragen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1167, 'change_password_hint', 'You''ll be signed out and log in again with the new password.', 'ACTIVE', SYSDATE, 'You''ll be signed out and log in again with the new password.', 'Ты выйдешь из аккаунта и снова войдёшь с новым паролем.', 'Vei fi deconectat și te vei autentifica din nou cu parola nouă.', 'Du wirst abgemeldet und meldest dich mit dem neuen Passwort wieder an.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1168, 'change_password_intro', 'Change the password you use to log in.', 'ACTIVE', SYSDATE, 'Change the password you use to log in.', 'Смени пароль, с которым входишь.', 'Schimbă parola cu care te autentifici.', 'Ändere das Passwort, mit dem du dich anmeldest.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1169, 'change_photo', 'Change photo', 'ACTIVE', SYSDATE, 'Change photo', 'Сменить фото', 'Schimbă fotografia', 'Foto ändern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1170, 'chat_with', 'Chat with {{name}}', 'ACTIVE', SYSDATE, 'Chat with {{name}}', 'Чат с {{name}}', 'Chat cu {{name}}', 'Chat mit {{name}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1171, 'choose_categories', 'Choose one or more', 'ACTIVE', SYSDATE, 'Choose one or more', 'Выбери одну или несколько', 'Alege una sau mai multe', 'Wähl eine oder mehrere')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1172, 'choose_trophy', 'Choose a trophy', 'ACTIVE', SYSDATE, 'Choose a trophy', 'Выбери трофей', 'Alege un trofeu', 'Wähl eine Trophäe')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1173, 'clear_search', 'Clear search', 'ACTIVE', SYSDATE, 'Clear search', 'Очистить поиск', 'Șterge căutarea', 'Suche löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1174, 'close', 'Close', 'ACTIVE', SYSDATE, 'Close', 'Закрыть', 'Închide', 'Schließen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1175, 'coins_earned', 'Coins', 'ACTIVE', SYSDATE, 'Coins', 'Монеты', 'Monede', 'Münzen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1176, 'coins_tooltip', '{{coins}} coins — open the shop', 'ACTIVE', SYSDATE, '{{coins}} coins — open the shop', '{{coins}} монет — открыть магазин', '{{coins}} monede — deschide magazinul', '{{coins}} Münzen — Shop öffnen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1177, 'compact_nav', 'Icons only in the main menu', 'ACTIVE', SYSDATE, 'Icons only in the main menu', 'Только значки в главном меню', 'Doar pictograme în meniul principal', 'Nur Symbole im Hauptmenü')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1178, 'confirm', 'Confirm', 'ACTIVE', SYSDATE, 'Confirm', 'Подтвердить', 'Confirmă', 'Bestätigen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1179, 'connected', 'Connected', 'ACTIVE', SYSDATE, 'Connected', 'Подключено', 'Conectat', 'Verbunden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1180, 'connecting', 'Connecting…', 'ACTIVE', SYSDATE, 'Connecting…', 'Подключение…', 'Se conectează…', 'Verbinde…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1181, 'conquest', 'Conquest', 'ACTIVE', SYSDATE, 'Conquest', 'Завоевание', 'Cucerire', 'Eroberung')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1182, 'conquest_held_by', '{{country}}, held by {{player}}', 'ACTIVE', SYSDATE, '{{country}}, held by {{player}}', '{{country}}, владеет {{player}}', '{{country}}, deținută de {{player}}', '{{country}}, gehalten von {{player}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1183, 'conquest_lead', 'Answer a country right, and fastest, to take it. Hold it until someone beats you.', 'ACTIVE', SYSDATE, 'Answer a country right, and fastest, to take it. Hold it until someone beats you.', 'Ответь на вопросы о стране правильно и быстрее всех, чтобы захватить её. Удерживай, пока тебя не обойдут.', 'Răspunde corect și cel mai repede la o țară ca s-o cucerești. O păstrezi până te întrece cineva.', 'Beantworte ein Land richtig und am schnellsten, um es einzunehmen. Du hältst es, bis dich jemand schlägt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1184, 'conquest_legend_held', 'Conquered', 'ACTIVE', SYSDATE, 'Conquered', 'Захвачено', 'Cucerită', 'Erobert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1185, 'conquest_legend_idle', 'Not taken yet', 'ACTIVE', SYSDATE, 'Not taken yet', 'Ещё не захвачено', 'Încă necucerită', 'Noch nicht eingenommen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1186, 'conquest_legend_open', 'Open this round', 'ACTIVE', SYSDATE, 'Open this round', 'Открыто в этом раунде', 'Deschisă în această rundă', 'In dieser Runde offen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1187, 'conquest_map', 'The conquest map', 'ACTIVE', SYSDATE, 'The conquest map', 'Карта завоеваний', 'Harta cuceririlor', 'Die Eroberungskarte')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1188, 'conquest_no_countries', 'No countries are set up yet. A content editor adds them as categories under Countries, each named by its three-letter code.', 'ACTIVE', SYSDATE, 'No countries are set up yet. A content editor adds them as categories under Countries, each named by its three-letter code.', 'Страны ещё не настроены. Редактор контента добавляет их как категории в разделе «Страны», каждая называется трёхбуквенным кодом.', 'Încă nu sunt configurate țări. Un editor de conținut le adaugă drept categorii sub Țări, fiecare numită după codul său din trei litere.', 'Es sind noch keine Länder eingerichtet. Ein Content-Editor fügt sie als Kategorien unter Länder hinzu, jeweils benannt nach ihrem dreistelligen Code.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1189, 'conquest_no_team', 'Yourself', 'ACTIVE', SYSDATE, 'Yourself', 'Сам за себя', 'Pe cont propriu', 'Für dich allein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1190, 'conquest_none_open_today', 'Nothing is open today. It opens again {{time}}.', 'ACTIVE', SYSDATE, 'Nothing is open today. It opens again {{time}}.', 'Сегодня ничего не открыто. Снова откроется {{time}}.', 'Azi nu e nimic deschis. Se redeschide {{time}}.', 'Heute ist nichts offen. Es öffnet wieder {{time}}.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1191, 'conquest_open', 'Open until {{time}}', 'ACTIVE', SYSDATE, 'Open until {{time}}', 'Открыто до {{time}}', 'Deschis până la {{time}}', 'Offen bis {{time}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1192, 'conquest_open_now', 'Open this round', 'ACTIVE', SYSDATE, 'Open this round', 'Открыто в этом раунде', 'Deschisă în această rundă', 'In dieser Runde offen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1193, 'conquest_opens_again', 'Opens again {{time}}', 'ACTIVE', SYSDATE, 'Opens again {{time}}', 'Снова откроется {{time}}', 'Se redeschide {{time}}', 'Öffnet wieder {{time}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1194, 'conquest_round', 'Round {{round}}', 'ACTIVE', SYSDATE, 'Round {{round}}', 'Раунд {{round}}', 'Runda {{round}}', 'Runde {{round}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1195, 'conquest_sign_in', 'Log in to take part in the conquest', 'ACTIVE', SYSDATE, 'Log in to take part in the conquest', 'Войди, чтобы участвовать в завоевании', 'Autentifică-te ca să participi la cucerire', 'Melde dich an, um bei der Eroberung mitzumachen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1196, 'conquest_take', 'Take {{country}}', 'ACTIVE', SYSDATE, 'Take {{country}}', 'Захватить: {{country}}', 'Cucerește {{country}}', '{{country}} einnehmen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1197, 'conquest_team_countries', '{{count}} countries', 'ACTIVE', SYSDATE, '{{count}} countries', 'Стран: {{count}}', '{{count}} țări', '{{count}} Länder')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1198, 'conquest_teams', 'Teams', 'ACTIVE', SYSDATE, 'Teams', 'Команды', 'Echipe', 'Teams')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1199, 'conquest_teams_none', 'No team holds a country yet. Pick a group and take one.', 'ACTIVE', SYSDATE, 'No team holds a country yet. Pick a group and take one.', 'Ни одна команда пока не владеет страной. Выбери группу и захвати одну.', 'Nicio echipă nu deține încă o țară. Alege un grup și cucerește una.', 'Noch hält kein Team ein Land. Wähl eine Gruppe und nimm eins ein.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1200, 'conquest_to_map', 'See the map', 'ACTIVE', SYSDATE, 'See the map', 'Открыть карту', 'Vezi harta', 'Zur Karte')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1201, 'conquest_to_map_open', 'Take a country', 'ACTIVE', SYSDATE, 'Take a country', 'Захватить страну', 'Cucerește o țară', 'Ein Land einnehmen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1202, 'conquest_unclaimed', 'Nobody holds this yet.', 'ACTIVE', SYSDATE, 'Nobody holds this yet.', 'Пока никто не владеет этой страной.', 'Încă nu o deține nimeni.', 'Noch hält niemand dieses Land.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1203, 'conquest_unnamed_team', 'A group', 'ACTIVE', SYSDATE, 'A group', 'Группа', 'Un grup', 'Eine Gruppe')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1204, 'conquest_your_team', 'You play for', 'ACTIVE', SYSDATE, 'You play for', 'Ты играешь за', 'Joci pentru', 'Du spielst für')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1205, 'conquest_yours', 'Yours', 'ACTIVE', SYSDATE, 'Yours', 'Твои', 'Ale tale', 'Deine')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1206, 'content_actions', 'Actions', 'ACTIVE', SYSDATE, 'Actions', 'Действия', 'Acțiuni', 'Aktionen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1207, 'content_active', 'Active', 'ACTIVE', SYSDATE, 'Active', 'Активен', 'Activ', 'Aktiv')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1208, 'content_add_another_answer', 'Add another answer', 'ACTIVE', SYSDATE, 'Add another answer', 'Добавить ещё один ответ', 'Adaugă încă un răspuns', 'Weitere Antwort hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1209, 'content_admin_sections', 'Admin sections', 'ACTIVE', SYSDATE, 'Admin sections', 'Разделы администрирования', 'Secțiuni de administrare', 'Verwaltungsbereiche')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1210, 'content_answer', 'Answer', 'ACTIVE', SYSDATE, 'Answer', 'Ответ', 'Răspuns', 'Antwort')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1211, 'content_answer_error', 'Type each answer or pick it from the glossary', 'ACTIVE', SYSDATE, 'Type each answer or pick it from the glossary', 'Введите каждый ответ или выберите его из глоссария', 'Scrie fiecare răspuns sau alege-l din glosar', 'Jede Antwort eingeben oder aus dem Glossar auswählen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1212, 'content_answer_in', 'Answer in {{language}}', 'ACTIVE', SYSDATE, 'Answer in {{language}}', 'Ответ на языке: {{language}}', 'Răspuns în {{language}}', 'Antwort auf {{language}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1213, 'content_answer_number', 'Answer {{number}}', 'ACTIVE', SYSDATE, 'Answer {{number}}', 'Ответ {{number}}', 'Răspunsul {{number}}', 'Antwort {{number}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1214, 'content_answer_placeholder', 'Type the answer', 'ACTIVE', SYSDATE, 'Type the answer', 'Введите ответ', 'Scrie răspunsul', 'Antwort eingeben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1215, 'content_answers_hint', 'Several answers make the question multiple choice. Terms are also paired key with value for drag and drop and, when the values are numbers, sorted for in order.', 'ACTIVE', SYSDATE, 'Several answers make the question multiple choice. Terms are also paired key with value for drag and drop and, when the values are numbers, sorted for in order.', 'Несколько ответов делают вопрос вопросом с выбором вариантов. Термины также объединяются в пары «ключ — значение» для перетаскивания, а если значения — числа, сортируются для режима «по порядку».', 'Mai multe răspunsuri transformă întrebarea într-una cu variante multiple. Termenii sunt de asemenea împerecheați cheie cu valoare pentru tragere și plasare și, când valorile sunt numere, sortați pentru ordonare.', 'Mehrere Antworten machen die Frage zu einer Multiple-Choice-Frage. Begriffe werden außerdem für Drag & Drop als Schlüssel-Wert-Paare zugeordnet und, wenn die Werte Zahlen sind, für „In Reihenfolge“ sortiert.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1216, 'content_article_category_error', 'Choose a category', 'ACTIVE', SYSDATE, 'Choose a category', 'Выберите категорию', 'Alege o categorie', 'Kategorie auswählen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1217, 'content_article_content_error', 'Write some content', 'ACTIVE', SYSDATE, 'Write some content', 'Добавьте содержимое', 'Scrie un conținut', 'Inhalt eingeben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1218, 'content_article_published', 'Article published', 'ACTIVE', SYSDATE, 'Article published', 'Статья опубликована', 'Articol publicat', 'Artikel veröffentlicht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1219, 'content_article_saved', 'Article saved — set it to Active to publish', 'ACTIVE', SYSDATE, 'Article saved — set it to Active to publish', 'Статья сохранена — сделайте её активной, чтобы опубликовать', 'Articol salvat — setează-l ca Activ pentru a-l publica', 'Artikel gespeichert — zum Veröffentlichen auf Aktiv setzen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1220, 'content_article_title_error', 'Give the article a title', 'ACTIVE', SYSDATE, 'Give the article a title', 'Укажите заголовок статьи', 'Dă-i articolului un titlu', 'Titel für den Artikel eingeben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1221, 'content_article_title_placeholder', 'e.g. How scoring works in Express quiz', 'ACTIVE', SYSDATE, 'e.g. How scoring works in Express quiz', 'напр. Как начисляются очки в экспресс-викторине', 'ex. Cum se calculează punctajul în quizul Express', 'z. B. So funktioniert die Punktevergabe im Express-Quiz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1222, 'content_attachment', 'Attachment', 'ACTIVE', SYSDATE, 'Attachment', 'Вложение', 'Atașament', 'Anhang')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1223, 'content_attributes', 'Attributes', 'ACTIVE', SYSDATE, 'Attributes', 'Атрибуты', 'Atribute', 'Attribute')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1224, 'content_badge_unresolved', '{{count}} unresolved', 'ACTIVE', SYSDATE, '{{count}} unresolved', 'Нерешённых: {{count}}', '{{count}} nerezolvate', '{{count}} offen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1225, 'content_categories', 'Categories', 'ACTIVE', SYSDATE, 'Categories', 'Категории', 'Categorii', 'Kategorien')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1226, 'content_categories_pages', 'Categories pages', 'ACTIVE', SYSDATE, 'Categories pages', 'Страницы категорий', 'Paginile categoriilor', 'Kategorieseiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1227, 'content_category', 'Category', 'ACTIVE', SYSDATE, 'Category', 'Категория', 'Categorie', 'Kategorie')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1228, 'content_category_deleted', 'Category deleted', 'ACTIVE', SYSDATE, 'Category deleted', 'Категория удалена', 'Categorie ștearsă', 'Kategorie gelöscht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1229, 'content_category_image_hint', 'Shown on the category card and behind the quiz list.', 'ACTIVE', SYSDATE, 'Shown on the category card and behind the quiz list.', 'Отображается на карточке категории и за списком викторин.', 'Afișată pe cardul categoriei și în spatele listei de quizuri.', 'Wird auf der Kategoriekarte und hinter der Quizliste angezeigt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1230, 'content_category_name_error', 'Give the category a name', 'ACTIVE', SYSDATE, 'Give the category a name', 'Укажите название категории', 'Dă-i categoriei un nume', 'Namen für die Kategorie eingeben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1231, 'content_category_name_placeholder', 'e.g. Geography', 'ACTIVE', SYSDATE, 'e.g. Geography', 'напр. География', 'ex. Geografie', 'z. B. Geografie')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1232, 'content_category_parent_hint', 'Leave empty for a top-level category.', 'ACTIVE', SYSDATE, 'Leave empty for a top-level category.', 'Оставьте пустым для категории верхнего уровня.', 'Lasă gol pentru o categorie de nivel superior.', 'Für eine Kategorie der obersten Ebene leer lassen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1233, 'content_category_saved', 'Category successfully saved', 'ACTIVE', SYSDATE, 'Category successfully saved', 'Категория успешно сохранена', 'Categorie salvată cu succes', 'Kategorie erfolgreich gespeichert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1234, 'content_category_search_placeholder', 'Search by name or parent…', 'ACTIVE', SYSDATE, 'Search by name or parent…', 'Поиск по названию или родителю…', 'Caută după nume sau părinte…', 'Nach Name oder übergeordneter Kategorie suchen…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1235, 'content_category_updated', 'Category updated', 'ACTIVE', SYSDATE, 'Category updated', 'Категория обновлена', 'Categorie actualizată', 'Kategorie aktualisiert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1236, 'content_choose_category', 'Choose a category…', 'ACTIVE', SYSDATE, 'Choose a category…', 'Выберите категорию…', 'Alege o categorie…', 'Kategorie auswählen…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1237, 'content_choose_type', 'Choose a type…', 'ACTIVE', SYSDATE, 'Choose a type…', 'Выберите тип…', 'Alege un tip…', 'Typ auswählen…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1238, 'content_choose_type_error', 'Choose a type', 'ACTIVE', SYSDATE, 'Choose a type', 'Выберите тип', 'Alege un tip', 'Typ auswählen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1239, 'content_complexity_error', 'Set a complexity level', 'ACTIVE', SYSDATE, 'Set a complexity level', 'Укажите уровень сложности', 'Setează un nivel de dificultate', 'Schwierigkeitsgrad festlegen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1240, 'content_complexity_hint', '1 (easy) to 10 (hard).', 'ACTIVE', SYSDATE, '1 (easy) to 10 (hard).', 'От 1 (легко) до 10 (сложно).', 'De la 1 (ușor) la 10 (greu).', '1 (leicht) bis 10 (schwer).')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1241, 'content_complexity_level', 'Complexity level', 'ACTIVE', SYSDATE, 'Complexity level', 'Уровень сложности', 'Nivel de dificultate', 'Schwierigkeitsgrad')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1242, 'content_content', 'Content', 'ACTIVE', SYSDATE, 'Content', 'Содержимое', 'Conținut', 'Inhalt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1243, 'content_cover_image', 'Cover image', 'ACTIVE', SYSDATE, 'Cover image', 'Обложка', 'Imagine de copertă', 'Titelbild')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1244, 'content_created', 'Created', 'ACTIVE', SYSDATE, 'Created', 'Создано', 'Creat', 'Erstellt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1245, 'content_dashboard', 'Content dashboard', 'ACTIVE', SYSDATE, 'Content dashboard', 'Панель контента', 'Panou de conținut', 'Inhalts-Dashboard')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1246, 'content_delete_category_confirm', 'will be permanently deleted. This can’t be undone, and it only works once no questions, glossaries or subcategories use it.', 'ACTIVE', SYSDATE, 'will be permanently deleted. This can’t be undone, and it only works once no questions, glossaries or subcategories use it.', 'будет удалена навсегда. Это действие нельзя отменить, и оно сработает, только если категорию не используют вопросы, глоссарии или подкатегории.', 'va fi ștearsă definitiv. Acțiunea nu poate fi anulată și funcționează doar dacă nicio întrebare, niciun glosar sau nicio subcategorie nu o mai folosește.', 'wird endgültig gelöscht. Dies kann nicht rückgängig gemacht werden und funktioniert nur, wenn keine Fragen, Glossare oder Unterkategorien sie mehr verwenden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1247, 'content_delete_category_title', 'Delete category?', 'ACTIVE', SYSDATE, 'Delete category?', 'Удалить категорию?', 'Ștergi categoria?', 'Kategorie löschen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1248, 'content_delete_glossary_confirm', 'and its translations will be permanently deleted. It only works once no questions or other glossaries use it.', 'ACTIVE', SYSDATE, 'and its translations will be permanently deleted. It only works once no questions or other glossaries use it.', 'и его переводы будут удалены навсегда. Это сработает, только если его не используют вопросы или другие глоссарии.', 'și traducerile sale vor fi șterse definitiv. Funcționează doar dacă nicio întrebare sau alt glosar nu îl mai folosește.', 'und seine Übersetzungen werden endgültig gelöscht. Dies funktioniert nur, wenn keine Fragen oder anderen Glossare ihn mehr verwenden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1249, 'content_delete_glossary_title', 'Delete glossary?', 'ACTIVE', SYSDATE, 'Delete glossary?', 'Удалить глоссарий?', 'Ștergi glosarul?', 'Glossar löschen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1250, 'content_delete_glossary_type_confirm', 'will be permanently deleted. It only works once no glossaries use this type.', 'ACTIVE', SYSDATE, 'will be permanently deleted. It only works once no glossaries use this type.', 'будет удалён навсегда. Это сработает, только если ни один глоссарий не использует этот тип.', 'va fi șters definitiv. Funcționează doar dacă niciun glosar nu mai folosește acest tip.', 'wird endgültig gelöscht. Dies funktioniert nur, wenn kein Glossar diesen Typ mehr verwendet.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1251, 'content_delete_glossary_type_title', 'Delete glossary type?', 'ACTIVE', SYSDATE, 'Delete glossary type?', 'Удалить тип глоссария?', 'Ștergi tipul de glosar?', 'Glossartyp löschen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1252, 'content_delete_name', 'Delete {{name}}', 'ACTIVE', SYSDATE, 'Delete {{name}}', 'Удалить {{name}}', 'Șterge {{name}}', '{{name}} löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1253, 'content_delete_question_confirm', '“{{question}}” will be permanently deleted, with its answers and translations. This can’t be undone.', 'ACTIVE', SYSDATE, '“{{question}}” will be permanently deleted, with its answers and translations. This can’t be undone.', '«{{question}}» будет удалён навсегда вместе с ответами и переводами. Это действие нельзя отменить.', '„{{question}}” va fi ștearsă definitiv, împreună cu răspunsurile și traducerile sale. Acțiunea nu poate fi anulată.', '„{{question}}“ wird endgültig gelöscht, samt Antworten und Übersetzungen. Dies kann nicht rückgängig gemacht werden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1254, 'content_delete_question_title', 'Delete question?', 'ACTIVE', SYSDATE, 'Delete question?', 'Удалить вопрос?', 'Ștergi întrebarea?', 'Frage löschen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1255, 'content_drop_image', 'Drop an image or click to choose', 'ACTIVE', SYSDATE, 'Drop an image or click to choose', 'Перетащите изображение или нажмите, чтобы выбрать', 'Trage o imagine aici sau dă clic pentru a alege', 'Bild hierher ziehen oder zum Auswählen klicken')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1256, 'content_edit_category', 'Edit category', 'ACTIVE', SYSDATE, 'Edit category', 'Редактировать категорию', 'Editează categoria', 'Kategorie bearbeiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1257, 'content_edit_glossary', 'Edit glossary', 'ACTIVE', SYSDATE, 'Edit glossary', 'Редактировать глоссарий', 'Editează glosarul', 'Glossar bearbeiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1258, 'content_edit_glossary_type', 'Edit glossary type', 'ACTIVE', SYSDATE, 'Edit glossary type', 'Редактировать тип глоссария', 'Editează tipul de glosar', 'Glossartyp bearbeiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1259, 'content_edit_name', 'Edit {{name}}', 'ACTIVE', SYSDATE, 'Edit {{name}}', 'Редактировать {{name}}', 'Editează {{name}}', '{{name}} bearbeiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1260, 'content_edit_question', 'Edit question', 'ACTIVE', SYSDATE, 'Edit question', 'Редактировать вопрос', 'Editează întrebarea', 'Frage bearbeiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1261, 'content_empty_categories', 'categories', 'ACTIVE', SYSDATE, 'categories', 'категорий', 'categorii', 'Kategorien')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1262, 'content_empty_glossaries', 'glossaries', 'ACTIVE', SYSDATE, 'glossaries', 'глоссариев', 'glosare', 'Glossare')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1263, 'content_empty_glossary_types', 'glossary types', 'ACTIVE', SYSDATE, 'glossary types', 'типов глоссария', 'tipuri de glosar', 'Glossartypen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1264, 'content_empty_no_match', 'No {{what}} match “{{query}}”.', 'ACTIVE', SYSDATE, 'No {{what}} match “{{query}}”.', 'Нет {{what}}, соответствующих «{{query}}».', 'Nu există {{what}} care să corespundă cu „{{query}}”.', 'Keine {{what}} passend zu „{{query}}“.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1265, 'content_empty_none_yet', 'No {{what}} yet.', 'ACTIVE', SYSDATE, 'No {{what}} yet.', 'Пока нет {{what}}.', 'Încă nu există {{what}}.', 'Noch keine {{what}}.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1266, 'content_empty_questions', 'questions', 'ACTIVE', SYSDATE, 'questions', 'вопросов', 'întrebări', 'Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1267, 'content_exclude_quiz_types', 'Exclude quiz types', 'ACTIVE', SYSDATE, 'Exclude quiz types', 'Исключить типы викторин', 'Exclude tipuri de quiz', 'Quiztypen ausschließen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1268, 'content_exclude_quiz_types_hint', 'The question never appears in these quiz types. Multiple choice, drag and drop and in order are also excluded on save when the answers don''t fit them.', 'ACTIVE', SYSDATE, 'The question never appears in these quiz types. Multiple choice, drag and drop and in order are also excluded on save when the answers don''t fit them.', 'Вопрос никогда не появится в этих типах викторин. Режимы «выбор вариантов», «перетаскивание» и «по порядку» также исключаются при сохранении, если ответы для них не подходят.', 'Întrebarea nu apare niciodată în aceste tipuri de quiz. Variante multiple, tragere și plasare și ordonare sunt de asemenea excluse la salvare când răspunsurile nu li se potrivesc.', 'Die Frage erscheint nie in diesen Quiztypen. Multiple Choice, Drag & Drop und „In Reihenfolge“ werden beim Speichern ebenfalls ausgeschlossen, wenn die Antworten nicht dazu passen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1269, 'content_from_glossary', 'From glossary', 'ACTIVE', SYSDATE, 'From glossary', 'Из глоссария', 'Din glosar', 'Aus dem Glossar')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1270, 'content_glossaries', 'Glossaries', 'ACTIVE', SYSDATE, 'Glossaries', 'Глоссарии', 'Glosare', 'Glossare')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1271, 'content_glossaries_by', 'Glossaries by', 'ACTIVE', SYSDATE, 'Glossaries by', 'Глоссарии по категории', 'Glosare după categorie', 'Glossare nach Kategorie')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1272, 'content_glossaries_pages', 'Glossaries pages', 'ACTIVE', SYSDATE, 'Glossaries pages', 'Страницы глоссариев', 'Paginile glosarelor', 'Glossarseiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1273, 'content_glossary_deleted', 'Glossary deleted', 'ACTIVE', SYSDATE, 'Glossary deleted', 'Глоссарий удалён', 'Glosar șters', 'Glossar gelöscht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1274, 'content_glossary_options_placeholder', 'e.g. 47.01,28.86 (a city''s lat,lng for map quizzes)', 'ACTIVE', SYSDATE, 'e.g. 47.01,28.86 (a city''s lat,lng for map quizzes)', 'напр. 47.01,28.86 (lat,lng города для викторин с картой)', 'ex. 47.01,28.86 (lat,lng ale unui oraș pentru quizurile cu hartă)', 'z. B. 47.01,28.86 (lat,lng einer Stadt für Karten-Quizze)')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1275, 'content_glossary_saved', 'Glossary successfully saved', 'ACTIVE', SYSDATE, 'Glossary successfully saved', 'Глоссарий успешно сохранён', 'Glosar salvat cu succes', 'Glossar erfolgreich gespeichert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1276, 'content_glossary_search_placeholder', 'Search by key, value or type…', 'ACTIVE', SYSDATE, 'Search by key, value or type…', 'Поиск по ключу, значению или типу…', 'Caută după cheie, valoare sau tip…', 'Nach Schlüssel, Wert oder Typ suchen…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1277, 'content_glossary_type', 'Glossary type', 'ACTIVE', SYSDATE, 'Glossary type', 'Тип глоссария', 'Tip de glosar', 'Glossartyp')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1278, 'content_glossary_type_deleted', 'Glossary type deleted', 'ACTIVE', SYSDATE, 'Glossary type deleted', 'Тип глоссария удалён', 'Tip de glosar șters', 'Glossartyp gelöscht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1279, 'content_glossary_type_name_error', 'Give the type a name', 'ACTIVE', SYSDATE, 'Give the type a name', 'Укажите название типа', 'Dă-i tipului un nume', 'Namen für den Typ eingeben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1280, 'content_glossary_type_name_placeholder', 'e.g. Capital city', 'ACTIVE', SYSDATE, 'e.g. Capital city', 'напр. Столица', 'ex. Capitală', 'z. B. Hauptstadt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1281, 'content_glossary_type_options_hint', 'Optional. map:country, map:continent or map:city turns its terms into map answers.', 'ACTIVE', SYSDATE, 'Optional. map:country, map:continent or map:city turns its terms into map answers.', 'Необязательно. map:country, map:continent или map:city превращает его термины в ответы на карте.', 'Opțional. map:country, map:continent sau map:city transformă termenii săi în răspunsuri pe hartă.', 'Optional. map:country, map:continent oder map:city macht seine Begriffe zu Kartenantworten.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1282, 'content_glossary_type_saved', 'Glossary type successfully saved', 'ACTIVE', SYSDATE, 'Glossary type successfully saved', 'Тип глоссария успешно сохранён', 'Tip de glosar salvat cu succes', 'Glossartyp erfolgreich gespeichert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1283, 'content_glossary_type_search_placeholder', 'Search by name or options…', 'ACTIVE', SYSDATE, 'Search by name or options…', 'Поиск по названию или параметрам…', 'Caută după nume sau opțiuni…', 'Nach Name oder Optionen suchen…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1284, 'content_glossary_type_updated', 'Glossary type updated', 'ACTIVE', SYSDATE, 'Glossary type updated', 'Тип глоссария обновлён', 'Tip de glosar actualizat', 'Glossartyp aktualisiert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1285, 'content_glossary_types', 'Glossary types', 'ACTIVE', SYSDATE, 'Glossary types', 'Типы глоссария', 'Tipuri de glosar', 'Glossartypen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1286, 'content_glossary_types_pages', 'Glossary types pages', 'ACTIVE', SYSDATE, 'Glossary types pages', 'Страницы типов глоссария', 'Pagini tipuri de glosar', 'Seiten der Glossartypen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1287, 'content_image', 'Image', 'ACTIVE', SYSDATE, 'Image', 'Изображение', 'Imagine', 'Bild')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1288, 'content_image_replace_hint', 'Pick a new image to replace it; otherwise the current one stays.', 'ACTIVE', SYSDATE, 'Pick a new image to replace it; otherwise the current one stays.', 'Выберите новое изображение для замены; иначе останется текущее.', 'Alege o imagine nouă pentru a o înlocui; altfel rămâne cea actuală.', 'Neues Bild wählen, um es zu ersetzen; sonst bleibt das aktuelle.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1289, 'content_is_active', 'Is Active', 'ACTIVE', SYSDATE, 'Is Active', 'Активен', 'Este activ', 'Aktiv')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1290, 'content_kb_status_active', 'Active', 'ACTIVE', SYSDATE, 'Active', 'Активна', 'Activ', 'Aktiv')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1291, 'content_kb_status_active_hint', 'Published: shown in the Wiki.', 'ACTIVE', SYSDATE, 'Published: shown in the Wiki.', 'Опубликована: показывается в Wiki.', 'Publicat: afișat în Wiki.', 'Veröffentlicht: wird im Wiki angezeigt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1292, 'content_kb_status_draft', 'Draft', 'ACTIVE', SYSDATE, 'Draft', 'Черновик', 'Ciornă', 'Entwurf')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1293, 'content_kb_status_draft_hint', 'Still being written. Not shown to readers.', 'ACTIVE', SYSDATE, 'Still being written. Not shown to readers.', 'Ещё пишется. Не показывается читателям.', 'Încă în lucru. Nu este afișat cititorilor.', 'Wird noch geschrieben. Für Leser nicht sichtbar.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1294, 'content_kb_status_hidden', 'Hidden', 'ACTIVE', SYSDATE, 'Hidden', 'Скрыта', 'Ascuns', 'Ausgeblendet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1295, 'content_kb_status_hidden_hint', 'Taken down but kept. Not shown to readers.', 'ACTIVE', SYSDATE, 'Taken down but kept. Not shown to readers.', 'Снята с публикации, но сохранена. Не показывается читателям.', 'Retras, dar păstrat. Nu este afișat cititorilor.', 'Zurückgezogen, aber behalten. Für Leser nicht sichtbar.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1296, 'content_kb_status_internal', 'Internal', 'ACTIVE', SYSDATE, 'Internal', 'Внутренняя', 'Intern', 'Intern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1297, 'content_kb_status_internal_hint', 'For the team only. Not shown to readers.', 'ACTIVE', SYSDATE, 'For the team only. Not shown to readers.', 'Только для команды. Не показывается читателям.', 'Doar pentru echipă. Nu este afișat cititorilor.', 'Nur für das Team. Für Leser nicht sichtbar.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1298, 'content_kb_status_pending', 'Pending review', 'ACTIVE', SYSDATE, 'Pending review', 'На проверке', 'În așteptarea revizuirii', 'Prüfung ausstehend')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1299, 'content_kb_status_pending_hint', 'Waiting for review. Not shown to readers.', 'ACTIVE', SYSDATE, 'Waiting for review. Not shown to readers.', 'Ожидает проверки. Не показывается читателям.', 'Așteaptă revizuirea. Nu este afișat cititorilor.', 'Wartet auf Prüfung. Für Leser nicht sichtbar.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1300, 'content_key', 'Key', 'ACTIVE', SYSDATE, 'Key', 'Ключ', 'Cheie', 'Schlüssel')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1301, 'content_name', 'Name', 'ACTIVE', SYSDATE, 'Name', 'Название', 'Nume', 'Name')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1302, 'content_new_article', 'New article', 'ACTIVE', SYSDATE, 'New article', 'Новая статья', 'Articol nou', 'Neuer Artikel')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1303, 'content_new_category', 'New category', 'ACTIVE', SYSDATE, 'New category', 'Новая категория', 'Categorie nouă', 'Neue Kategorie')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1304, 'content_new_glossary', 'New glossary', 'ACTIVE', SYSDATE, 'New glossary', 'Новый глоссарий', 'Glosar nou', 'Neues Glossar')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1305, 'content_new_glossary_type', 'New glossary type', 'ACTIVE', SYSDATE, 'New glossary type', 'Новый тип глоссария', 'Tip de glosar nou', 'Neuer Glossartyp')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1306, 'content_new_question', 'New question', 'ACTIVE', SYSDATE, 'New question', 'Новый вопрос', 'Întrebare nouă', 'Neue Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1307, 'content_next_page', 'Next page', 'ACTIVE', SYSDATE, 'Next page', 'Следующая страница', 'Pagina următoare', 'Nächste Seite')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1308, 'content_no_parent', 'No parent', 'ACTIVE', SYSDATE, 'No parent', 'Без родителя', 'Fără părinte', 'Kein übergeordnetes Element')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1309, 'content_no_parent_top_level', 'No parent (top level)', 'ACTIVE', SYSDATE, 'No parent (top level)', 'Без родителя (верхний уровень)', 'Fără părinte (nivel superior)', 'Keine übergeordnete (oberste Ebene)')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1310, 'content_none', 'None', 'ACTIVE', SYSDATE, 'None', 'Нет', 'Niciunul', 'Keine')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1311, 'content_optional', 'Optional.', 'ACTIVE', SYSDATE, 'Optional.', 'Необязательно.', 'Opțional.', 'Optional.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1312, 'content_options', 'Options', 'ACTIVE', SYSDATE, 'Options', 'Варианты', 'Opțiuni', 'Optionen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1313, 'content_or', 'or', 'ACTIVE', SYSDATE, 'or', 'или', 'sau', 'oder')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1314, 'content_page_number', 'Page {{number}}', 'ACTIVE', SYSDATE, 'Page {{number}}', 'Страница {{number}}', 'Pagina {{number}}', 'Seite {{number}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1315, 'content_pagination', 'Pagination', 'ACTIVE', SYSDATE, 'Pagination', 'Пагинация', 'Paginare', 'Seitennavigation')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1316, 'content_pagination_range', '{{from}}–{{to}} of {{total}}', 'ACTIVE', SYSDATE, '{{from}}–{{to}} of {{total}}', '{{from}}–{{to}} из {{total}}', '{{from}}–{{to}} din {{total}}', '{{from}}–{{to}} von {{total}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1317, 'content_parent', 'Parent', 'ACTIVE', SYSDATE, 'Parent', 'Родитель', 'Părinte', 'Übergeordnet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1318, 'content_pick_row_to_edit', 'Pick a row in the table to edit it.', 'ACTIVE', SYSDATE, 'Pick a row in the table to edit it.', 'Выберите строку в таблице, чтобы изменить её.', 'Alege un rând din tabel pentru a-l edita.', 'Zeile in der Tabelle wählen, um sie zu bearbeiten.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1319, 'content_previous_page', 'Previous page', 'ACTIVE', SYSDATE, 'Previous page', 'Предыдущая страница', 'Pagina anterioară', 'Vorherige Seite')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1320, 'content_priority', 'Priority', 'ACTIVE', SYSDATE, 'Priority', 'Приоритет', 'Prioritate', 'Priorität')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1321, 'content_publish', 'Publish', 'ACTIVE', SYSDATE, 'Publish', 'Опубликовать', 'Publică', 'Veröffentlichen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1322, 'content_publishing', 'Publishing', 'ACTIVE', SYSDATE, 'Publishing', 'Публикация', 'Publicare', 'Veröffentlichung')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1323, 'content_question_deleted', 'Question deleted', 'ACTIVE', SYSDATE, 'Question deleted', 'Вопрос удалён', 'Întrebare ștearsă', 'Frage gelöscht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1324, 'content_question_edit_hint', 'Answers and translations stay as they are.', 'ACTIVE', SYSDATE, 'Answers and translations stay as they are.', 'Ответы и переводы останутся без изменений.', 'Răspunsurile și traducerile rămân neschimbate.', 'Antworten und Übersetzungen bleiben unverändert.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1325, 'content_question_in', 'Question in {{language}}', 'ACTIVE', SYSDATE, 'Question in {{language}}', 'Вопрос на языке: {{language}}', 'Întrebare în {{language}}', 'Frage auf {{language}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1326, 'content_question_saved', 'Question successfully saved', 'ACTIVE', SYSDATE, 'Question successfully saved', 'Вопрос успешно сохранён', 'Întrebare salvată cu succes', 'Frage erfolgreich gespeichert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1327, 'content_question_search_placeholder', 'Search by question, topic or category…', 'ACTIVE', SYSDATE, 'Search by question, topic or category…', 'Поиск по вопросу, теме или категории…', 'Caută după întrebare, subiect sau categorie…', 'Nach Frage, Thema oder Kategorie suchen…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1328, 'content_question_translations', 'Question translations', 'ACTIVE', SYSDATE, 'Question translations', 'Переводы вопроса', 'Traducerile întrebării', 'Übersetzungen der Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1329, 'content_question_updated', 'Question updated', 'ACTIVE', SYSDATE, 'Question updated', 'Вопрос обновлён', 'Întrebare actualizată', 'Frage aktualisiert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1330, 'content_questions', 'Questions', 'ACTIVE', SYSDATE, 'Questions', 'Вопросы', 'Întrebări', 'Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1331, 'content_questions_pages', 'Questions pages', 'ACTIVE', SYSDATE, 'Questions pages', 'Страницы вопросов', 'Pagini de întrebări', 'Seiten der Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1332, 'content_remove', 'Remove', 'ACTIVE', SYSDATE, 'Remove', 'Удалить', 'Elimină', 'Entfernen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1333, 'content_remove_answer', 'Remove answer {{number}}', 'ACTIVE', SYSDATE, 'Remove answer {{number}}', 'Удалить ответ {{number}}', 'Elimină răspunsul {{number}}', 'Antwort {{number}} entfernen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1334, 'content_remove_image', 'Remove image', 'ACTIVE', SYSDATE, 'Remove image', 'Удалить изображение', 'Elimină imaginea', 'Bild entfernen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1335, 'content_remove_tag', 'Remove tag {{tag}}', 'ACTIVE', SYSDATE, 'Remove tag {{tag}}', 'Удалить тег {{tag}}', 'Elimină eticheta {{tag}}', 'Tag {{tag}} entfernen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1336, 'content_save_category', 'Save category', 'ACTIVE', SYSDATE, 'Save category', 'Сохранить категорию', 'Salvează categoria', 'Kategorie speichern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1337, 'content_save_changes', 'Save changes', 'ACTIVE', SYSDATE, 'Save changes', 'Сохранить изменения', 'Salvează modificările', 'Änderungen speichern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1338, 'content_save_glossary', 'Save glossary', 'ACTIVE', SYSDATE, 'Save glossary', 'Сохранить глоссарий', 'Salvează glosarul', 'Glossar speichern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1339, 'content_save_question', 'Save question', 'ACTIVE', SYSDATE, 'Save question', 'Сохранить вопрос', 'Salvează întrebarea', 'Frage speichern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1340, 'content_save_type', 'Save type', 'ACTIVE', SYSDATE, 'Save type', 'Сохранить тип', 'Salvează tipul', 'Typ speichern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1341, 'content_search', 'Search', 'ACTIVE', SYSDATE, 'Search', 'Поиск', 'Căutare', 'Suche')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1342, 'content_search_categories', 'Search categories', 'ACTIVE', SYSDATE, 'Search categories', 'Поиск категорий', 'Caută categorii', 'Kategorien suchen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1343, 'content_search_glossaries', 'Search glossaries', 'ACTIVE', SYSDATE, 'Search glossaries', 'Поиск глоссариев', 'Caută glosare', 'Glossare suchen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1344, 'content_search_glossary_types', 'Search glossary types', 'ACTIVE', SYSDATE, 'Search glossary types', 'Поиск типов глоссария', 'Caută tipuri de glosar', 'Glossartypen suchen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1345, 'content_search_match', '{{count}} match', 'ACTIVE', SYSDATE, '{{count}} match', 'Совпадений: {{count}}', '{{count}} rezultat', '{{count}} Treffer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1346, 'content_search_matches', '{{count}} matches', 'ACTIVE', SYSDATE, '{{count}} matches', 'Совпадений: {{count}}', '{{count}} rezultate', '{{count}} Treffer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1347, 'content_search_placeholder', 'Search…', 'ACTIVE', SYSDATE, 'Search…', 'Поиск…', 'Caută…', 'Suchen…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1348, 'content_search_questions', 'Search questions', 'ACTIVE', SYSDATE, 'Search questions', 'Поиск вопросов', 'Caută întrebări', 'Fragen suchen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1349, 'content_set_key', 'Set a key', 'ACTIVE', SYSDATE, 'Set a key', 'Укажите ключ', 'Setează o cheie', 'Schlüssel festlegen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1350, 'content_set_value', 'Set a value', 'ACTIVE', SYSDATE, 'Set a value', 'Укажите значение', 'Setează o valoare', 'Wert festlegen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1351, 'content_status', 'Status', 'ACTIVE', SYSDATE, 'Status', 'Статус', 'Stare', 'Status')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1352, 'content_tag_placeholder', 'Type a tag, press Enter', 'ACTIVE', SYSDATE, 'Type a tag, press Enter', 'Введите тег и нажмите Enter', 'Scrie o etichetă, apasă Enter', 'Tag eingeben, Enter drücken')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1353, 'content_tags', 'Tags', 'ACTIVE', SYSDATE, 'Tags', 'Теги', 'Etichete', 'Tags')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1354, 'content_title', 'Title', 'ACTIVE', SYSDATE, 'Title', 'Заголовок', 'Titlu', 'Titel')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1355, 'content_topic', 'Topic', 'ACTIVE', SYSDATE, 'Topic', 'Тема', 'Subiect', 'Thema')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1356, 'content_translations_of', 'Translations of "{{answer}}"', 'ACTIVE', SYSDATE, 'Translations of "{{answer}}"', 'Переводы «{{answer}}»', 'Traducerile pentru „{{answer}}”', 'Übersetzungen von „{{answer}}“')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1357, 'content_type', 'Type', 'ACTIVE', SYSDATE, 'Type', 'Тип', 'Tip', 'Typ')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1358, 'content_value', 'Value', 'ACTIVE', SYSDATE, 'Value', 'Значение', 'Valoare', 'Wert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1359, 'content_value_type', 'Value type', 'ACTIVE', SYSDATE, 'Value type', 'Тип значения', 'Tipul valorii', 'Werttyp')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1360, 'content_visible', 'Visible', 'ACTIVE', SYSDATE, 'Visible', 'Видимость', 'Vizibil', 'Sichtbar')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1361, 'content_visible_to_players', 'Visible to players', 'ACTIVE', SYSDATE, 'Visible to players', 'Видна игрокам', 'Vizibilă pentru jucători', 'Für Spieler sichtbar')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1362, 'content_visible_to_readers', 'Visible to readers', 'ACTIVE', SYSDATE, 'Visible to readers', 'Видна читателям', 'Vizibil pentru cititori', 'Für Leser sichtbar')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1363, 'content_wiki', 'Wiki', 'ACTIVE', SYSDATE, 'Wiki', 'Wiki', 'Wiki', 'Wiki')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1364, 'content_without_glossary_type', '{{count}} without a glossary type', 'ACTIVE', SYSDATE, '{{count}} without a glossary type', 'Без типа глоссария: {{count}}', '{{count}} fără tip de glosar', '{{count}} ohne Glossartyp')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1365, 'continue_with', 'Continue with {{provider}}', 'ACTIVE', SYSDATE, 'Continue with {{provider}}', 'Продолжить через {{provider}}', 'Continuă cu {{provider}}', 'Weiter mit {{provider}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1366, 'copied', 'Copied', 'ACTIVE', SYSDATE, 'Copied', 'Скопировано', 'Copiat', 'Kopiert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1367, 'copy', 'Copy', 'ACTIVE', SYSDATE, 'Copy', 'Копировать', 'Copiază', 'Kopieren')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1368, 'copy_address', 'Copy the {{name}} address', 'ACTIVE', SYSDATE, 'Copy the {{name}} address', 'Скопировать адрес {{name}}', 'Copiază adresa {{name}}', '{{name}}-Adresse kopieren')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1369, 'copy_link', 'Copy link', 'ACTIVE', SYSDATE, 'Copy link', 'Копировать ссылку', 'Copiază linkul', 'Link kopieren')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1370, 'create', 'Create', 'ACTIVE', SYSDATE, 'Create', 'Создать', 'Creează', 'Erstellen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1371, 'create_group', 'Create group', 'ACTIVE', SYSDATE, 'Create group', 'Создать группу', 'Creează grup', 'Gruppe erstellen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1372, 'create_quiz', 'Create a quiz', 'ACTIVE', SYSDATE, 'Create a quiz', 'Создать викторину', 'Creează un quiz', 'Quiz erstellen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1373, 'create_quiz_subtitle', 'Write your questions, then invite people to take it.', 'ACTIVE', SYSDATE, 'Write your questions, then invite people to take it.', 'Напиши вопросы, а потом пригласи людей пройти викторину.', 'Scrie-ți întrebările, apoi invită oamenii să-l rezolve.', 'Schreib deine Fragen und lade dann Leute zum Mitspielen ein.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1374, 'custom_quiz', 'Custom quiz', 'ACTIVE', SYSDATE, 'Custom quiz', 'Своя викторина', 'Quiz personalizat', 'Eigenes Quiz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1375, 'custom_quiz_unavailable', 'This quiz is not available. Only its creator and the people invited to it can play it.', 'ACTIVE', SYSDATE, 'This quiz is not available. Only its creator and the people invited to it can play it.', 'Эта викторина недоступна. Играть в неё могут только автор и приглашённые.', 'Acest quiz nu este disponibil. Doar creatorul său și persoanele invitate îl pot juca.', 'Dieses Quiz ist nicht verfügbar. Nur sein Ersteller und die eingeladenen Personen können es spielen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1376, 'daily_challenge', 'Daily challenge', 'ACTIVE', SYSDATE, 'Daily challenge', 'Ежедневный вызов', 'Provocarea zilei', 'Tägliche Herausforderung')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1377, 'daily_challenge_sub', '{{questions}} questions, the same for everyone · {{players}} played · new in {{left}}', 'ACTIVE', SYSDATE, '{{questions}} questions, the same for everyone · {{players}} played · new in {{left}}', '{{questions}} вопросов, одинаковых для всех · сыграли: {{players}} · новый через {{left}}', '{{questions}} întrebări, aceleași pentru toți · {{players}} au jucat · nouă în {{left}}', '{{questions}} Fragen, für alle gleich · {{players}} gespielt · neu in {{left}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1378, 'daily_flawless_run', 'Finish a quiz with no mistakes', 'ACTIVE', SYSDATE, 'Finish a quiz with no mistakes', 'Пройди викторину без ошибок', 'Termină un quiz fără nicio greșeală', 'Beende ein Quiz ohne Fehler')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1379, 'daily_nobody', 'Nobody has played yet today. Be the first.', 'ACTIVE', SYSDATE, 'Nobody has played yet today. Be the first.', 'Сегодня ещё никто не играл. Будь первым.', 'Nimeni n-a jucat încă azi. Fii primul.', 'Heute hat noch niemand gespielt. Sei der Erste.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1380, 'daily_play', 'Play today''s challenge', 'ACTIVE', SYSDATE, 'Play today''s challenge', 'Сыграть сегодняшний вызов', 'Joacă provocarea de azi', 'Heutige Herausforderung spielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1381, 'daily_play_quizzes', 'Finish {{target}} quizzes', 'ACTIVE', SYSDATE, 'Finish {{target}} quizzes', 'Пройди викторин: {{target}}', 'Termină {{target}} quizuri', 'Beende {{target}} Quiz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1382, 'daily_rank', 'You are #{{rank}} of {{players}} today', 'ACTIVE', SYSDATE, 'You are #{{rank}} of {{players}} today', 'Сегодня ты #{{rank}} из {{players}}', 'Azi ești pe locul #{{rank}} din {{players}}', 'Du bist heute #{{rank}} von {{players}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1383, 'daily_right_answers', 'Answer {{target}} questions right', 'ACTIVE', SYSDATE, 'Answer {{target}} questions right', 'Ответь правильно на вопросы: {{target}}', 'Răspunde corect la {{target}} întrebări', 'Beantworte {{target}} Fragen richtig')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1384, 'daily_table', 'See the table', 'ACTIVE', SYSDATE, 'See the table', 'Посмотреть таблицу', 'Vezi clasamentul', 'Tabelle ansehen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1385, 'daily_table_all', 'Full table', 'ACTIVE', SYSDATE, 'Full table', 'Вся таблица', 'Clasament complet', 'Ganze Tabelle')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1386, 'daily_tasks', 'Today''s tasks', 'ACTIVE', SYSDATE, 'Today''s tasks', 'Задания на сегодня', 'Sarcinile de azi', 'Heutige Aufgaben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1387, 'daily_tasks_done', '{{done}} of {{total}} done', 'ACTIVE', SYSDATE, '{{done}} of {{total}} done', 'Выполнено {{done}} из {{total}}', '{{done}} din {{total}} gata', '{{done}} von {{total}} erledigt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1388, 'decline', 'No thanks', 'ACTIVE', SYSDATE, 'No thanks', 'Нет, спасибо', 'Nu, mulțumesc', 'Nein danke')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1389, 'default_length', 'Default', 'ACTIVE', SYSDATE, 'Default', 'По умолчанию', 'Implicit', 'Standard')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1390, 'delete', 'Delete', 'ACTIVE', SYSDATE, 'Delete', 'Удалить', 'Șterge', 'Löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1391, 'delete_group', 'Delete group', 'ACTIVE', SYSDATE, 'Delete group', 'Удалить группу', 'Șterge grupul', 'Gruppe löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1392, 'delete_group_confirm', '"{{name}}" and all its messages will be deleted for every member. This can’t be undone.', 'ACTIVE', SYSDATE, '"{{name}}" and all its messages will be deleted for every member. This can’t be undone.', '«{{name}}» и все её сообщения будут удалены для всех участников. Это нельзя отменить.', '„{{name}}” și toate mesajele sale vor fi șterse pentru toți membrii. Acțiunea nu poate fi anulată.', '„{{name}}“ und alle Nachrichten darin werden für alle Mitglieder gelöscht. Das kann nicht rückgängig gemacht werden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1393, 'delete_group_title', 'Delete group?', 'ACTIVE', SYSDATE, 'Delete group?', 'Удалить группу?', 'Ștergi grupul?', 'Gruppe löschen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1394, 'delete_message', 'Delete message', 'ACTIVE', SYSDATE, 'Delete message', 'Удалить сообщение', 'Șterge mesajul', 'Nachricht löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1395, 'delete_message_confirm', 'It will be removed for everyone in this chat.', 'ACTIVE', SYSDATE, 'It will be removed for everyone in this chat.', 'Оно будет удалено для всех в этом чате.', 'Va fi șters pentru toți cei din acest chat.', 'Sie wird für alle in diesem Chat entfernt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1396, 'delete_message_title', 'Delete message?', 'ACTIVE', SYSDATE, 'Delete message?', 'Удалить сообщение?', 'Ștergi mesajul?', 'Nachricht löschen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1397, 'delete_quiz', 'Delete quiz', 'ACTIVE', SYSDATE, 'Delete quiz', 'Удалить викторину', 'Șterge quizul', 'Quiz löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1398, 'delete_quiz_confirm', 'Its questions, its invitations and everyone’s results for it will be deleted too. This cannot be undone.', 'ACTIVE', SYSDATE, 'Its questions, its invitations and everyone’s results for it will be deleted too. This cannot be undone.', 'Её вопросы, приглашения и результаты всех участников тоже будут удалены. Это нельзя отменить.', 'Întrebările, invitațiile și rezultatele tuturor pentru el vor fi șterse și ele. Acțiunea nu poate fi anulată.', 'Seine Fragen, Einladungen und alle Ergebnisse dazu werden ebenfalls gelöscht. Das kann nicht rückgängig gemacht werden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1399, 'delete_quiz_title', 'Delete this quiz?', 'ACTIVE', SYSDATE, 'Delete this quiz?', 'Удалить эту викторину?', 'Ștergi acest quiz?', 'Dieses Quiz löschen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1400, 'desktop_notifications_hint', 'Get a system notification for new messages while this tab is in the background', 'ACTIVE', SYSDATE, 'Get a system notification for new messages while this tab is in the background', 'Получай системные уведомления о новых сообщениях, пока эта вкладка в фоне', 'Primește o notificare de sistem pentru mesaje noi cât timp această filă e în fundal', 'Erhalte eine Systembenachrichtigung bei neuen Nachrichten, während dieser Tab im Hintergrund ist')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1401, 'did_you_know', 'Did you know?', 'ACTIVE', SYSDATE, 'Did you know?', 'А ты знал?', 'Știai că?', 'Wusstest du schon?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1402, 'difficulty', 'Difficulty', 'ACTIVE', SYSDATE, 'Difficulty', 'Сложность', 'Dificultate', 'Schwierigkeit')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1403, 'difficulty_easy', 'Easy', 'ACTIVE', SYSDATE, 'Easy', 'Лёгкая', 'Ușor', 'Leicht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1404, 'difficulty_hard', 'Hard', 'ACTIVE', SYSDATE, 'Hard', 'Сложная', 'Greu', 'Schwer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1405, 'difficulty_medium', 'Medium', 'ACTIVE', SYSDATE, 'Medium', 'Средняя', 'Mediu', 'Mittel')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1406, 'dock_left', 'Bottom left (default)', 'ACTIVE', SYSDATE, 'Bottom left (default)', 'Внизу слева (по умолчанию)', 'Stânga jos (implicit)', 'Unten links (Standard)')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1407, 'dock_right', 'Bottom right', 'ACTIVE', SYSDATE, 'Bottom right', 'Внизу справа', 'Dreapta jos', 'Unten rechts')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1408, 'donate', 'Donate', 'ACTIVE', SYSDATE, 'Donate', 'Поддержать', 'Donează', 'Spenden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1409, 'donate_crypto', 'Crypto', 'ACTIVE', SYSDATE, 'Crypto', 'Криптовалюта', 'Cripto', 'Krypto')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1410, 'donate_crypto_hint', 'Send only that coin, on that network, to its address: anything else is lost.', 'ACTIVE', SYSDATE, 'Send only that coin, on that network, to its address: anything else is lost.', 'Отправляй только эту монету, только в этой сети и только на её адрес: всё остальное будет потеряно.', 'Trimite doar acea monedă, pe acea rețea, la adresa ei: orice altceva se pierde.', 'Sende nur diese Coin, über dieses Netzwerk, an ihre Adresse: Alles andere geht verloren.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1411, 'donate_lead', 'Play Quiz is free and has no ads. If you enjoy it, a donation keeps the servers running and new questions coming.', 'ACTIVE', SYSDATE, 'Play Quiz is free and has no ads. If you enjoy it, a donation keeps the servers running and new questions coming.', 'Play Quiz бесплатный и без рекламы. Если тебе нравится, пожертвование помогает серверам работать, а новым вопросам — появляться.', 'Play Quiz este gratuit și fără reclame. Dacă îți place, o donație ține serverele pornite și aduce întrebări noi.', 'Play Quiz ist kostenlos und werbefrei. Wenn es dir gefällt, hält eine Spende die Server am Laufen und sorgt für neue Fragen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1412, 'donate_network', 'Network: {{network}}', 'ACTIVE', SYSDATE, 'Network: {{network}}', 'Сеть: {{network}}', 'Rețea: {{network}}', 'Netzwerk: {{network}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1413, 'donate_none', 'Donations are not set up yet. Thank you for thinking of it!', 'ACTIVE', SYSDATE, 'Donations are not set up yet. Thank you for thinking of it!', 'Пожертвования пока не настроены. Спасибо, что подумал об этом!', 'Donațiile nu sunt încă configurate. Mulțumim că te-ai gândit la asta!', 'Spenden sind noch nicht eingerichtet. Danke, dass du daran denkst!')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1414, 'donate_paypal_email', 'Or send to', 'ACTIVE', SYSDATE, 'Or send to', 'Или отправь на', 'Sau trimite la', 'Oder sende an')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1415, 'donate_with_paypal', 'Donate with PayPal', 'ACTIVE', SYSDATE, 'Donate with PayPal', 'Поддержать через PayPal', 'Donează cu PayPal', 'Mit PayPal spenden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1416, 'drag_pairs_hint', 'Match each one with its pair.', 'ACTIVE', SYSDATE, 'Match each one with its pair.', 'Сопоставь каждый элемент с его парой.', 'Potrivește fiecare element cu perechea sa.', 'Ordne jedes Element seinem Paar zu.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1417, 'drop_answer_here', 'Drop the answer here', 'ACTIVE', SYSDATE, 'Drop the answer here', 'Перетащи ответ сюда', 'Plasează răspunsul aici', 'Antwort hier ablegen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1418, 'duel_with', 'Duel {{name}}', 'ACTIVE', SYSDATE, 'Duel {{name}}', 'Дуэль с {{name}}', 'Duel cu {{name}}', 'Duell mit {{name}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1419, 'edit', 'Edit', 'ACTIVE', SYSDATE, 'Edit', 'Изменить', 'Editează', 'Bearbeiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1420, 'edit_message', 'Edit message', 'ACTIVE', SYSDATE, 'Edit message', 'Изменить сообщение', 'Editează mesajul', 'Nachricht bearbeiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1421, 'edited', 'edited', 'ACTIVE', SYSDATE, 'edited', 'изменено', 'editat', 'bearbeitet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1422, 'editing_message', 'Editing message', 'ACTIVE', SYSDATE, 'Editing message', 'Редактирование сообщения', 'Editezi mesajul', 'Nachricht wird bearbeitet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1423, 'email_placeholder', 'name@example.com', 'ACTIVE', SYSDATE, 'name@example.com', 'name@example.com', 'nume@example.com', 'name@example.com')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1424, 'enable_desktop_notifications', 'Enable desktop notifications', 'ACTIVE', SYSDATE, 'Enable desktop notifications', 'Включить уведомления на рабочем столе', 'Activează notificările pe desktop', 'Desktop-Benachrichtigungen aktivieren')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1425, 'error', 'Something went wrong', 'ACTIVE', SYSDATE, 'Something went wrong', 'Что-то пошло не так', 'Ceva n-a mers bine', 'Etwas ist schiefgelaufen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1426, 'experience', 'Experience', 'ACTIVE', SYSDATE, 'Experience', 'Опыт', 'Experiență', 'Erfahrung')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1427, 'extra_time', '+{{seconds}}s', 'ACTIVE', SYSDATE, '+{{seconds}}s', '+{{seconds}} с', '+{{seconds}}s', '+{{seconds}} s')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1428, 'extra_time_text', 'In a timed quiz, puts more time on the clock.', 'ACTIVE', SYSDATE, 'In a timed quiz, puts more time on the clock.', 'В викторине на время добавляет время на таймер.', 'Într-un quiz cu cronometru, adaugă timp pe ceas.', 'Gibt dir in einem Quiz auf Zeit mehr Zeit auf der Uhr.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1429, 'fact_of_the_day', 'Fact of the day', 'ACTIVE', SYSDATE, 'Fact of the day', 'Факт дня', 'Faptul zilei', 'Fakt des Tages')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1430, 'feed_challenge', '{{user}} challenges you: {{category}}', 'ACTIVE', SYSDATE, '{{user}} challenges you: {{category}}', '{{user}} бросает тебе вызов: {{category}}', '{{user}} te provoacă: {{category}}', '{{user}} fordert dich heraus: {{category}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1431, 'feed_challenge_done', '{{user}} took on your {{category}} challenge', 'ACTIVE', SYSDATE, '{{user}} took on your {{category}} challenge', '{{user}} принял твой вызов по теме {{category}}', '{{user}} a acceptat provocarea ta la {{category}}', '{{user}} hat deine Herausforderung in {{category}} angenommen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1432, 'feed_challenge_draw', '{{user}} matched your score in {{category}} exactly', 'ACTIVE', SYSDATE, '{{user}} matched your score in {{category}} exactly', '{{user}} в точности повторил твой результат в {{category}}', '{{user}} a egalat exact scorul tău la {{category}}', '{{user}} hat in {{category}} genau dein Ergebnis erreicht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1433, 'feed_challenge_lost', '{{user}} beat your score in {{category}}', 'ACTIVE', SYSDATE, '{{user}} beat your score in {{category}}', '{{user}} побил твой результат в {{category}}', '{{user}} ți-a depășit scorul la {{category}}', '{{user}} hat dein Ergebnis in {{category}} übertroffen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1434, 'feed_challenge_won', '{{user}} took on your {{category}} challenge, and you won', 'ACTIVE', SYSDATE, '{{user}} took on your {{category}} challenge, and you won', '{{user}} принял твой вызов по теме {{category}}, и ты победил', '{{user}} a acceptat provocarea ta la {{category}}, iar tu ai câștigat', '{{user}} hat deine Herausforderung in {{category}} angenommen, und du hast gewonnen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1435, 'feed_conquest_lost', '{{user}} took {{country}} from you', 'ACTIVE', SYSDATE, '{{user}} took {{country}} from you', '{{user}} отнял у тебя {{country}}', '{{user}} ți-a luat {{country}}', '{{user}} hat dir {{country}} abgenommen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1436, 'feed_conquest_round', 'Conquest round {{round}} is open: {{countries}}', 'ACTIVE', SYSDATE, 'Conquest round {{round}} is open: {{countries}}', 'Раунд завоевания {{round}} открыт: {{countries}}', 'Runda de cucerire {{round}} a început: {{countries}}', 'Eroberungsrunde {{round}} ist offen: {{countries}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1437, 'feed_friend_conquest', '{{user}} conquered {{country}}', 'ACTIVE', SYSDATE, '{{user}} conquered {{country}}', '{{user}} завоевал {{country}}', '{{user}} a cucerit {{country}}', '{{user}} hat {{country}} erobert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1438, 'feed_friend_level', '{{name}} (level {{level}})', 'ACTIVE', SYSDATE, '{{name}} (level {{level}})', '{{name}} (уровень {{level}})', '{{name}} (nivelul {{level}})', '{{name}} (Level {{level}})')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1439, 'feed_friend_levels', 'Friends levelled up: {{list}}', 'ACTIVE', SYSDATE, 'Friends levelled up: {{list}}', 'Друзья повысили уровень: {{list}}', 'Prieteni care au avansat în nivel: {{list}}', 'Freunde mit Level-up: {{list}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1440, 'feed_friend_post', '{{user}}: {{title}}', 'ACTIVE', SYSDATE, '{{user}}: {{title}}', '{{user}}: {{title}}', '{{user}}: {{title}}', '{{user}}: {{title}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1441, 'feed_own_post', 'You: {{title}}', 'ACTIVE', SYSDATE, 'You: {{title}}', 'Ты: {{title}}', 'Tu: {{title}}', 'Du: {{title}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1442, 'feed_questions_added', 'New questions in {{category}}: {{count}}', 'ACTIVE', SYSDATE, 'New questions in {{category}}: {{count}}', 'Новые вопросы в {{category}}: {{count}}', 'Întrebări noi la {{category}}: {{count}}', 'Neue Fragen in {{category}}: {{count}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1443, 'feed_start_quiz', 'A quiz on {{category}} starts as soon as you continue, and its time counts from there.', 'ACTIVE', SYSDATE, 'A quiz on {{category}} starts as soon as you continue, and its time counts from there.', 'Викторина по теме {{category}} начнётся, как только ты продолжишь, и время пойдёт с этого момента.', 'Un quiz la {{category}} începe imediat ce continui, iar timpul curge de atunci.', 'Ein Quiz zu {{category}} startet, sobald du fortfährst, und die Zeit läuft ab dann.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1444, 'feed_start_quiz_title', 'Start a quiz?', 'ACTIVE', SYSDATE, 'Start a quiz?', 'Начать викторину?', 'Începi un quiz?', 'Quiz starten?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1445, 'feed_trophy', 'New trophy: {{title}}', 'ACTIVE', SYSDATE, 'New trophy: {{title}}', 'Новый трофей: {{title}}', 'Trofeu nou: {{title}}', 'Neue Trophäe: {{title}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1446, 'feed_wiki_article', 'New in {{category}} on the wiki: {{title}}', 'ACTIVE', SYSDATE, 'New in {{category}} on the wiki: {{title}}', 'Новое в вики по теме {{category}}: {{title}}', 'Nou pe wiki la {{category}}: {{title}}', 'Neu im Wiki zu {{category}}: {{title}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1447, 'feedback_bug', 'Bug', 'ACTIVE', SYSDATE, 'Bug', 'Ошибка', 'Eroare', 'Fehler')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1448, 'feedback_bug_placeholder', 'What happened, and what did you expect?', 'ACTIVE', SYSDATE, 'What happened, and what did you expect?', 'Что произошло и чего ты ожидал?', 'Ce s-a întâmplat și ce te așteptai să se întâmple?', 'Was ist passiert und was hast du erwartet?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1449, 'feedback_contact', 'Your email, if you would like an answer (optional)', 'ACTIVE', SYSDATE, 'Your email, if you would like an answer (optional)', 'Твой email, если хочешь получить ответ (необязательно)', 'Emailul tău, dacă vrei un răspuns (opțional)', 'Deine E-Mail, falls du eine Antwort möchtest (optional)')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1450, 'feedback_intro', 'Found a bug, have a question or an idea? Tell us.', 'ACTIVE', SYSDATE, 'Found a bug, have a question or an idea? Tell us.', 'Нашёл ошибку, есть вопрос или идея? Расскажи нам.', 'Ai găsit o eroare, ai o întrebare sau o idee? Spune-ne.', 'Einen Fehler gefunden, eine Frage oder eine Idee? Sag es uns.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1451, 'feedback_message', 'Your message', 'ACTIVE', SYSDATE, 'Your message', 'Твоё сообщение', 'Mesajul tău', 'Deine Nachricht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1452, 'feedback_other', 'Other', 'ACTIVE', SYSDATE, 'Other', 'Другое', 'Altceva', 'Sonstiges')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1453, 'feedback_placeholder', 'Your message', 'ACTIVE', SYSDATE, 'Your message', 'Твоё сообщение', 'Mesajul tău', 'Deine Nachricht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1454, 'feedback_question', 'Question', 'ACTIVE', SYSDATE, 'Question', 'Вопрос', 'Întrebare', 'Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1455, 'feedback_sent', 'Thanks! Your message is with the admins.', 'ACTIVE', SYSDATE, 'Thanks! Your message is with the admins.', 'Спасибо! Твоё сообщение передано администраторам.', 'Mulțumim! Mesajul tău a ajuns la administratori.', 'Danke! Deine Nachricht ist bei den Admins angekommen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1456, 'feedback_suggestion', 'Suggestion', 'ACTIVE', SYSDATE, 'Suggestion', 'Предложение', 'Sugestie', 'Vorschlag')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1457, 'feedback_title', 'Message the admins', 'ACTIVE', SYSDATE, 'Message the admins', 'Написать администраторам', 'Scrie administratorilor', 'Den Admins schreiben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1458, 'feedback_type', 'What is it about?', 'ACTIVE', SYSDATE, 'What is it about?', 'О чём это?', 'Despre ce este vorba?', 'Worum geht es?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1459, 'find_people', 'Find people', 'ACTIVE', SYSDATE, 'Find people', 'Найти людей', 'Găsește persoane', 'Leute finden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1460, 'footer_map_data', 'Map data', 'ACTIVE', SYSDATE, 'Map data', 'Картографические данные', 'Date hartă', 'Kartendaten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1461, 'footer_navigation', 'Footer', 'ACTIVE', SYSDATE, 'Footer', 'Подвал сайта', 'Subsol', 'Fußzeile')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1462, 'footer_tagline', 'Learn something new every day, test yourself and challenge your friends.', 'ACTIVE', SYSDATE, 'Learn something new every day, test yourself and challenge your friends.', 'Узнавай что-то новое каждый день, проверяй себя и бросай вызов друзьям.', 'Învață ceva nou în fiecare zi, testează-te și provoacă-ți prietenii.', 'Lerne jeden Tag etwas Neues, teste dich selbst und fordere deine Freunde heraus.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1463, 'friends', 'Friends', 'ACTIVE', SYSDATE, 'Friends', 'Друзья', 'Prieteni', 'Freunde')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1464, 'friends_news', 'Friends', 'ACTIVE', SYSDATE, 'Friends', 'Друзья', 'Prieteni', 'Freunde')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1465, 'friends_panel', 'Friends panel', 'ACTIVE', SYSDATE, 'Friends panel', 'Панель друзей', 'Panoul de prieteni', 'Freundesleiste')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1466, 'friends_panel_hint', 'Which corner your friends list sits in. Notifications pop up in the other one.', 'ACTIVE', SYSDATE, 'Which corner your friends list sits in. Notifications pop up in the other one.', 'В каком углу находится список друзей. Уведомления появляются в другом.', 'În ce colț stă lista de prieteni. Notificările apar în celălalt.', 'In welcher Ecke deine Freundesliste sitzt. Benachrichtigungen erscheinen in der anderen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1467, 'group_board_meta', '{{quizzes}} quizzes · {{accuracy}}%', 'ACTIVE', SYSDATE, '{{quizzes}} quizzes · {{accuracy}}%', 'Викторин: {{quizzes}} · {{accuracy}}%', '{{quizzes}} quizuri · {{accuracy}}%', '{{quizzes}} Quiz · {{accuracy}} %')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1468, 'group_board_note', 'Ranked by right answers. Custom quizzes do not count.', 'ACTIVE', SYSDATE, 'Ranked by right answers. Custom quizzes do not count.', 'Рейтинг по правильным ответам. Собственные викторины не учитываются.', 'Clasament după răspunsuri corecte. Quizurile personalizate nu se pun la socoteală.', 'Sortiert nach richtigen Antworten. Eigene Quiz zählen nicht.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1469, 'group_deleted', 'Group deleted', 'ACTIVE', SYSDATE, 'Group deleted', 'Группа удалена', 'Grup șters', 'Gruppe gelöscht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1470, 'group_leaderboard', 'Group leaderboard', 'ACTIVE', SYSDATE, 'Group leaderboard', 'Рейтинг группы', 'Clasamentul grupului', 'Gruppen-Rangliste')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1471, 'group_name', 'Group name', 'ACTIVE', SYSDATE, 'Group name', 'Название группы', 'Numele grupului', 'Gruppenname')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1472, 'group_play_live', 'Play live together', 'ACTIVE', SYSDATE, 'Play live together', 'Играть вместе вживую', 'Joacă live împreună', 'Live zusammen spielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1473, 'have_account', 'Already have an account?', 'ACTIVE', SYSDATE, 'Already have an account?', 'Уже есть аккаунт?', 'Ai deja un cont?', 'Du hast schon ein Konto?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1474, 'hide_password', 'Hide password', 'ACTIVE', SYSDATE, 'Hide password', 'Скрыть пароль', 'Ascunde parola', 'Passwort verbergen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1475, 'hint_fifty_fifty', '50/50', 'ACTIVE', SYSDATE, '50/50', '50/50', '50/50', '50/50')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1476, 'hint_text', 'In a quiz, takes away all the wrong options but one. Not in Conquest or challenges.', 'ACTIVE', SYSDATE, 'In a quiz, takes away all the wrong options but one. Not in Conquest or challenges.', 'В викторине убирает все неверные варианты, кроме одного. Не работает в завоевании и вызовах.', 'Într-un quiz, elimină toate variantele greșite, cu excepția uneia. Nu funcționează în Cucerire sau în provocări.', 'Entfernt in einem Quiz alle falschen Antworten bis auf eine. Nicht in Eroberung oder Herausforderungen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1477, 'home_friends', 'Friends'' news on the home page', 'ACTIVE', SYSDATE, 'Friends'' news on the home page', 'Новости друзей на главной', 'Noutățile prietenilor pe pagina principală', 'Freunde-News auf der Startseite')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1478, 'home_friends_below', 'Under the news', 'ACTIVE', SYSDATE, 'Under the news', 'Под новостями', 'Sub noutăți', 'Unter den News')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1479, 'home_friends_left', 'Left of the news', 'ACTIVE', SYSDATE, 'Left of the news', 'Слева от новостей', 'În stânga noutăților', 'Links neben den News')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1480, 'home_friends_right', 'Right of the news (default)', 'ACTIVE', SYSDATE, 'Right of the news (default)', 'Справа от новостей (по умолчанию)', 'În dreapta noutăților (implicit)', 'Rechts neben den News (Standard)')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1481, 'home_invitations', 'Waiting for you', 'ACTIVE', SYSDATE, 'Waiting for you', 'Тебя ждут', 'Te așteaptă', 'Warten auf dich')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1482, 'home_invitations_all', 'All invitations', 'ACTIVE', SYSDATE, 'All invitations', 'Все приглашения', 'Toate invitațiile', 'Alle Einladungen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1483, 'home_invitations_count', '{{count}} quiz invitations', 'ACTIVE', SYSDATE, '{{count}} quiz invitations', 'Приглашений в викторины: {{count}}', '{{count}} invitații la quiz', '{{count}} Quiz-Einladungen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1484, 'home_link_label', 'Play Quiz home', 'ACTIVE', SYSDATE, 'Play Quiz home', 'Главная Play Quiz', 'Pagina principală Play Quiz', 'Play Quiz Startseite')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1485, 'home_quote', 'A smarter you starts with a single question', 'ACTIVE', SYSDATE, 'A smarter you starts with a single question', 'Путь к тому, чтобы стать умнее, начинается с одного вопроса', 'Un tu mai deștept începe cu o singură întrebare', 'Ein klügeres Du beginnt mit einer einzigen Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1486, 'home_recent', 'Pick up where you left off', 'ACTIVE', SYSDATE, 'Pick up where you left off', 'Продолжи с того места, где остановился', 'Continuă de unde ai rămas', 'Mach da weiter, wo du aufgehört hast')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1487, 'home_recent_sub', 'Your last quizzes', 'ACTIVE', SYSDATE, 'Your last quizzes', 'Твои последние викторины', 'Ultimele tale quizuri', 'Deine letzten Quiz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1488, 'home_weak_practice', 'Practise it', 'ACTIVE', SYSDATE, 'Practise it', 'Потренироваться', 'Exersează', 'Üben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1489, 'home_weak_right', 'right, of {{count}} questions', 'ACTIVE', SYSDATE, 'right, of {{count}} questions', 'верно, из {{count}} вопросов', 'corecte, din {{count}} întrebări', 'richtig, von {{count}} Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1490, 'home_weak_spot', 'Your weak spot', 'ACTIVE', SYSDATE, 'Your weak spot', 'Твоё слабое место', 'Punctul tău slab', 'Deine Schwachstelle')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1491, 'home_weak_spot_sub', 'This month, by how often you are right', 'ACTIVE', SYSDATE, 'This month, by how often you are right', 'За этот месяц, по доле правильных ответов', 'Luna aceasta, după cât de des răspunzi corect', 'Diesen Monat, nach deiner Trefferquote')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1492, 'how_it_works', 'How it works', 'ACTIVE', SYSDATE, 'How it works', 'Как это работает', 'Cum funcționează', 'So funktioniert''s')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1493, 'in_order_hint', 'Put them in order, smallest first.', 'ACTIVE', SYSDATE, 'Put them in order, smallest first.', 'Расставь по порядку, начиная с наименьшего.', 'Pune-le în ordine, de la cel mai mic.', 'Bring sie in die richtige Reihenfolge, das Kleinste zuerst.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1494, 'invalid_email', 'Invalid email address', 'ACTIVE', SYSDATE, 'Invalid email address', 'Неверный адрес электронной почты', 'Adresă de email invalidă', 'Ungültige E-Mail-Adresse')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1495, 'invite_a_group', 'Invite a group', 'ACTIVE', SYSDATE, 'Invite a group', 'Пригласить группу', 'Invită un grup', 'Gruppe einladen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1496, 'invite_players', 'Invite players', 'ACTIVE', SYSDATE, 'Invite players', 'Пригласить игроков', 'Invită jucători', 'Spieler einladen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1497, 'invited_by', 'From {{name}}', 'ACTIVE', SYSDATE, 'From {{name}}', 'От {{name}}', 'De la {{name}}', 'Von {{name}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1498, 'invited_count', '{{count}} invited', 'ACTIVE', SYSDATE, '{{count}} invited', 'Приглашено: {{count}}', '{{count}} invitați', '{{count}} eingeladen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1499, 'iq_answered', 'Questions', 'ACTIVE', SYSDATE, 'Questions', 'Вопросы', 'Întrebări', 'Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1500, 'iq_begin', 'Begin the test', 'ACTIVE', SYSDATE, 'Begin the test', 'Начать тест', 'Începe testul', 'Test starten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1501, 'iq_disclaimer', 'This is a self-administered test, not a clinical assessment: nobody is supervising, and the scale is built from how this app''s players score. Treat it as a good estimate, not a diagnosis.', 'ACTIVE', SYSDATE, 'This is a self-administered test, not a clinical assessment: nobody is supervising, and the scale is built from how this app''s players score. Treat it as a good estimate, not a diagnosis.', 'Это тест для самостоятельного прохождения, а не клиническая оценка: никто за тобой не наблюдает, а шкала построена на результатах игроков этого приложения. Считай результат хорошей оценкой, а не диагнозом.', 'Acesta este un test auto-administrat, nu o evaluare clinică: nu te supraveghează nimeni, iar scala este construită din scorurile jucătorilor acestei aplicații. Privește-l ca pe o estimare bună, nu ca pe un diagnostic.', 'Das ist ein Selbsttest, keine klinische Untersuchung: Niemand beaufsichtigt dich, und die Skala basiert auf den Ergebnissen der Spieler dieser App. Sieh es als gute Schätzung, nicht als Diagnose.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1502, 'iq_facts_error', 'points: the range your score is reported with', 'ACTIVE', SYSDATE, 'points: the range your score is reported with', 'баллов: диапазон, в котором указывается твой результат', 'puncte: intervalul în care este raportat scorul tău', 'Punkte: die Spanne, mit der dein Ergebnis angegeben wird')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1503, 'iq_facts_items', 'questions, ending once your score is settled', 'ACTIVE', SYSDATE, 'questions, ending once your score is settled', 'вопросов, тест заканчивается, когда результат определён', 'întrebări, testul se încheie când scorul tău s-a stabilizat', 'Fragen, der Test endet, sobald dein Ergebnis feststeht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1504, 'iq_facts_minutes', 'in total, in one sitting', 'ACTIVE', SYSDATE, 'in total, in one sitting', 'всего, за один подход', 'în total, dintr-o singură ședință', 'insgesamt, an einem Stück')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1505, 'iq_facts_minutes_unit', 'min', 'ACTIVE', SYSDATE, 'min', 'мин', 'min', 'Min.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1506, 'iq_facts_time', 'per question, timed by the server', 'ACTIVE', SYSDATE, 'per question, timed by the server', 'на вопрос, время отсчитывает сервер', 'pe întrebare, cronometrat de server', 'pro Frage, vom Server gemessen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1507, 'iq_intro', 'Figures, patterns and number series — no general knowledge, no language. The questions are picked as you go: get one right and the next is harder, get one wrong and it is easier, so the test finds your level instead of walking everyone through the same list.', 'ACTIVE', SYSDATE, 'Figures, patterns and number series — no general knowledge, no language. The questions are picked as you go: get one right and the next is harder, get one wrong and it is easier, so the test finds your level instead of walking everyone through the same list.', 'Фигуры, закономерности и числовые ряды — без общих знаний и без языка. Вопросы подбираются по ходу: ответишь верно — следующий будет сложнее, ошибёшься — проще. Так тест находит твой уровень, а не гоняет всех по одному и тому же списку.', 'Figuri, tipare și serii de numere — fără cultură generală, fără limbaj. Întrebările sunt alese pe parcurs: răspunzi corect și următoarea e mai grea, greșești și e mai ușoară, așa că testul îți găsește nivelul în loc să-i treacă pe toți prin aceeași listă.', 'Figuren, Muster und Zahlenreihen — kein Allgemeinwissen, keine Sprache. Die Fragen werden unterwegs ausgewählt: Beantwortest du eine richtig, wird die nächste schwerer, liegst du falsch, wird sie leichter. So findet der Test dein Niveau, statt alle durch dieselbe Liste zu schicken.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1508, 'iq_matrix_alt', 'A grid of figures with one missing', 'ACTIVE', SYSDATE, 'A grid of figures with one missing', 'Сетка фигур, в которой не хватает одной', 'O grilă de figuri din care lipsește una', 'Ein Raster aus Figuren, in dem eine fehlt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1509, 'iq_no_going_back', 'Each question is timed and there is no going back. If you cannot see it, guess and move on.', 'ACTIVE', SYSDATE, 'Each question is timed and there is no going back. If you cannot see it, guess and move on.', 'На каждый вопрос отведено время, и вернуться назад нельзя. Если не видишь ответа, угадай и иди дальше.', 'Fiecare întrebare este cronometrată și nu te poți întoarce. Dacă nu vezi răspunsul, ghicește și mergi mai departe.', 'Jede Frage hat ein Zeitlimit, und es gibt kein Zurück. Wenn du es nicht siehst, rate und mach weiter.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1510, 'iq_not_taken', 'Not taken yet.', 'ACTIVE', SYSDATE, 'Not taken yet.', 'Ещё не пройден.', 'Nesusținut încă.', 'Noch nicht gemacht.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1511, 'iq_percentile', 'Percentile', 'ACTIVE', SYSDATE, 'Percentile', 'Процентиль', 'Percentilă', 'Perzentil')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1512, 'iq_percentile_value', 'Above {{percentile}}% of takers', 'ACTIVE', SYSDATE, 'Above {{percentile}}% of takers', 'Лучше, чем {{percentile}}% прошедших', 'Peste {{percentile}}% dintre participanți', 'Besser als {{percentile}} % der Teilnehmer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1513, 'iq_previous', 'Last time you scored {{iq}}.', 'ACTIVE', SYSDATE, 'Last time you scored {{iq}}.', 'В прошлый раз ты набрал {{iq}}.', 'Data trecută ai obținut {{iq}}.', 'Letztes Mal hattest du {{iq}}.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1514, 'iq_previous_note', 'A second go is always flattered by knowing the questions, so the first one is the one that counts.', 'ACTIVE', SYSDATE, 'A second go is always flattered by knowing the questions, so the first one is the one that counts.', 'Вторая попытка всегда выигрывает от знакомства с вопросами, поэтому в зачёт идёт первая.', 'A doua încercare e mereu avantajată de faptul că știi întrebările, așa că prima este cea care contează.', 'Ein zweiter Versuch profitiert immer davon, dass du die Fragen kennst, deshalb zählt der erste.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1515, 'iq_profile_empty', 'Twenty minutes of figures and patterns, scored on the usual scale.', 'ACTIVE', SYSDATE, 'Twenty minutes of figures and patterns, scored on the usual scale.', 'Двадцать минут фигур и закономерностей с оценкой по привычной шкале.', 'Douăzeci de minute de figuri și tipare, punctate pe scala obișnuită.', 'Zwanzig Minuten Figuren und Muster, bewertet auf der üblichen Skala.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1516, 'iq_prompt_analogy', 'The first figure changes into the second. Change the third the same way.', 'ACTIVE', SYSDATE, 'The first figure changes into the second. Change the third the same way.', 'Первая фигура превращается во вторую. Измени третью так же.', 'Prima figură se transformă în a doua. Transformă-o pe a treia la fel.', 'Die erste Figur verwandelt sich in die zweite. Verändere die dritte genauso.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1517, 'iq_prompt_matrix', 'Which figure completes the grid?', 'ACTIVE', SYSDATE, 'Which figure completes the grid?', 'Какая фигура дополняет сетку?', 'Ce figură completează grila?', 'Welche Figur vervollständigt das Raster?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1518, 'iq_prompt_odd', 'Four of these belong together. Which one does not?', 'ACTIVE', SYSDATE, 'Four of these belong together. Which one does not?', 'Четыре из них связаны между собой. Какая лишняя?', 'Patru dintre acestea se potrivesc. Care nu?', 'Vier davon gehören zusammen. Welche nicht?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1519, 'iq_prompt_series', 'Which number comes next?', 'ACTIVE', SYSDATE, 'Which number comes next?', 'Какое число следующее?', 'Ce număr urmează?', 'Welche Zahl kommt als Nächstes?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1520, 'iq_provisional_short', 'provisional scale', 'ACTIVE', SYSDATE, 'provisional scale', 'предварительная шкала', 'scală provizorie', 'vorläufige Skala')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1521, 'iq_question_of', 'Question {{number}} of {{total}}', 'ACTIVE', SYSDATE, 'Question {{number}} of {{total}}', 'Вопрос {{number}} из {{total}}', 'Întrebarea {{number}} din {{total}}', 'Frage {{number}} von {{total}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1522, 'iq_range', 'Most likely between {{low}} and {{high}}', 'ACTIVE', SYSDATE, 'Most likely between {{low}} and {{high}}', 'Скорее всего, от {{low}} до {{high}}', 'Cel mai probabil între {{low}} și {{high}}', 'Höchstwahrscheinlich zwischen {{low}} und {{high}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1523, 'iq_range_short', '{{low}}–{{high}}', 'ACTIVE', SYSDATE, '{{low}}–{{high}}', '{{low}}–{{high}}', '{{low}}–{{high}}', '{{low}}–{{high}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1524, 'iq_repeat_note', 'This was go number {{number}}. Repeat attempts run high — the questions are the same bank.', 'ACTIVE', SYSDATE, 'This was go number {{number}}. Repeat attempts run high — the questions are the same bank.', 'Это была попытка номер {{number}}. Повторные попытки дают завышенный результат — вопросы берутся из того же банка.', 'Aceasta a fost încercarea numărul {{number}}. Încercările repetate ies mai mari — întrebările vin din aceeași bază.', 'Das war Versuch Nummer {{number}}. Wiederholte Versuche fallen höher aus — die Fragen stammen aus demselben Pool.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1525, 'iq_result_note', 'The range is the honest part of the number. A test this length pins a score down to about five points of standard error, which is the ten-point range above — so 108 and 112 are the same result. Scores are quoted the usual way, with 100 in the middle and two thirds of people between 85 and 115.', 'ACTIVE', SYSDATE, 'The range is the honest part of the number. A test this length pins a score down to about five points of standard error, which is the ten-point range above — so 108 and 112 are the same result. Scores are quoted the usual way, with 100 in the middle and two thirds of people between 85 and 115.', 'Диапазон — честная часть этого числа. Тест такой длины определяет результат с точностью примерно до пяти баллов стандартной ошибки, что и даёт десятибалльный диапазон выше, — так что 108 и 112 означают одно и то же. Результаты указываются привычным образом: 100 — середина, а две трети людей находятся между 85 и 115.', 'Intervalul este partea onestă a numărului. Un test de lungimea aceasta fixează un scor la aproximativ cinci puncte de eroare standard, adică intervalul de zece puncte de mai sus — deci 108 și 112 sunt același rezultat. Scorurile sunt date în mod obișnuit, cu 100 la mijloc și două treimi dintre oameni între 85 și 115.', 'Die Spanne ist der ehrliche Teil der Zahl. Ein Test dieser Länge bestimmt ein Ergebnis auf etwa fünf Punkte Standardfehler genau, das ist die Zehn-Punkte-Spanne oben — 108 und 112 sind also dasselbe Ergebnis. Die Werte werden wie üblich angegeben: 100 in der Mitte und zwei Drittel der Menschen zwischen 85 und 115.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1526, 'iq_retake', 'Take it again', 'ACTIVE', SYSDATE, 'Take it again', 'Пройти снова', 'Dă-l din nou', 'Nochmal machen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1527, 'iq_scale', 'Scale', 'ACTIVE', SYSDATE, 'Scale', 'Шкала', 'Scală', 'Skala')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1528, 'iq_scale_normed', 'Normed on this app''s players', 'ACTIVE', SYSDATE, 'Normed on this app''s players', 'Нормирована по игрокам этого приложения', 'Normată pe jucătorii acestei aplicații', 'Normiert an den Spielern dieser App')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1529, 'iq_scale_provisional', 'Provisional — too few tests to norm on yet', 'ACTIVE', SYSDATE, 'Provisional — too few tests to norm on yet', 'Предварительная — пока слишком мало тестов для нормирования', 'Provizorie — încă prea puține teste pentru normare', 'Vorläufig — noch zu wenige Tests für eine Normierung')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1530, 'iq_sign_in', 'Sign in to take the test — the result is kept on your account.', 'ACTIVE', SYSDATE, 'Sign in to take the test — the result is kept on your account.', 'Войди, чтобы пройти тест — результат сохранится в твоём аккаунте.', 'Autentifică-te ca să dai testul — rezultatul rămâne în contul tău.', 'Melde dich an, um den Test zu machen — das Ergebnis wird in deinem Konto gespeichert.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1531, 'iq_sign_in_short', 'Log in to take the IQ test', 'ACTIVE', SYSDATE, 'Log in to take the IQ test', 'Войди, чтобы пройти IQ-тест', 'Autentifică-te pentru testul IQ', 'Melde dich für den IQ-Test an')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1532, 'iq_test', 'IQ test', 'ACTIVE', SYSDATE, 'IQ test', 'IQ-тест', 'Test IQ', 'IQ-Test')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1533, 'iq_your_score', 'Your score', 'ACTIVE', SYSDATE, 'Your score', 'Твой результат', 'Scorul tău', 'Dein Ergebnis')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1534, 'item_number', 'Item {{number}}', 'ACTIVE', SYSDATE, 'Item {{number}}', 'Элемент {{number}}', 'Elementul {{number}}', 'Element {{number}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1535, 'item_placeholder', 'e.g. Bronze Age', 'ACTIVE', SYSDATE, 'e.g. Bronze Age', 'напр. бронзовый век', 'ex. Epoca bronzului', 'z. B. Bronzezeit')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1536, 'items_in_order', 'Items, in the right order', 'ACTIVE', SYSDATE, 'Items, in the right order', 'Элементы в правильном порядке', 'Elemente, în ordinea corectă', 'Elemente in der richtigen Reihenfolge')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1537, 'join', 'Join', 'ACTIVE', SYSDATE, 'Join', 'Присоединиться', 'Alătură-te', 'Beitreten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1538, 'kb_back', 'Back to the Wiki', 'ACTIVE', SYSDATE, 'Back to the Wiki', 'Назад в вики', 'Înapoi la Wiki', 'Zurück zum Wiki')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1539, 'kb_empty', 'No articles yet. Check back soon.', 'ACTIVE', SYSDATE, 'No articles yet. Check back soon.', 'Статей пока нет. Загляни позже.', 'Încă nu există articole. Revino curând.', 'Noch keine Artikel. Schau bald wieder vorbei.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1540, 'kb_helpful', 'Was this article helpful?', 'ACTIVE', SYSDATE, 'Was this article helpful?', 'Эта статья была полезной?', 'Ți-a fost util acest articol?', 'War dieser Artikel hilfreich?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1541, 'kb_in_this_topic', 'In this topic', 'ACTIVE', SYSDATE, 'In this topic', 'В этой теме', 'În acest subiect', 'In diesem Thema')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1542, 'kb_no_results', 'No articles match “{{query}}”.', 'ACTIVE', SYSDATE, 'No articles match “{{query}}”.', 'Нет статей по запросу «{{query}}».', 'Niciun articol nu se potrivește cu „{{query}}”.', 'Keine Artikel passen zu „{{query}}“.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1543, 'kb_not_found', 'This article does not exist or is no longer published.', 'ACTIVE', SYSDATE, 'This article does not exist or is no longer published.', 'Эта статья не существует или больше не опубликована.', 'Acest articol nu există sau nu mai este publicat.', 'Dieser Artikel existiert nicht oder ist nicht mehr veröffentlicht.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1544, 'kb_search_placeholder', 'Search articles, topics or tags…', 'ACTIVE', SYSDATE, 'Search articles, topics or tags…', 'Ищи статьи, темы или теги…', 'Caută articole, subiecte sau etichete…', 'Artikel, Themen oder Tags suchen…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1545, 'kb_subtitle', 'Guides, explanations and tips to sharpen your quiz game.', 'ACTIVE', SYSDATE, 'Guides, explanations and tips to sharpen your quiz game.', 'Руководства, объяснения и советы, чтобы прокачать твою игру в викторины.', 'Ghiduri, explicații și sfaturi ca să-ți perfecționezi jocul la quiz.', 'Anleitungen, Erklärungen und Tipps, um dein Quiz-Spiel zu verbessern.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1546, 'kb_tag_search', 'Articles tagged “{{tag}}”', 'ACTIVE', SYSDATE, 'Articles tagged “{{tag}}”', 'Статьи с тегом «{{tag}}»', 'Articole cu eticheta „{{tag}}”', 'Artikel mit dem Tag „{{tag}}“')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1547, 'kb_thanks', 'Thanks for your feedback!', 'ACTIVE', SYSDATE, 'Thanks for your feedback!', 'Спасибо за отзыв!', 'Mulțumim pentru feedback!', 'Danke für dein Feedback!')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1548, 'kb_vote_down', 'No, it did not', 'ACTIVE', SYSDATE, 'No, it did not', 'Нет, не помогла', 'Nu, nu m-a ajutat', 'Nein, nicht wirklich')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1549, 'kb_vote_up', 'Yes, it helped', 'ACTIVE', SYSDATE, 'Yes, it helped', 'Да, помогла', 'Da, m-a ajutat', 'Ja, hat geholfen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1550, 'keep_playing', 'Keep playing', 'ACTIVE', SYSDATE, 'Keep playing', 'Продолжить игру', 'Continuă să joci', 'Weiterspielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1551, 'lb_accuracy', 'Most accurate', 'ACTIVE', SYSDATE, 'Most accurate', 'Самые точные', 'Cei mai preciși', 'Am treffsichersten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1552, 'lb_accuracy_rule', 'Players with at least {{count}} answers. Custom quizzes do not count.', 'ACTIVE', SYSDATE, 'Players with at least {{count}} answers. Custom quizzes do not count.', 'Игроки, у которых не меньше {{count}} ответов. Собственные викторины не учитываются.', 'Jucători cu cel puțin {{count}} răspunsuri. Quizurile personalizate nu se pun la socoteală.', 'Spieler mit mindestens {{count}} Antworten. Eigene Quiz zählen nicht.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1553, 'lb_accuracy_unit', 'of {{count}} answers', 'ACTIVE', SYSDATE, 'of {{count}} answers', 'из {{count}} ответов', 'din {{count}} răspunsuri', 'von {{count}} Antworten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1554, 'lb_empty', 'Nobody on this table yet. Play a quiz and be the first.', 'ACTIVE', SYSDATE, 'Nobody on this table yet. Play a quiz and be the first.', 'В этой таблице пока никого нет. Сыграй викторину и стань первым.', 'Nimeni în acest clasament încă. Joacă un quiz și fii primul.', 'Noch niemand in dieser Tabelle. Spiel ein Quiz und sei der Erste.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1555, 'lb_level', 'Highest level', 'ACTIVE', SYSDATE, 'Highest level', 'Самый высокий уровень', 'Cel mai mare nivel', 'Höchstes Level')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1556, 'lb_quizzes', 'Most quizzes', 'ACTIVE', SYSDATE, 'Most quizzes', 'Больше всего викторин', 'Cele mai multe quizuri', 'Die meisten Quiz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1557, 'lb_quizzes_unit', 'quizzes', 'ACTIVE', SYSDATE, 'quizzes', 'викторин', 'quizuri', 'Quiz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1558, 'lb_right', 'Most right answers', 'ACTIVE', SYSDATE, 'Most right answers', 'Больше всего верных ответов', 'Cele mai multe răspunsuri corecte', 'Die meisten richtigen Antworten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1559, 'lb_right_unit', 'of {{count}}', 'ACTIVE', SYSDATE, 'of {{count}}', 'из {{count}}', 'din {{count}}', 'von {{count}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1560, 'lb_rule', 'Custom quizzes do not count.', 'ACTIVE', SYSDATE, 'Custom quizzes do not count.', 'Собственные викторины не учитываются.', 'Quizurile personalizate nu se pun la socoteală.', 'Eigene Quiz zählen nicht.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1561, 'lb_streak', 'Longest streak', 'ACTIVE', SYSDATE, 'Longest streak', 'Самая длинная серия', 'Cea mai lungă serie', 'Längste Serie')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1562, 'lb_streak_unit', 'days in a row', 'ACTIVE', SYSDATE, 'days in a row', 'дней подряд', 'zile la rând', 'Tage in Folge')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1563, 'leaderboard', 'Leaderboard', 'ACTIVE', SYSDATE, 'Leaderboard', 'Рейтинг', 'Clasament', 'Rangliste')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1564, 'leave_quiz', 'Leave quiz', 'ACTIVE', SYSDATE, 'Leave quiz', 'Выйти из викторины', 'Părăsește quizul', 'Quiz verlassen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1565, 'leave_quiz_message', 'Your progress in this quiz will be lost and it will not be scored.', 'ACTIVE', SYSDATE, 'Your progress in this quiz will be lost and it will not be scored.', 'Твой прогресс в этой викторине будет потерян, и она не будет засчитана.', 'Progresul tău în acest quiz se va pierde și nu va fi punctat.', 'Dein Fortschritt in diesem Quiz geht verloren und es wird nicht gewertet.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1566, 'leave_quiz_title', 'Leave the quiz?', 'ACTIVE', SYSDATE, 'Leave the quiz?', 'Выйти из викторины?', 'Părăsești quizul?', 'Quiz verlassen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1567, 'length_10', '10', 'ACTIVE', SYSDATE, '10', '10', '10', '10')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1568, 'length_20', '20', 'ACTIVE', SYSDATE, '20', '20', '20', '20')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1569, 'length_5', '5', 'ACTIVE', SYSDATE, '5', '5', '5', '5')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1570, 'level_progress', 'Level {{level}} — {{into}}/{{next}} XP', 'ACTIVE', SYSDATE, 'Level {{level}} — {{into}}/{{next}} XP', 'Уровень {{level}} — {{into}}/{{next}} XP', 'Nivelul {{level}} — {{into}}/{{next}} XP', 'Level {{level}} — {{into}}/{{next}} XP')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1571, 'level_short', 'Lv {{level}}', 'ACTIVE', SYSDATE, 'Lv {{level}}', 'Ур. {{level}}', 'Niv. {{level}}', 'Lv. {{level}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1572, 'level_value', 'Level {{level}}', 'ACTIVE', SYSDATE, 'Level {{level}}', 'Уровень {{level}}', 'Nivelul {{level}}', 'Level {{level}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1573, 'link_copied', 'Link copied', 'ACTIVE', SYSDATE, 'Link copied', 'Ссылка скопирована', 'Link copiat', 'Link kopiert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1574, 'link_copy_failed', 'Could not copy the link: {{link}}', 'ACTIVE', SYSDATE, 'Could not copy the link: {{link}}', 'Не удалось скопировать ссылку: {{link}}', 'Linkul nu a putut fi copiat: {{link}}', 'Link konnte nicht kopiert werden: {{link}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1575, 'live_again', 'Play again', 'ACTIVE', SYSDATE, 'Play again', 'Сыграть ещё', 'Joacă din nou', 'Nochmal spielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1576, 'live_closed', 'The room closed.', 'ACTIVE', SYSDATE, 'The room closed.', 'Комната закрыта.', 'Camera s-a închis.', 'Der Raum wurde geschlossen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1577, 'live_closed_declined', 'Your friend said no thanks this time.', 'ACTIVE', SYSDATE, 'Your friend said no thanks this time.', 'Твой друг на этот раз отказался.', 'Prietenul tău a refuzat de data asta.', 'Dein Freund hat diesmal abgelehnt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1578, 'live_closed_expired', 'Nobody started the room, so it closed.', 'ACTIVE', SYSDATE, 'Nobody started the room, so it closed.', 'Никто не запустил комнату, поэтому она закрылась.', 'Nimeni nu a pornit camera, așa că s-a închis.', 'Niemand hat den Raum gestartet, deshalb wurde er geschlossen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1579, 'live_closed_host', 'The host closed the room.', 'ACTIVE', SYSDATE, 'The host closed the room.', 'Хост закрыл комнату.', 'Gazda a închis camera.', 'Der Host hat den Raum geschlossen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1580, 'live_closed_replaced', 'The host opened a new room instead.', 'ACTIVE', SYSDATE, 'The host opened a new room instead.', 'Хост открыл вместо неё новую комнату.', 'Gazda a deschis în schimb o cameră nouă.', 'Der Host hat stattdessen einen neuen Raum geöffnet.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1581, 'live_code', 'Room code', 'ACTIVE', SYSDATE, 'Room code', 'Код комнаты', 'Codul camerei', 'Raumcode')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1582, 'live_code_label', 'Room code', 'ACTIVE', SYSDATE, 'Room code', 'Код комнаты', 'Codul camerei', 'Raumcode')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1583, 'live_copy_link', 'Copy an invite link', 'ACTIVE', SYSDATE, 'Copy an invite link', 'Скопировать ссылку-приглашение', 'Copiază un link de invitație', 'Einladungslink kopieren')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1584, 'live_duel', 'Duel a friend', 'ACTIVE', SYSDATE, 'Duel a friend', 'Дуэль с другом', 'Duel cu un prieten', 'Duell mit einem Freund')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1585, 'live_duel_title', 'Live duel', 'ACTIVE', SYSDATE, 'Live duel', 'Живая дуэль', 'Duel live', 'Live-Duell')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1586, 'live_get_ready', 'Get ready', 'ACTIVE', SYSDATE, 'Get ready', 'Приготовься', 'Pregătește-te', 'Mach dich bereit')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1587, 'live_host', 'Host', 'ACTIVE', SYSDATE, 'Host', 'Хост', 'Gazdă', 'Host')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1588, 'live_invite_duel', 'challenges you to a live duel', 'ACTIVE', SYSDATE, 'challenges you to a live duel', 'вызывает тебя на живую дуэль', 'te provoacă la un duel live', 'fordert dich zu einem Live-Duell heraus')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1589, 'live_invite_group', 'Invite a group', 'ACTIVE', SYSDATE, 'Invite a group', 'Пригласить группу', 'Invită un grup', 'Gruppe einladen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1590, 'live_invite_room', 'invites you to a live quiz room', 'ACTIVE', SYSDATE, 'invites you to a live quiz room', 'приглашает тебя в комнату live-викторины', 'te invită într-o cameră de quiz live', 'lädt dich in einen Live-Quiz-Raum ein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1591, 'live_join', 'Join', 'ACTIVE', SYSDATE, 'Join', 'Войти', 'Intră', 'Beitreten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1592, 'live_leave', 'Leave', 'ACTIVE', SYSDATE, 'Leave', 'Выйти', 'Ieși', 'Verlassen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1593, 'live_lobby_sub', '{{questions}} questions, {{seconds}} seconds each.', 'ACTIVE', SYSDATE, '{{questions}} questions, {{seconds}} seconds each.', 'Вопросов: {{questions}}, по {{seconds}} сек. на каждый.', '{{questions}} întrebări, câte {{seconds}} secunde fiecare.', '{{questions}} Fragen, je {{seconds}} Sekunden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1594, 'live_missing', 'That room does not exist any more.', 'ACTIVE', SYSDATE, 'That room does not exist any more.', 'Этой комнаты больше нет.', 'Această cameră nu mai există.', 'Diesen Raum gibt es nicht mehr.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1595, 'live_open_room', 'Open room', 'ACTIVE', SYSDATE, 'Open room', 'Открыть комнату', 'Deschide camera', 'Raum öffnen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1596, 'live_pick_friends', 'Invite friends (optional): anyone with the code can join too.', 'ACTIVE', SYSDATE, 'Invite friends (optional): anyone with the code can join too.', 'Пригласи друзей (необязательно): войти может любой, у кого есть код.', 'Invită prieteni (opțional): oricine are codul poate intra.', 'Lade Freunde ein (optional): Jeder mit dem Code kann auch beitreten.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1597, 'live_pick_opponent', 'Who do you want to duel? Only friends on the site now see it.', 'ACTIVE', SYSDATE, 'Who do you want to duel? Only friends on the site now see it.', 'С кем хочешь сразиться? Видны только друзья, которые сейчас на сайте.', 'Cu cine vrei să te duelezi? Apar doar prietenii aflați acum pe site.', 'Wen willst du herausfordern? Nur Freunde, die gerade online sind, sehen es.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1598, 'live_play', 'Play live', 'ACTIVE', SYSDATE, 'Play live', 'Играть вживую', 'Joacă live', 'Live spielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1599, 'live_play_sub', 'Same question, same moment. Right and fast wins.', 'ACTIVE', SYSDATE, 'Same question, same moment. Right and fast wins.', 'Один вопрос, один момент. Побеждает тот, кто ответит верно и быстро.', 'Aceeași întrebare, în același moment. Câștigă cine răspunde corect și repede.', 'Gleiche Frage, gleicher Moment. Richtig und schnell gewinnt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1600, 'live_questions', 'Questions', 'ACTIVE', SYSDATE, 'Questions', 'Вопросы', 'Întrebări', 'Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1601, 'live_right', 'Right! +{{points}}', 'ACTIVE', SYSDATE, 'Right! +{{points}}', 'Верно! +{{points}}', 'Corect! +{{points}}', 'Richtig! +{{points}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1602, 'live_room', 'Open a room', 'ACTIVE', SYSDATE, 'Open a room', 'Открыть комнату', 'Deschide o cameră', 'Raum eröffnen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1603, 'live_room_title', 'Live room', 'ACTIVE', SYSDATE, 'Live room', 'Live-комната', 'Cameră live', 'Live-Raum')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1604, 'live_seconds', 'Seconds each', 'ACTIVE', SYSDATE, 'Seconds each', 'Секунд на вопрос', 'Secunde pe întrebare', 'Sekunden pro Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1605, 'live_send_duel', 'Send duel', 'ACTIVE', SYSDATE, 'Send duel', 'Вызвать на дуэль', 'Trimite duelul', 'Duell senden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1606, 'live_start', 'Start the match', 'ACTIVE', SYSDATE, 'Start the match', 'Начать матч', 'Începe meciul', 'Match starten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1607, 'live_too_slow', 'Out of time', 'ACTIVE', SYSDATE, 'Out of time', 'Время вышло', 'Timpul a expirat', 'Zeit abgelaufen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1608, 'live_wait_host', 'Waiting for the host to start…', 'ACTIVE', SYSDATE, 'Waiting for the host to start…', 'Ждём, когда хост начнёт…', 'Se așteaptă ca gazda să înceapă…', 'Warte, bis der Host startet…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1609, 'live_waiting_for', 'Invited: {{names}}', 'ACTIVE', SYSDATE, 'Invited: {{names}}', 'Приглашены: {{names}}', 'Invitați: {{names}}', 'Eingeladen: {{names}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1610, 'live_winner', '{{name}} wins', 'ACTIVE', SYSDATE, '{{name}} wins', '{{name}} побеждает', '{{name}} câștigă', '{{name}} gewinnt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1611, 'live_wrong', 'Not this time', 'ACTIVE', SYSDATE, 'Not this time', 'Не в этот раз', 'Nu de data asta', 'Diesmal nicht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1612, 'live_you_won', 'You won!', 'ACTIVE', SYSDATE, 'You won!', 'Ты победил!', 'Ai câștigat!', 'Du hast gewonnen!')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1613, 'live_your_right', '{{right}}/{{total}} right', 'ACTIVE', SYSDATE, '{{right}}/{{total}} right', 'Верно: {{right}}/{{total}}', '{{right}}/{{total}} corecte', '{{right}}/{{total}} richtig')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1614, 'load_more', 'Load more', 'ACTIVE', SYSDATE, 'Load more', 'Загрузить ещё', 'Încarcă mai mult', 'Mehr laden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1615, 'loading', 'Loading', 'ACTIVE', SYSDATE, 'Loading', 'Загрузка', 'Se încarcă', 'Lädt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1616, 'loading_map', 'Loading map…', 'ACTIVE', SYSDATE, 'Loading map…', 'Загрузка карты…', 'Se încarcă harta…', 'Karte wird geladen…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1617, 'login_subtitle', 'Welcome back! Ready for another round?', 'ACTIVE', SYSDATE, 'Welcome back! Ready for another round?', 'С возвращением! Готов к новому раунду?', 'Bine ai revenit! Gata pentru încă o rundă?', 'Willkommen zurück! Bereit für eine neue Runde?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1618, 'main_navigation', 'Main navigation', 'ACTIVE', SYSDATE, 'Main navigation', 'Главная навигация', 'Navigare principală', 'Hauptnavigation')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1619, 'map_confirm', 'Confirm {{letter}}', 'ACTIVE', SYSDATE, 'Confirm {{letter}}', 'Подтвердить {{letter}}', 'Confirmă {{letter}}', '{{letter}} bestätigen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1620, 'map_drag_hint', 'Drag to move the map', 'ACTIVE', SYSDATE, 'Drag to move the map', 'Перетаскивай, чтобы двигать карту', 'Trage pentru a muta harta', 'Ziehen, um die Karte zu bewegen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1621, 'map_option', 'Option {{letter}}', 'ACTIVE', SYSDATE, 'Option {{letter}}', 'Вариант {{letter}}', 'Varianta {{letter}}', 'Option {{letter}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1622, 'map_options', 'Answer options on the map', 'ACTIVE', SYSDATE, 'Answer options on the map', 'Варианты ответа на карте', 'Variante de răspuns pe hartă', 'Antwortoptionen auf der Karte')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1623, 'map_pick', 'Pick a place on the map', 'ACTIVE', SYSDATE, 'Pick a place on the map', 'Выбери место на карте', 'Alege un loc pe hartă', 'Wähle einen Ort auf der Karte')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1624, 'mini_quiz_right', 'Correct!', 'ACTIVE', SYSDATE, 'Correct!', 'Верно!', 'Corect!', 'Richtig!')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1625, 'mini_quiz_streak', 'Correct answers in a row', 'ACTIVE', SYSDATE, 'Correct answers in a row', 'Верных ответов подряд', 'Răspunsuri corecte la rând', 'Richtige Antworten in Folge')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1626, 'mini_quiz_wrong', 'Not quite', 'ACTIVE', SYSDATE, 'Not quite', 'Не совсем', 'Nu chiar', 'Nicht ganz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1627, 'motion', 'Motion', 'ACTIVE', SYSDATE, 'Motion', 'Анимация', 'Animație', 'Bewegung')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1628, 'motion_hint', 'Animations, page transitions and hover effects. By default the site follows your system: with its animation effects off, so is the motion here.', 'ACTIVE', SYSDATE, 'Animations, page transitions and hover effects. By default the site follows your system: with its animation effects off, so is the motion here.', 'Анимации, переходы между страницами и эффекты при наведении. По умолчанию сайт следует настройкам системы: если в ней анимации выключены, здесь тоже.', 'Animații, tranziții între pagini și efecte la trecerea cu mouse-ul. Implicit, site-ul urmează sistemul tău: dacă animațiile sunt oprite acolo, sunt oprite și aici.', 'Animationen, Seitenübergänge und Hover-Effekte. Standardmäßig folgt die Seite deinem System: Sind dort Animationen aus, sind sie es hier auch.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1629, 'motion_off', 'Never animate', 'ACTIVE', SYSDATE, 'Never animate', 'Никогда не анимировать', 'Fără animații', 'Nie animieren')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1630, 'motion_on', 'Always animate', 'ACTIVE', SYSDATE, 'Always animate', 'Всегда анимировать', 'Animații mereu', 'Immer animieren')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1631, 'motion_system', 'Follow my system (default)', 'ACTIVE', SYSDATE, 'Follow my system (default)', 'Как в системе (по умолчанию)', 'Urmează sistemul (implicit)', 'Systemeinstellung folgen (Standard)')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1632, 'move_down', 'Move {{item}} down', 'ACTIVE', SYSDATE, 'Move {{item}} down', 'Переместить {{item}} вниз', 'Mută {{item}} în jos', '{{item}} nach unten verschieben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1633, 'move_up', 'Move {{item}} up', 'ACTIVE', SYSDATE, 'Move {{item}} up', 'Переместить {{item}} вверх', 'Mută {{item}} în sus', '{{item}} nach oben verschieben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1634, 'mute_group', 'Mute notifications', 'ACTIVE', SYSDATE, 'Mute notifications', 'Отключить уведомления', 'Dezactivează notificările', 'Benachrichtigungen stummschalten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1635, 'muted', 'Muted', 'ACTIVE', SYSDATE, 'Muted', 'Без звука', 'Notificări oprite', 'Stummgeschaltet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1636, 'my_quizzes', 'My quizzes', 'ACTIVE', SYSDATE, 'My quizzes', 'Мои викторины', 'Quizurile mele', 'Meine Quiz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1637, 'my_quizzes_navigation', 'My quizzes and invitations', 'ACTIVE', SYSDATE, 'My quizzes and invitations', 'Мои викторины и приглашения', 'Quizurile mele și invitații', 'Meine Quiz und Einladungen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1638, 'my_quizzes_subtitle', 'The quizzes you made.', 'ACTIVE', SYSDATE, 'The quizzes you made.', 'Викторины, которые ты создал.', 'Quizurile create de tine.', 'Die Quiz, die du erstellt hast.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1639, 'nav_together', 'Together', 'ACTIVE', SYSDATE, 'Together', 'Вместе', 'Împreună', 'Zusammen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1640, 'navigation', 'Navigation', 'ACTIVE', SYSDATE, 'Navigation', 'Навигация', 'Navigare', 'Navigation')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1641, 'new_message', 'New message', 'ACTIVE', SYSDATE, 'New message', 'Новое сообщение', 'Mesaj nou', 'Neue Nachricht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1642, 'news', 'News', 'ACTIVE', SYSDATE, 'News', 'Новости', 'Noutăți', 'Neuigkeiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1643, 'news_all', 'All news', 'ACTIVE', SYSDATE, 'All news', 'Все новости', 'Toate noutățile', 'Alle Neuigkeiten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1644, 'news_delete', 'Delete patch note', 'ACTIVE', SYSDATE, 'Delete patch note', 'Удалить заметку об обновлении', 'Șterge nota de actualizare', 'Patchnotes löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1645, 'news_delete_confirm', '"{{title}}" will be removed from the news for everyone.', 'ACTIVE', SYSDATE, '"{{title}}" will be removed from the news for everyone.', '«{{title}}» будет удалена из новостей для всех.', '„{{title}}” va fi eliminat din noutăți pentru toată lumea.', '„{{title}}“ wird für alle aus den Neuigkeiten entfernt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1646, 'news_delete_post', 'Delete post', 'ACTIVE', SYSDATE, 'Delete post', 'Удалить пост', 'Șterge postarea', 'Beitrag löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1647, 'news_filter', 'Choose what news to show', 'ACTIVE', SYSDATE, 'Choose what news to show', 'Выбери, какие новости показывать', 'Alege ce noutăți să apară', 'Wähle, welche Neuigkeiten angezeigt werden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1648, 'news_filtered_out', 'Nothing to show with these filters.', 'ACTIVE', SYSDATE, 'Nothing to show with these filters.', 'С этими фильтрами ничего нет.', 'Nimic de afișat cu aceste filtre.', 'Mit diesen Filtern gibt es nichts anzuzeigen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1649, 'news_kind_friend_conquests', 'Friends'' conquests', 'ACTIVE', SYSDATE, 'Friends'' conquests', 'Завоевания друзей', 'Cuceririle prietenilor', 'Eroberungen von Freunden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1650, 'news_kind_friend_levels', 'Friends'' levels', 'ACTIVE', SYSDATE, 'Friends'' levels', 'Уровни друзей', 'Nivelurile prietenilor', 'Level von Freunden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1651, 'news_kind_friend_posts', 'Friends'' posts', 'ACTIVE', SYSDATE, 'Friends'' posts', 'Посты друзей', 'Postările prietenilor', 'Beiträge von Freunden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1652, 'news_kind_patch', 'Updates', 'ACTIVE', SYSDATE, 'Updates', 'Обновления', 'Actualizări', 'Updates')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1653, 'news_kind_questions', 'New questions', 'ACTIVE', SYSDATE, 'New questions', 'Новые вопросы', 'Întrebări noi', 'Neue Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1654, 'news_kind_world', 'World news', 'ACTIVE', SYSDATE, 'World news', 'Мировые новости', 'Știri din lume', 'Weltnachrichten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1655, 'news_lead', 'Updates, new questions, your friends and the world, from the last 30 days', 'ACTIVE', SYSDATE, 'Updates, new questions, your friends and the world, from the last 30 days', 'Обновления, новые вопросы, твои друзья и мир — за последние 30 дней', 'Actualizări, întrebări noi, prietenii tăi și lumea, din ultimele 30 de zile', 'Updates, neue Fragen, deine Freunde und die Welt aus den letzten 30 Tagen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1656, 'news_lead_guest', 'Updates, new questions and the world. Log in to see your friends here too.', 'ACTIVE', SYSDATE, 'Updates, new questions and the world. Log in to see your friends here too.', 'Обновления, новые вопросы и мир. Войди, чтобы видеть здесь и своих друзей.', 'Actualizări, întrebări noi și lumea. Autentifică-te ca să-ți vezi și prietenii aici.', 'Updates, neue Fragen und die Welt. Melde dich an, um hier auch deine Freunde zu sehen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1657, 'news_none', 'No news in the last 30 days.', 'ACTIVE', SYSDATE, 'No news in the last 30 days.', 'За последние 30 дней новостей нет.', 'Nicio noutate în ultimele 30 de zile.', 'Keine Neuigkeiten in den letzten 30 Tagen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1658, 'news_patch_text', 'What changed?', 'ACTIVE', SYSDATE, 'What changed?', 'Что изменилось?', 'Ce s-a schimbat?', 'Was hat sich geändert?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1659, 'news_patch_title', 'Title', 'ACTIVE', SYSDATE, 'Title', 'Заголовок', 'Titlu', 'Titel')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1660, 'news_post_text', 'Tell your friends something', 'ACTIVE', SYSDATE, 'Tell your friends something', 'Расскажи что-нибудь друзьям', 'Spune-le ceva prietenilor', 'Erzähl deinen Freunden etwas')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1661, 'news_publish', 'Publish', 'ACTIVE', SYSDATE, 'Publish', 'Опубликовать', 'Publică', 'Veröffentlichen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1662, 'next', 'Next', 'ACTIVE', SYSDATE, 'Next', 'Далее', 'Următoarea', 'Weiter')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1663, 'next_trophy', 'Next trophy', 'ACTIVE', SYSDATE, 'Next trophy', 'Следующий трофей', 'Următorul trofeu', 'Nächste Trophäe')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1664, 'no_account', 'Don''t have an account?', 'ACTIVE', SYSDATE, 'Don''t have an account?', 'Нет аккаунта?', 'Nu ai cont?', 'Noch kein Konto?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1665, 'no_answer', 'Not answered', 'ACTIVE', SYSDATE, 'Not answered', 'Нет ответа', 'Fără răspuns', 'Nicht beantwortet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1666, 'no_categories', 'No categories', 'ACTIVE', SYSDATE, 'No categories', 'Нет категорий', 'Nicio categorie', 'Keine Kategorien')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1667, 'no_categories_match', 'No categories match “{{query}}”.', 'ACTIVE', SYSDATE, 'No categories match “{{query}}”.', 'Нет категорий по запросу «{{query}}».', 'Nicio categorie nu corespunde cu „{{query}}”.', 'Keine Kategorien passen zu „{{query}}“.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1668, 'no_chats_yet', 'No chats yet — start one to send a question.', 'ACTIVE', SYSDATE, 'No chats yet — start one to send a question.', 'Чатов пока нет — начни чат, чтобы отправить вопрос.', 'Încă nu ai conversații — începe una ca să trimiți o întrebare.', 'Noch keine Chats — starte einen, um eine Frage zu senden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1669, 'no_friends', 'No friends yet. Find someone above.', 'ACTIVE', SYSDATE, 'No friends yet. Find someone above.', 'Друзей пока нет. Найди кого-нибудь выше.', 'Încă nu ai prieteni. Caută pe cineva mai sus.', 'Noch keine Freunde. Finde oben jemanden.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1670, 'no_groups', 'You are not in any group yet', 'ACTIVE', SYSDATE, 'You are not in any group yet', 'Ты пока не состоишь ни в одной группе', 'Încă nu faci parte din niciun grup', 'Du bist noch in keiner Gruppe')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1671, 'no_groups_hint', 'Start a group with the people you want to chat with.', 'ACTIVE', SYSDATE, 'Start a group with the people you want to chat with.', 'Создай группу с теми, с кем хочешь общаться.', 'Creează un grup cu persoanele cu care vrei să vorbești.', 'Starte eine Gruppe mit den Leuten, mit denen du chatten willst.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1672, 'no_groups_to_pick', 'No groups', 'ACTIVE', SYSDATE, 'No groups', 'Нет групп', 'Niciun grup', 'Keine Gruppen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1673, 'no_matches', 'No matches', 'ACTIVE', SYSDATE, 'No matches', 'Ничего не найдено', 'Niciun rezultat', 'Keine Treffer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1674, 'no_messages', 'No messages yet. Say hi!', 'ACTIVE', SYSDATE, 'No messages yet. Say hi!', 'Сообщений пока нет. Поздоровайся!', 'Încă nu sunt mesaje. Salută!', 'Noch keine Nachrichten. Sag Hallo!')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1675, 'no_my_quizzes', 'You have not made a quiz yet.', 'ACTIVE', SYSDATE, 'You have not made a quiz yet.', 'Ты ещё не создал ни одной викторины.', 'Încă nu ai creat niciun quiz.', 'Du hast noch kein Quiz erstellt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1676, 'no_people', 'No one left to add', 'ACTIVE', SYSDATE, 'No one left to add', 'Больше некого добавить', 'Nu mai e nimeni de adăugat', 'Niemand mehr zum Hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1677, 'no_people_to_invite', 'No one to invite', 'ACTIVE', SYSDATE, 'No one to invite', 'Некого пригласить', 'Nu e nimeni de invitat', 'Niemand zum Einladen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1678, 'no_quiz_history', 'No finished quizzes yet. Take one and it will show up here.', 'ACTIVE', SYSDATE, 'No finished quizzes yet. Take one and it will show up here.', 'Завершённых викторин пока нет. Пройди одну, и она появится здесь.', 'Încă nu ai quizuri terminate. Joacă unul și va apărea aici.', 'Noch keine abgeschlossenen Quiz. Spiel eins und es erscheint hier.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1679, 'no_quiz_invitations', 'No invitations yet. When someone invites you to their quiz, it shows up here.', 'ACTIVE', SYSDATE, 'No invitations yet. When someone invites you to their quiz, it shows up here.', 'Приглашений пока нет. Когда кто-то пригласит тебя в свою викторину, оно появится здесь.', 'Încă nu ai invitații. Când cineva te invită la quizul său, invitația apare aici.', 'Noch keine Einladungen. Wenn dich jemand zu seinem Quiz einlädt, erscheint es hier.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1680, 'no_username', 'No username', 'ACTIVE', SYSDATE, 'No username', 'Нет имени пользователя', 'Fără nume de utilizator', 'Kein Benutzername')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1681, 'no_username_hint', 'Set a username in your profile', 'ACTIVE', SYSDATE, 'Set a username in your profile', 'Укажи имя пользователя в профиле', 'Setează un nume de utilizator în profil', 'Lege in deinem Profil einen Benutzernamen fest')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1682, 'none', 'None', 'ACTIVE', SYSDATE, 'None', 'Никого', 'Niciunul', 'Keine')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1683, 'notifications', 'Notifications', 'ACTIVE', SYSDATE, 'Notifications', 'Уведомления', 'Notificări', 'Benachrichtigungen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1684, 'notifications_group_earlier', 'Earlier', 'ACTIVE', SYSDATE, 'Earlier', 'Ранее', 'Mai devreme', 'Früher')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1685, 'notifications_group_new', 'New', 'ACTIVE', SYSDATE, 'New', 'Новые', 'Noi', 'Neu')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1686, 'notifications_new_count', '{{count}} new', 'ACTIVE', SYSDATE, '{{count}} new', 'Новых: {{count}}', '{{count}} noi', '{{count}} neu')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1687, 'notifications_none', 'Nothing new yet.', 'ACTIVE', SYSDATE, 'Nothing new yet.', 'Пока ничего нового.', 'Nimic nou deocamdată.', 'Noch nichts Neues.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1688, 'notifications_unread', 'Notifications, {{count}} new', 'ACTIVE', SYSDATE, 'Notifications, {{count}} new', 'Уведомления, новых: {{count}}', 'Notificări, {{count}} noi', 'Benachrichtigungen, {{count}} neu')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1689, 'occupation_quizzes', 'Lean express quizzes to my occupation', 'ACTIVE', SYSDATE, 'Lean express quizzes to my occupation', 'Подбирать экспресс-викторины под мою профессию', 'Adaptează quizurile express la ocupația mea', 'Express-Quiz an meinen Beruf anpassen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1690, 'of_questions', 'of {{count}}', 'ACTIVE', SYSDATE, 'of {{count}}', 'из {{count}}', 'din {{count}}', 'von {{count}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1691, 'old_password_wrong', 'Old password does not match', 'ACTIVE', SYSDATE, 'Old password does not match', 'Старый пароль не совпадает', 'Parola veche nu se potrivește', 'Das alte Passwort stimmt nicht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1692, 'open_feedback', '{{count}} open feedback', 'ACTIVE', SYSDATE, '{{count}} open feedback', 'Открытых отзывов: {{count}}', '{{count}} feedback deschis', '{{count}} offenes Feedback')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1693, 'or_continue_with', 'or continue with', 'ACTIVE', SYSDATE, 'or continue with', 'или продолжить через', 'sau continuă cu', 'oder weiter mit')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1694, 'participants', 'Participants', 'ACTIVE', SYSDATE, 'Participants', 'Участники', 'Participanți', 'Teilnehmer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1695, 'password', 'Password', 'ACTIVE', SYSDATE, 'Password', 'Пароль', 'Parolă', 'Passwort')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1696, 'password_changed', 'Password changed. Please log in again.', 'ACTIVE', SYSDATE, 'Password changed. Please log in again.', 'Пароль изменён. Войди снова.', 'Parola a fost schimbată. Autentifică-te din nou.', 'Passwort geändert. Bitte melde dich erneut an.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1697, 'password_common', 'Too common — easy to guess', 'ACTIVE', SYSDATE, 'Too common — easy to guess', 'Слишком распространённый — легко угадать', 'Prea comună — ușor de ghicit', 'Zu häufig — leicht zu erraten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1698, 'password_common_toast', 'That password is too common and easy to guess: choose another.', 'ACTIVE', SYSDATE, 'That password is too common and easy to guess: choose another.', 'Этот пароль слишком распространён и его легко угадать: выбери другой.', 'Parola e prea comună și ușor de ghicit: alege alta.', 'Dieses Passwort ist zu häufig und leicht zu erraten: Wähle ein anderes.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1699, 'password_good', 'Good', 'ACTIVE', SYSDATE, 'Good', 'Хороший', 'Bună', 'Gut')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1700, 'password_not_strong', 'Choose a stronger password: at least 8 characters, mixing lowercase, uppercase, numbers and symbols.', 'ACTIVE', SYSDATE, 'Choose a stronger password: at least 8 characters, mixing lowercase, uppercase, numbers and symbols.', 'Выбери пароль понадёжнее: не менее 8 символов, строчные и заглавные буквы, цифры и символы.', 'Alege o parolă mai puternică: cel puțin 8 caractere, cu litere mici, majuscule, cifre și simboluri.', 'Wähle ein stärkeres Passwort: mindestens 8 Zeichen, gemischt aus Klein- und Großbuchstaben, Zahlen und Symbolen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1701, 'password_strong', 'Strong', 'ACTIVE', SYSDATE, 'Strong', 'Надёжный', 'Puternică', 'Stark')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1702, 'password_too_short', 'Too short — at least 8 characters', 'ACTIVE', SYSDATE, 'Too short — at least 8 characters', 'Слишком короткий — минимум 8 символов', 'Prea scurtă — cel puțin 8 caractere', 'Zu kurz — mindestens 8 Zeichen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1703, 'password_weak', 'Weak — mix lowercase, uppercase, numbers and symbols', 'ACTIVE', SYSDATE, 'Weak — mix lowercase, uppercase, numbers and symbols', 'Слабый — смешай строчные и заглавные буквы, цифры и символы', 'Slabă — combină litere mici, majuscule, cifre și simboluri', 'Schwach — mische Klein- und Großbuchstaben, Zahlen und Symbole')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1704, 'passwords_do_not_match', 'Passwords do not match', 'ACTIVE', SYSDATE, 'Passwords do not match', 'Пароли не совпадают', 'Parolele nu se potrivesc', 'Passwörter stimmen nicht überein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1705, 'period_day', 'Today', 'ACTIVE', SYSDATE, 'Today', 'Сегодня', 'Azi', 'Heute')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1706, 'period_month', 'This month', 'ACTIVE', SYSDATE, 'This month', 'В этом месяце', 'Luna aceasta', 'Diesen Monat')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1707, 'period_week', 'This week', 'ACTIVE', SYSDATE, 'This week', 'На этой неделе', 'Săptămâna aceasta', 'Diese Woche')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1708, 'photo_unsaved', 'New photo — save to keep it', 'ACTIVE', SYSDATE, 'New photo — save to keep it', 'Новое фото — сохрани, чтобы оставить его', 'Fotografie nouă — salvează ca s-o păstrezi', 'Neues Foto — speichere, um es zu behalten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1709, 'play', 'Play', 'ACTIVE', SYSDATE, 'Play', 'Играть', 'Joacă', 'Spielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1710, 'play_again', 'Play again', 'ACTIVE', SYSDATE, 'Play again', 'Сыграть ещё', 'Joacă din nou', 'Nochmal spielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1711, 'play_category', 'Play {{name}}', 'ACTIVE', SYSDATE, 'Play {{name}}', 'Играть: {{name}}', 'Joacă {{name}}', '{{name}} spielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1712, 'play_together', 'Play together', 'ACTIVE', SYSDATE, 'Play together', 'Играть вместе', 'Joacă împreună', 'Zusammen spielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1713, 'played', 'Played', 'ACTIVE', SYSDATE, 'Played', 'Сыграно', 'Jucat', 'Gespielt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1714, 'played_count', 'played {{count}} times', 'ACTIVE', SYSDATE, 'played {{count}} times', 'сыграно раз: {{count}}', 'jucat de {{count}} ori', '{{count}}-mal gespielt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1715, 'presence_offline', 'Offline', 'ACTIVE', SYSDATE, 'Offline', 'Не в сети', 'Offline', 'Offline')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1716, 'presence_online', 'Online', 'ACTIVE', SYSDATE, 'Online', 'В сети', 'Online', 'Online')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1717, 'presence_playing', 'In a match', 'ACTIVE', SYSDATE, 'In a match', 'В матче', 'Într-un meci', 'Im Match')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1718, 'previous', 'Previous', 'ACTIVE', SYSDATE, 'Previous', 'Назад', 'Înapoi', 'Zurück')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1719, 'profile_not_found', 'There is no such player.', 'ACTIVE', SYSDATE, 'There is no such player.', 'Такого игрока нет.', 'Nu există un astfel de jucător.', 'Diesen Spieler gibt es nicht.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1720, 'qr_for', 'QR code for the {{name}} address', 'ACTIVE', SYSDATE, 'QR code for the {{name}} address', 'QR-код для адреса {{name}}', 'Cod QR pentru adresa {{name}}', 'QR-Code für die {{name}}-Adresse')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1721, 'question', 'Question', 'ACTIVE', SYSDATE, 'Question', 'Вопрос', 'Întrebarea', 'Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1722, 'question_answer_error', 'Add at least one right answer', 'ACTIVE', SYSDATE, 'Add at least one right answer', 'Добавь хотя бы один правильный ответ', 'Adaugă cel puțin un răspuns corect', 'Füge mindestens eine richtige Antwort hinzu')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1723, 'question_content_error', 'Write the question', 'ACTIVE', SYSDATE, 'Write the question', 'Напиши вопрос', 'Scrie întrebarea', 'Schreib die Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1724, 'question_items_error', 'Add at least two items', 'ACTIVE', SYSDATE, 'Add at least two items', 'Добавь хотя бы два элемента', 'Adaugă cel puțin două elemente', 'Füge mindestens zwei Elemente hinzu')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1725, 'question_number', 'Question {{number}}', 'ACTIVE', SYSDATE, 'Question {{number}}', 'Вопрос {{number}}', 'Întrebarea {{number}}', 'Frage {{number}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1726, 'question_one_answer_error', 'This quiz type takes one right answer', 'ACTIVE', SYSDATE, 'This quiz type takes one right answer', 'В этом типе викторины один правильный ответ', 'Acest tip de quiz are un singur răspuns corect', 'Dieser Quiztyp hat genau eine richtige Antwort')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1727, 'question_one_wrong_error', 'This quiz type takes exactly one wrong option', 'ACTIVE', SYSDATE, 'This quiz type takes exactly one wrong option', 'В этом типе викторины ровно один неверный вариант', 'Acest tip de quiz are exact o variantă greșită', 'Dieser Quiztyp hat genau eine falsche Option')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1728, 'question_placeholder', 'e.g. What is the capital of Moldova?', 'ACTIVE', SYSDATE, 'e.g. What is the capital of Moldova?', 'напр. Какая столица Молдовы?', 'ex. Care este capitala Moldovei?', 'z. B. Was ist die Hauptstadt von Moldau?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1729, 'question_sent', 'Question sent to {{name}}', 'ACTIVE', SYSDATE, 'Question sent to {{name}}', 'Вопрос отправлен: {{name}}', 'Întrebare trimisă lui {{name}}', 'Frage an {{name}} gesendet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1730, 'question_wrong_error', 'Add at least one wrong option to choose from', 'ACTIVE', SYSDATE, 'Add at least one wrong option to choose from', 'Добавь хотя бы один неверный вариант для выбора', 'Adaugă cel puțin o variantă greșită din care să se aleagă', 'Füge mindestens eine falsche Option zur Auswahl hinzu')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1731, 'question_wrong_matches_right', 'A wrong option is the same as a right answer', 'ACTIVE', SYSDATE, 'A wrong option is the same as a right answer', 'Неверный вариант совпадает с правильным ответом', 'O variantă greșită este identică cu un răspuns corect', 'Eine falsche Option ist gleich einer richtigen Antwort')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1732, 'questions', 'Questions', 'ACTIVE', SYSDATE, 'Questions', 'Вопросы', 'Întrebări', 'Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1733, 'questions_answered', 'Questions', 'ACTIVE', SYSDATE, 'Questions', 'Вопросы', 'Întrebări', 'Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1734, 'questions_count', '{{count}} questions', 'ACTIVE', SYSDATE, '{{count}} questions', 'Вопросов: {{count}}', '{{count}} întrebări', '{{count}} Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1735, 'quick_question', 'Quick question', 'ACTIVE', SYSDATE, 'Quick question', 'Быстрый вопрос', 'Întrebare rapidă', 'Schnelle Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1736, 'quiz_already_answered', 'You already answered this one.', 'ACTIVE', SYSDATE, 'You already answered this one.', 'Ты уже ответил на этот вопрос.', 'Ai răspuns deja la aceasta.', 'Diese hast du schon beantwortet.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1737, 'quiz_answer_covered', 'Answered — hidden until you answer it too', 'ACTIVE', SYSDATE, 'Answered — hidden until you answer it too', 'Есть ответ — скрыт, пока ты тоже не ответишь', 'Răspuns dat — ascuns până răspunzi și tu', 'Beantwortet — verborgen, bis du auch antwortest')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1738, 'quiz_answered', 'answered a question', 'ACTIVE', SYSDATE, 'answered a question', 'ответил на вопрос', 'a răspuns la o întrebare', 'hat eine Frage beantwortet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1739, 'quiz_category_error', 'Pick at least one category', 'ACTIVE', SYSDATE, 'Pick at least one category', 'Выбери хотя бы одну категорию', 'Alege cel puțin o categorie', 'Wähle mindestens eine Kategorie')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1740, 'quiz_created', 'Quiz created', 'ACTIVE', SYSDATE, 'Quiz created', 'Викторина создана', 'Quiz creat', 'Quiz erstellt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1741, 'quiz_created_invited', 'Quiz created, {{count}} invites sent', 'ACTIVE', SYSDATE, 'Quiz created, {{count}} invites sent', 'Викторина создана, приглашений отправлено: {{count}}', 'Quiz creat, {{count}} invitații trimise', 'Quiz erstellt, {{count}} Einladungen gesendet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1742, 'quiz_deleted', 'Quiz deleted', 'ACTIVE', SYSDATE, 'Quiz deleted', 'Викторина удалена', 'Quiz șters', 'Quiz gelöscht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1743, 'quiz_history', 'Quiz history', 'ACTIVE', SYSDATE, 'Quiz history', 'История викторин', 'Istoricul quizurilor', 'Quiz-Verlauf')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1744, 'quiz_invitations', 'Quiz invitations', 'ACTIVE', SYSDATE, 'Quiz invitations', 'Приглашения в викторины', 'Invitații la quiz', 'Quiz-Einladungen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1745, 'quiz_invitations_subtitle', 'Quizzes other players made and invited you to.', 'ACTIVE', SYSDATE, 'Quizzes other players made and invited you to.', 'Викторины, которые создали другие игроки и пригласили тебя.', 'Quizuri create de alți jucători, la care te-au invitat.', 'Quiz, die andere Spieler erstellt und dich eingeladen haben.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1746, 'quiz_result', 'Quiz result', 'ACTIVE', SYSDATE, 'Quiz result', 'Результат викторины', 'Rezultatul quizului', 'Quiz-Ergebnis')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1747, 'quiz_sent_by_you', 'Waiting for an answer.', 'ACTIVE', SYSDATE, 'Waiting for an answer.', 'Ждём ответа.', 'Se așteaptă un răspuns.', 'Warte auf eine Antwort.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1748, 'quiz_sent_you_a_question', 'sent a question', 'ACTIVE', SYSDATE, 'sent a question', 'отправил(а) вопрос', 'a trimis o întrebare', 'hat eine Frage geschickt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1749, 'quiz_settings', 'Settings', 'ACTIVE', SYSDATE, 'Settings', 'Настройки', 'Setări', 'Einstellungen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1750, 'quiz_time_error', 'Between 5 and 300 seconds', 'ACTIVE', SYSDATE, 'Between 5 and 300 seconds', 'От 5 до 300 секунд', 'Între 5 și 300 de secunde', 'Zwischen 5 und 300 Sekunden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1751, 'quiz_type', 'Quiz type', 'ACTIVE', SYSDATE, 'Quiz type', 'Тип викторины', 'Tipul quizului', 'Quiz-Typ')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1752, 'quiz_type_error', 'Pick how the quiz is played', 'ACTIVE', SYSDATE, 'Pick how the quiz is played', 'Выбери, как играть в викторину', 'Alege cum se joacă quizul', 'Wähle, wie das Quiz gespielt wird')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1753, 'quizzes', 'Quizzes', 'ACTIVE', SYSDATE, 'Quizzes', 'Викторины', 'Quizuri', 'Quizze')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1754, 'quizzes_count', '{{count}} quizzes', 'ACTIVE', SYSDATE, '{{count}} quizzes', 'Викторин: {{count}}', '{{count}} quizuri', '{{count}} Quizze')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1755, 'quizzes_played', 'Quizzes played', 'ACTIVE', SYSDATE, 'Quizzes played', 'Сыграно викторин', 'Quizuri jucate', 'Gespielte Quizze')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1756, 'reactions', 'Reactions', 'ACTIVE', SYSDATE, 'Reactions', 'Реакции', 'Reacții', 'Reaktionen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1757, 'read_more', 'Read more', 'ACTIVE', SYSDATE, 'Read more', 'Читать дальше', 'Citește mai mult', 'Weiterlesen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1758, 'register_subtitle', 'Create an account and start playing.', 'ACTIVE', SYSDATE, 'Create an account and start playing.', 'Создай аккаунт и начни играть.', 'Creează un cont și începe să joci.', 'Erstelle ein Konto und leg los.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1759, 'remove', 'Remove', 'ACTIVE', SYSDATE, 'Remove', 'Удалить', 'Elimină', 'Entfernen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1760, 'remove_friend', 'Remove {{name}}', 'ACTIVE', SYSDATE, 'Remove {{name}}', 'Удалить {{name}}', 'Elimină pe {{name}}', '{{name}} entfernen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1761, 'remove_friend_confirm', '{{name}} will be removed from your friends, and you from theirs.', 'ACTIVE', SYSDATE, '{{name}} will be removed from your friends, and you from theirs.', '{{name}} будет удалён(а) из твоих друзей, а ты — из его (её) друзей.', '{{name}} va fi eliminat din prietenii tăi, iar tu din ai săi.', '{{name}} wird aus deinen Freunden entfernt und du aus seinen bzw. ihren.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1762, 'remove_friend_title', 'Remove friend?', 'ACTIVE', SYSDATE, 'Remove friend?', 'Удалить из друзей?', 'Elimini prietenul?', 'Freund entfernen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1763, 'remove_group_photo', 'Remove the group photo', 'ACTIVE', SYSDATE, 'Remove the group photo', 'Удалить фото группы', 'Elimină fotografia grupului', 'Gruppenfoto entfernen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1764, 'remove_item', 'Remove {{name}}', 'ACTIVE', SYSDATE, 'Remove {{name}}', 'Удалить {{name}}', 'Elimină {{name}}', '{{name}} entfernen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1765, 'remove_question', 'Remove question {{number}}', 'ACTIVE', SYSDATE, 'Remove question {{number}}', 'Удалить вопрос {{number}}', 'Elimină întrebarea {{number}}', 'Frage {{number}} entfernen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1766, 'remove_screenshot', 'Remove the screenshot', 'ACTIVE', SYSDATE, 'Remove the screenshot', 'Удалить скриншот', 'Elimină captura de ecran', 'Screenshot entfernen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1767, 'repass', 'Repeat password', 'ACTIVE', SYSDATE, 'Repeat password', 'Повтори пароль', 'Repetă parola', 'Passwort wiederholen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1768, 'report', 'Report', 'ACTIVE', SYSDATE, 'Report', 'Пожаловаться', 'Raportează', 'Melden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1769, 'report_details', 'Details', 'ACTIVE', SYSDATE, 'Details', 'Подробности', 'Detalii', 'Details')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1770, 'report_details_placeholder', 'Details (optional), e.g. what the right answer should be', 'ACTIVE', SYSDATE, 'Details (optional), e.g. what the right answer should be', 'Подробности (необязательно), например, какой должен быть правильный ответ', 'Detalii (opțional), de ex. care ar trebui să fie răspunsul corect', 'Details (optional), z. B. wie die richtige Antwort lauten sollte')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1771, 'report_other', 'Other', 'ACTIVE', SYSDATE, 'Other', 'Другое', 'Altceva', 'Sonstiges')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1772, 'report_question', 'Report a problem', 'ACTIVE', SYSDATE, 'Report a problem', 'Сообщить о проблеме', 'Raportează o problemă', 'Problem melden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1773, 'report_question_title', 'Report this question', 'ACTIVE', SYSDATE, 'Report this question', 'Пожаловаться на вопрос', 'Raportează această întrebare', 'Diese Frage melden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1774, 'report_reason', 'What is wrong?', 'ACTIVE', SYSDATE, 'What is wrong?', 'Что не так?', 'Ce nu este în regulă?', 'Was stimmt nicht?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1775, 'report_sent', 'Thanks! The admins will look at this question.', 'ACTIVE', SYSDATE, 'Thanks! The admins will look at this question.', 'Спасибо! Администраторы посмотрят этот вопрос.', 'Mulțumim! Administratorii vor verifica această întrebare.', 'Danke! Die Admins sehen sich diese Frage an.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1776, 'report_skip', 'Skip this question', 'ACTIVE', SYSDATE, 'Skip this question', 'Пропустить этот вопрос', 'Sari peste această întrebare', 'Diese Frage überspringen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1777, 'report_typo', 'Typo', 'ACTIVE', SYSDATE, 'Typo', 'Опечатка', 'Greșeală de scriere', 'Tippfehler')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1778, 'report_unclear', 'Unclear', 'ACTIVE', SYSDATE, 'Unclear', 'Непонятно', 'Neclar', 'Unklar')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1779, 'report_wrong_answer', 'Wrong answer', 'ACTIVE', SYSDATE, 'Wrong answer', 'Неверный ответ', 'Răspuns greșit', 'Falsche Antwort')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1780, 'reset', 'Reset', 'ACTIVE', SYSDATE, 'Reset', 'Сбросить', 'Resetează', 'Zurücksetzen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1781, 'reset_all', 'Reset everything', 'ACTIVE', SYSDATE, 'Reset everything', 'Сбросить всё', 'Resetează tot', 'Alles zurücksetzen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1782, 'reset_all_confirm', 'Every setting on this page goes back to how the site comes.', 'ACTIVE', SYSDATE, 'Every setting on this page goes back to how the site comes.', 'Все настройки на этой странице вернутся к стандартным.', 'Toate setările de pe această pagină revin la valorile implicite.', 'Alle Einstellungen auf dieser Seite werden auf den Standard zurückgesetzt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1783, 'reset_all_title', 'Reset the appearance?', 'ACTIVE', SYSDATE, 'Reset the appearance?', 'Сбросить оформление?', 'Resetezi aspectul?', 'Darstellung zurücksetzen?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1784, 'reset_view', 'Reset view', 'ACTIVE', SYSDATE, 'Reset view', 'Сбросить вид', 'Resetează vizualizarea', 'Ansicht zurücksetzen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1785, 'result_good', 'Nicely done.', 'ACTIVE', SYSDATE, 'Nicely done.', 'Отлично сыграно.', 'Bravo, bine jucat.', 'Gut gemacht.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1786, 'result_great', 'Brilliant run.', 'ACTIVE', SYSDATE, 'Brilliant run.', 'Блестящая игра.', 'Joc strălucit.', 'Brillante Runde.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1787, 'result_keep_going', 'Keep at it: every question teaches one.', 'ACTIVE', SYSDATE, 'Keep at it: every question teaches one.', 'Не сдавайся: каждый вопрос чему-то учит.', 'Continuă: fiecare întrebare te învață ceva.', 'Bleib dran: Jede Frage bringt dir etwas bei.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1788, 'right_answer', 'Right answer', 'ACTIVE', SYSDATE, 'Right answer', 'Правильный ответ', 'Răspuns corect', 'Richtige Antwort')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1789, 'right_answer_number', 'Right answer {{number}}', 'ACTIVE', SYSDATE, 'Right answer {{number}}', 'Правильный ответ {{number}}', 'Răspunsul corect {{number}}', 'Richtige Antwort {{number}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1790, 'right_answers', 'Right', 'ACTIVE', SYSDATE, 'Right', 'Верно', 'Corecte', 'Richtig')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1791, 'right_answers_label', 'Right answers', 'ACTIVE', SYSDATE, 'Right answers', 'Правильные ответы', 'Răspunsuri corecte', 'Richtige Antworten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1792, 'right_of', '{{right}}/{{total}} right', 'ACTIVE', SYSDATE, '{{right}}/{{total}} right', '{{right}}/{{total}} верно', '{{right}}/{{total}} corecte', '{{right}}/{{total}} richtig')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1793, 'saving', 'Saving', 'ACTIVE', SYSDATE, 'Saving', 'Сохранение', 'Se salvează', 'Wird gespeichert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1794, 'search', 'Search', 'ACTIVE', SYSDATE, 'Search', 'Поиск', 'Caută', 'Suchen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1795, 'search_categories', 'Search categories…', 'ACTIVE', SYSDATE, 'Search categories…', 'Поиск категорий…', 'Caută categorii…', 'Kategorien suchen…')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1796, 'search_people', 'Search people', 'ACTIVE', SYSDATE, 'Search people', 'Поиск людей', 'Caută persoane', 'Personen suchen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1797, 'seconds_each', '{{count}} s each', 'ACTIVE', SYSDATE, '{{count}} s each', 'по {{count}} с', 'câte {{count}} s', 'je {{count}} s')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1798, 'send', 'Send', 'ACTIVE', SYSDATE, 'Send', 'Отправить', 'Trimite', 'Senden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1799, 'send_feedback', 'Send feedback', 'ACTIVE', SYSDATE, 'Send feedback', 'Оставить отзыв', 'Trimite feedback', 'Feedback senden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1800, 'send_question', 'Send this question to a chat', 'ACTIVE', SYSDATE, 'Send this question to a chat', 'Отправить этот вопрос в чат', 'Trimite această întrebare într-un chat', 'Diese Frage in einen Chat senden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1801, 'send_to_chat', 'Send to a chat', 'ACTIVE', SYSDATE, 'Send to a chat', 'Отправить в чат', 'Trimite într-un chat', 'In einen Chat senden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1802, 'set_group_photo', 'Set a group photo', 'ACTIVE', SYSDATE, 'Set a group photo', 'Установить фото группы', 'Setează o fotografie de grup', 'Gruppenfoto festlegen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1803, 'shop', 'Shop', 'ACTIVE', SYSDATE, 'Shop', 'Магазин', 'Magazin', 'Shop')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1804, 'shop_balance', 'You have {{coins}} coins. Every 10 XP you earn brings one more.', 'ACTIVE', SYSDATE, 'You have {{coins}} coins. Every 10 XP you earn brings one more.', 'У тебя {{coins}} монет. Каждые 10 XP приносят ещё одну.', 'Ai {{coins}} monede. Fiecare 10 XP câștigate îți aduc încă una.', 'Du hast {{coins}} Münzen. Je 10 verdiente XP bringen dir eine weitere.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1805, 'shop_sign_in', 'Sign in to earn and spend coins.', 'ACTIVE', SYSDATE, 'Sign in to earn and spend coins.', 'Войди, чтобы зарабатывать и тратить монеты.', 'Autentifică-te ca să câștigi și să cheltuiești monede.', 'Melde dich an, um Münzen zu verdienen und auszugeben.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1806, 'show_password', 'Show password', 'ACTIVE', SYSDATE, 'Show password', 'Показать пароль', 'Arată parola', 'Passwort anzeigen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1807, 'show_subcategories', 'Show subcategories', 'ACTIVE', SYSDATE, 'Show subcategories', 'Показать подкатегории', 'Arată subcategoriile', 'Unterkategorien anzeigen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1808, 'sign_out_confirm', 'You will need to log in again to play and chat.', 'ACTIVE', SYSDATE, 'You will need to log in again to play and chat.', 'Чтобы играть и общаться в чате, нужно будет снова войти.', 'Va trebui să te autentifici din nou ca să joci și să folosești chatul.', 'Du musst dich erneut anmelden, um zu spielen und zu chatten.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1809, 'sign_out_title', 'Sign out?', 'ACTIVE', SYSDATE, 'Sign out?', 'Выйти?', 'Te deconectezi?', 'Abmelden?')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1810, 'skip', 'Skip', 'ACTIVE', SYSDATE, 'Skip', 'Пропустить', 'Sari peste', 'Überspringen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1811, 'social_blocked', 'This account has been blocked.', 'ACTIVE', SYSDATE, 'This account has been blocked.', 'Этот аккаунт заблокирован.', 'Acest cont a fost blocat.', 'Dieses Konto wurde gesperrt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1812, 'social_failed', 'Signing in did not work. Please try again.', 'ACTIVE', SYSDATE, 'Signing in did not work. Please try again.', 'Не удалось войти. Попробуй ещё раз.', 'Autentificarea nu a reușit. Încearcă din nou.', 'Die Anmeldung hat nicht geklappt. Bitte versuch es noch einmal.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1813, 'social_no_email', 'That account did not share a verified email address with us.', 'ACTIVE', SYSDATE, 'That account did not share a verified email address with us.', 'Этот аккаунт не передал нам подтверждённый адрес электронной почты.', 'Acest cont nu ne-a transmis o adresă de email verificată.', 'Dieses Konto hat uns keine bestätigte E-Mail-Adresse übermittelt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1814, 'social_unverified', 'An account with this email is waiting for its email to be confirmed. Confirm it, then sign in.', 'ACTIVE', SYSDATE, 'An account with this email is waiting for its email to be confirmed. Confirm it, then sign in.', 'Аккаунт с этим адресом ждёт подтверждения электронной почты. Подтверди её и войди.', 'Un cont cu acest email așteaptă confirmarea adresei. Confirm-o, apoi autentifică-te.', 'Ein Konto mit dieser E-Mail-Adresse wartet noch auf die Bestätigung. Bestätige sie und melde dich dann an.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1815, 'start_quiz', 'Start quiz', 'ACTIVE', SYSDATE, 'Start quiz', 'Начать викторину', 'Începe quizul', 'Quiz starten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1816, 'statistics', 'Statistics', 'ACTIVE', SYSDATE, 'Statistics', 'Статистика', 'Statistici', 'Statistiken')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1817, 'stats_active_days', 'on {{days}} days', 'ACTIVE', SYSDATE, 'on {{days}} days', 'за {{days}} дн.', 'în {{days}} zile', 'an {{days}} Tagen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1818, 'stats_activity', 'Quizzes per day', 'ACTIVE', SYSDATE, 'Quizzes per day', 'Викторин в день', 'Quizuri pe zi', 'Quizze pro Tag')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1819, 'stats_empty', 'No quizzes in this period yet.', 'ACTIVE', SYSDATE, 'No quizzes in this period yet.', 'За этот период викторин пока нет.', 'Încă nu există quizuri în această perioadă.', 'In diesem Zeitraum noch keine Quizze.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1820, 'stats_hours', '{{hours}}h', 'ACTIVE', SYSDATE, '{{hours}}h', '{{hours}} ч', '{{hours}} h', '{{hours}} Std.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1821, 'stats_minutes', '{{minutes}}m', 'ACTIVE', SYSDATE, '{{minutes}}m', '{{minutes}} мин', '{{minutes}} min', '{{minutes}} Min.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1822, 'stats_most_played', 'Played most', 'ACTIVE', SYSDATE, 'Played most', 'Чаще всего', 'Cele mai jucate', 'Am meisten gespielt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1823, 'stats_per_question', 'Per question', 'ACTIVE', SYSDATE, 'Per question', 'На вопрос', 'Pe întrebare', 'Pro Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1824, 'stats_per_quiz', '{{seconds}}s per quiz', 'ACTIVE', SYSDATE, '{{seconds}}s per quiz', '{{seconds}} с на викторину', '{{seconds}} s pe quiz', '{{seconds}} s pro Quiz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1825, 'stats_right_wrong', '{{right}} right · {{wrong}} wrong', 'ACTIVE', SYSDATE, '{{right}} right · {{wrong}} wrong', '{{right}} верно · {{wrong}} неверно', '{{right}} corecte · {{wrong}} greșite', '{{right}} richtig · {{wrong}} falsch')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1826, 'stats_streak', 'Day streak', 'ACTIVE', SYSDATE, 'Day streak', 'Серия дней', 'Serie de zile', 'Tages-Serie')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1827, 'stats_streak_hint', 'days in a row right now', 'ACTIVE', SYSDATE, 'days in a row right now', 'дней подряд на данный момент', 'zile la rând în prezent', 'Tage in Folge bis jetzt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1828, 'stats_strong', 'Strong points', 'ACTIVE', SYSDATE, 'Strong points', 'Сильные стороны', 'Puncte forte', 'Stärken')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1829, 'stats_time_played', 'Time played', 'ACTIVE', SYSDATE, 'Time played', 'Время в игре', 'Timp de joc', 'Spielzeit')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1830, 'stats_top_category_hint', '{{quizzes}} quizzes · {{accuracy}}% right', 'ACTIVE', SYSDATE, '{{quizzes}} quizzes · {{accuracy}}% right', 'Викторин: {{quizzes}} · {{accuracy}}% верно', '{{quizzes}} quizuri · {{accuracy}}% corecte', '{{quizzes}} Quizze · {{accuracy}} % richtig')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1831, 'stats_trophies_hint', 'won in this period', 'ACTIVE', SYSDATE, 'won in this period', 'получено за этот период', 'câștigate în această perioadă', 'in diesem Zeitraum gewonnen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1832, 'stats_weak', 'Worth some reading', 'ACTIVE', SYSDATE, 'Worth some reading', 'Стоит подтянуть', 'Merită studiate', 'Lohnt sich nachzulesen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1833, 'status_code', 'Status code:', 'ACTIVE', SYSDATE, 'Status code:', 'Код статуса:', 'Cod de stare:', 'Statuscode:')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1834, 'step_choose_category', 'Choose a category', 'ACTIVE', SYSDATE, 'Choose a category', 'Выбери категорию', 'Alege o categorie', 'Wähle eine Kategorie')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1835, 'step_choose_category_text', 'Pick a topic you like, or let an express quiz pick for you.', 'ACTIVE', SYSDATE, 'Pick a topic you like, or let an express quiz pick for you.', 'Выбери интересную тему или позволь экспресс-викторине выбрать за тебя.', 'Alege un subiect care îți place sau lasă un quiz expres să aleagă pentru tine.', 'Such dir ein Thema aus, das dir gefällt, oder lass ein Express-Quiz für dich wählen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1836, 'step_keep_learning', 'Keep learning', 'ACTIVE', SYSDATE, 'Keep learning', 'Продолжай учиться', 'Învață în continuare', 'Lern weiter')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1837, 'step_keep_learning_text', 'Read up in the Wiki, then challenge your friends.', 'ACTIVE', SYSDATE, 'Read up in the Wiki, then challenge your friends.', 'Почитай Вики, а потом брось вызов друзьям.', 'Documentează-te în Wiki, apoi provoacă-ți prietenii.', 'Lies im Wiki nach und fordere dann deine Freunde heraus.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1838, 'step_see_results', 'See your results', 'ACTIVE', SYSDATE, 'See your results', 'Посмотри результаты', 'Vezi rezultatele', 'Sieh dir deine Ergebnisse an')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1839, 'step_see_results_text', 'Go through your answers and the right ones, side by side.', 'ACTIVE', SYSDATE, 'Go through your answers and the right ones, side by side.', 'Сравни свои ответы с правильными бок о бок.', 'Parcurge răspunsurile tale și pe cele corecte, unul lângă altul.', 'Geh deine Antworten und die richtigen nebeneinander durch.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1840, 'step_take_quiz', 'Take the quiz', 'ACTIVE', SYSDATE, 'Take the quiz', 'Пройди викторину', 'Rezolvă quizul', 'Spiel das Quiz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1841, 'step_take_quiz_text', 'Answer question by question, at your own pace.', 'ACTIVE', SYSDATE, 'Answer question by question, at your own pace.', 'Отвечай на вопросы по одному, в своём темпе.', 'Răspunde întrebare cu întrebare, în ritmul tău.', 'Beantworte Frage für Frage, in deinem eigenen Tempo.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1842, 'streak_days', '{{days}} days in a row', 'ACTIVE', SYSDATE, '{{days}} days in a row', '{{days}} дн. подряд', '{{days}} zile la rând', '{{days}} Tage in Folge')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1843, 'streak_freeze', 'Streak freeze', 'ACTIVE', SYSDATE, 'Streak freeze', 'Заморозка серии', 'Înghețarea seriei', 'Serien-Freeze')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1844, 'streak_freeze_text', 'Covers one missed day, so your streak goes on. You hold {{held}} of {{max}}.', 'ACTIVE', SYSDATE, 'Covers one missed day, so your streak goes on. You hold {{held}} of {{max}}.', 'Покрывает один пропущенный день, и твоя серия продолжается. У тебя {{held}} из {{max}}.', 'Acoperă o zi ratată, ca seria ta să continue. Ai {{held}} din {{max}}.', 'Deckt einen verpassten Tag ab, damit deine Serie weiterläuft. Du hast {{held}} von {{max}}.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1845, 'subcategories_count', '{{count}} subcategories', 'ACTIVE', SYSDATE, '{{count}} subcategories', 'Подкатегорий: {{count}}', '{{count}} subcategorii', '{{count}} Unterkategorien')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1846, 'take_quiz_subtitle', 'Pick a category and start playing.', 'ACTIVE', SYSDATE, 'Pick a category and start playing.', 'Выбери категорию и начни играть.', 'Alege o categorie și începe să joci.', 'Wähle eine Kategorie und leg los.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1847, 'text_default', 'Default', 'ACTIVE', SYSDATE, 'Default', 'По умолчанию', 'Implicit', 'Standard')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1848, 'text_large', 'Large', 'ACTIVE', SYSDATE, 'Large', 'Крупный', 'Mare', 'Groß')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1849, 'text_larger', 'Larger', 'ACTIVE', SYSDATE, 'Larger', 'Ещё крупнее', 'Mai mare', 'Größer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1850, 'text_size', 'Text size', 'ACTIVE', SYSDATE, 'Text size', 'Размер текста', 'Mărimea textului', 'Textgröße')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1851, 'text_size_hint', 'For all the text on the site.', 'ACTIVE', SYSDATE, 'For all the text on the site.', 'Для всего текста на сайте.', 'Pentru tot textul de pe site.', 'Für den gesamten Text auf der Seite.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1852, 'text_small', 'Small', 'ACTIVE', SYSDATE, 'Small', 'Мелкий', 'Mic', 'Klein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1853, 'this_month', 'This month', 'ACTIVE', SYSDATE, 'This month', 'В этом месяце', 'Luna aceasta', 'Diesen Monat')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1854, 'this_week', 'This week', 'ACTIVE', SYSDATE, 'This week', 'На этой неделе', 'Săptămâna aceasta', 'Diese Woche')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1855, 'time_per_question', 'Seconds per question', 'ACTIVE', SYSDATE, 'Seconds per question', 'Секунд на вопрос', 'Secunde pe întrebare', 'Sekunden pro Frage')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1856, 'time_spent', 'Time', 'ACTIVE', SYSDATE, 'Time', 'Время', 'Timp', 'Zeit')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1857, 'together_lead', 'Duel a friend live, fill a room, race everyone on today''s questions, or send a score to beat.', 'ACTIVE', SYSDATE, 'Duel a friend live, fill a room, race everyone on today''s questions, or send a score to beat.', 'Сразись с другом вживую, собери комнату, соревнуйся со всеми на сегодняшних вопросах или отправь результат, который нужно побить.', 'Dă-te în duel live cu un prieten, umple o cameră, întrece-te cu toți pe întrebările de azi sau trimite un scor de bătut.', 'Duelliere dich live mit einem Freund, füll einen Raum, tritt mit allen bei den heutigen Fragen an oder schick einen Punktestand, den es zu schlagen gilt.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1858, 'together_sign_in', 'Log in to play with friends', 'ACTIVE', SYSDATE, 'Log in to play with friends', 'Войди, чтобы играть с друзьями', 'Autentifică-te ca să joci cu prietenii', 'Melde dich an, um mit Freunden zu spielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1859, 'trophies', 'Trophies', 'ACTIVE', SYSDATE, 'Trophies', 'Трофеи', 'Trofee', 'Trophäen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1860, 'trophies_all', 'All trophies', 'ACTIVE', SYSDATE, 'All trophies', 'Все трофеи', 'Toate trofeele', 'Alle Trophäen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1861, 'trophies_categories', 'Categories', 'ACTIVE', SYSDATE, 'Categories', 'Категории', 'Categorii', 'Kategorien')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1862, 'trophies_conquest', 'Conquest', 'ACTIVE', SYSDATE, 'Conquest', 'Завоевание', 'Cucerire', 'Eroberung')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1863, 'trophies_devotion', 'Devotion', 'ACTIVE', SYSDATE, 'Devotion', 'Преданность', 'Devotament', 'Hingabe')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1864, 'trophies_earned', '{{earned}} of {{total}} earned', 'ACTIVE', SYSDATE, '{{earned}} of {{total}} earned', 'Получено {{earned}} из {{total}}', '{{earned}} din {{total}} câștigate', '{{earned}} von {{total}} verdient')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1865, 'trophies_earned_of', '{{earned}} of {{total}} earned', 'ACTIVE', SYSDATE, '{{earned}} of {{total}} earned', 'Получено {{earned}} из {{total}}', '{{earned}} din {{total}} câștigate', '{{earned}} von {{total}} verdient')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1866, 'trophies_mind', 'Mind', 'ACTIVE', SYSDATE, 'Mind', 'Разум', 'Minte', 'Verstand')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1867, 'trophies_none_yet', 'No trophies yet — finish a quiz and the first one is yours.', 'ACTIVE', SYSDATE, 'No trophies yet — finish a quiz and the first one is yours.', 'Трофеев пока нет — пройди викторину, и первый будет твоим.', 'Încă niciun trofeu — termină un quiz și primul e al tău.', 'Noch keine Trophäen — schließ ein Quiz ab und die erste gehört dir.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1868, 'trophies_pick_hint', 'Pick one to show beside your name', 'ACTIVE', SYSDATE, 'Pick one to show beside your name', 'Выбери один, чтобы показывать рядом с именем', 'Alege unul pe care să-l afișezi lângă numele tău', 'Wähle eine aus, die neben deinem Namen angezeigt wird')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1869, 'trophies_quizzes', 'Quizzes', 'ACTIVE', SYSDATE, 'Quizzes', 'Викторины', 'Quizuri', 'Quizze')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1870, 'trophies_secret', 'Secret', 'ACTIVE', SYSDATE, 'Secret', 'Секретные', 'Secrete', 'Geheim')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1871, 'trophies_sign_in', 'Sign in to see your trophies.', 'ACTIVE', SYSDATE, 'Sign in to see your trophies.', 'Войди, чтобы увидеть свои трофеи.', 'Autentifică-te ca să-ți vezi trofeele.', 'Melde dich an, um deine Trophäen zu sehen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1872, 'trophy_chosen', 'Shown beside your name', 'ACTIVE', SYSDATE, 'Shown beside your name', 'Показан рядом с твоим именем', 'Afișat lângă numele tău', 'Wird neben deinem Namen angezeigt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1873, 'trophy_none', 'None', 'ACTIVE', SYSDATE, 'None', 'Нет', 'Niciunul', 'Keine')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1874, 'trophy_secret', 'Secret trophy', 'ACTIVE', SYSDATE, 'Secret trophy', 'Секретный трофей', 'Trofeu secret', 'Geheime Trophäe')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1875, 'trophy_secret_hint', 'Found by playing, not by looking', 'ACTIVE', SYSDATE, 'Found by playing, not by looking', 'Находится в игре, а не в поисках', 'Se găsește jucând, nu căutând', 'Wird beim Spielen gefunden, nicht beim Suchen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1876, 'type_hint_in_order', 'Players put the items in the order you write them.', 'ACTIVE', SYSDATE, 'Players put the items in the order you write them.', 'Игроки расставляют элементы в том порядке, в котором ты их записал(а).', 'Jucătorii pun elementele în ordinea în care le scrii tu.', 'Die Spieler bringen die Elemente in die Reihenfolge, in der du sie schreibst.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1877, 'type_hint_input', 'Players type the answer; any accepted spelling counts, whatever the case.', 'ACTIVE', SYSDATE, 'Players type the answer; any accepted spelling counts, whatever the case.', 'Игроки вводят ответ; засчитывается любое допустимое написание, независимо от регистра.', 'Jucătorii scriu răspunsul; orice formă acceptată contează, indiferent de majuscule.', 'Die Spieler tippen die Antwort ein; jede akzeptierte Schreibweise zählt, egal ob groß oder klein.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1878, 'type_hint_multiple', 'Players pick every right answer.', 'ACTIVE', SYSDATE, 'Players pick every right answer.', 'Игроки выбирают все правильные ответы.', 'Jucătorii aleg toate răspunsurile corecte.', 'Die Spieler wählen alle richtigen Antworten.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1879, 'type_hint_one_from_two', 'Players choose between the right answer and one wrong option.', 'ACTIVE', SYSDATE, 'Players choose between the right answer and one wrong option.', 'Игроки выбирают между правильным ответом и одним неверным вариантом.', 'Jucătorii aleg între răspunsul corect și o variantă greșită.', 'Die Spieler wählen zwischen der richtigen Antwort und einer falschen Option.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1880, 'type_hint_single', 'Players pick the one right answer.', 'ACTIVE', SYSDATE, 'Players pick the one right answer.', 'Игроки выбирают единственный правильный ответ.', 'Jucătorii aleg singurul răspuns corect.', 'Die Spieler wählen die eine richtige Antwort.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1881, 'type_your_answer', 'Type your answer', 'ACTIVE', SYSDATE, 'Type your answer', 'Введи свой ответ', 'Scrie răspunsul tău', 'Gib deine Antwort ein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1882, 'unmute_group', 'Muted — click to get notifications again', 'ACTIVE', SYSDATE, 'Muted — click to get notifications again', 'Без звука — нажми, чтобы снова получать уведомления', 'Fără notificări — apasă ca să le primești din nou', 'Stummgeschaltet — klicke, um wieder Benachrichtigungen zu erhalten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1883, 'versus_waiting', 'Not played yet', 'ACTIVE', SYSDATE, 'Not played yet', 'Ещё не сыграно', 'Încă nejucat', 'Noch nicht gespielt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1884, 'welcome_back', 'Welcome back', 'ACTIVE', SYSDATE, 'Welcome back', 'С возвращением', 'Bine ai revenit', 'Willkommen zurück')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1885, 'welcome_back_name', 'Welcome back, {{name}}', 'ACTIVE', SYSDATE, 'Welcome back, {{name}}', 'С возвращением, {{name}}', 'Bine ai revenit, {{name}}', 'Willkommen zurück, {{name}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1886, 'wrong_answers', 'Wrong', 'ACTIVE', SYSDATE, 'Wrong', 'Неверно', 'Greșite', 'Falsch')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1887, 'wrong_option', 'Wrong option', 'ACTIVE', SYSDATE, 'Wrong option', 'Неверный вариант', 'Variantă greșită', 'Falsche Option')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1888, 'wrong_option_number', 'Wrong option {{number}}', 'ACTIVE', SYSDATE, 'Wrong option {{number}}', 'Неверный вариант {{number}}', 'Varianta greșită {{number}}', 'Falsche Option {{number}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1889, 'wrong_option_placeholder', 'e.g. Balti', 'ACTIVE', SYSDATE, 'e.g. Balti', 'например, Бэлць', 'de ex. Bălți', 'z. B. Bălți')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1890, 'wrong_options', 'Wrong options', 'ACTIVE', SYSDATE, 'Wrong options', 'Неверные варианты', 'Variante greșite', 'Falsche Optionen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1891, 'xp_to_next', '{{left}} XP to level {{next}}', 'ACTIVE', SYSDATE, '{{left}} XP to level {{next}}', '{{left}} XP до уровня {{next}}', '{{left}} XP până la nivelul {{next}}', '{{left}} XP bis Level {{next}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1892, 'you', 'You', 'ACTIVE', SYSDATE, 'You', 'Ты', 'Tu', 'Du')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1893, 'your_answer', 'Your answer', 'ACTIVE', SYSDATE, 'Your answer', 'Твой ответ', 'Răspunsul tău', 'Deine Antwort')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1894, 'zoom_in', 'Zoom in', 'ACTIVE', SYSDATE, 'Zoom in', 'Приблизить', 'Mărește', 'Vergrößern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (1895, 'zoom_out', 'Zoom out', 'ACTIVE', SYSDATE, 'Zoom out', 'Отдалить', 'Micșorează', 'Verkleinern')
/execute/

-- //@UNDO
DELETE FROM Q_TRANSLATION WHERE ID BETWEEN 1000 AND 1899
/execute/
