-- // add_server_text_translations
-- What the server writes for a player — error messages, trophy names and conditions, the reason
-- a conquest country cannot be played, the activation email — read through ServerText in the
-- request's language (the site sends it as Accept-Language), from these rows.
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3000, 'conquest_blocked_closed', 'Conquest is closed today', 'SERVER', 'ACTIVE', SYSDATE, 'Conquest is closed today', 'Сегодня Завоевание закрыто', 'Cucerirea este închisă azi', 'Die Eroberung ist heute geschlossen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3001, 'conquest_blocked_cooldown', 'Another go in {{hours}}h {{minutes}}m', 'SERVER', 'ACTIVE', SYSDATE, 'Another go in {{hours}}h {{minutes}}m', 'Следующая попытка через {{hours}} ч {{minutes}} мин', 'Altă încercare în {{hours}}h {{minutes}}m', 'Nächster Versuch in {{hours}} Std. {{minutes}} Min.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3002, 'conquest_blocked_not_open', 'Not open this round', 'SERVER', 'ACTIVE', SYSDATE, 'Not open this round', 'Не открыта в этом раунде', 'Nu e deschisă în această rundă', 'In dieser Runde nicht offen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3003, 'conquest_blocked_sign_in', 'Sign in to take part', 'SERVER', 'ACTIVE', SYSDATE, 'Sign in to take part', 'Войди, чтобы участвовать', 'Conectează-te ca să participi', 'Melde dich an, um mitzumachen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3004, 'email_activate_instruction', 'To activate your account please use the following link:', 'SERVER', 'ACTIVE', SYSDATE, 'To activate your account please use the following link:', 'Чтобы активировать аккаунт, перейди по ссылке:', 'Pentru a-ți activa contul, folosește linkul următor:', 'Um dein Konto zu aktivieren, nutze bitte den folgenden Link:')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3005, 'email_activate_link', 'Activate account', 'SERVER', 'ACTIVE', SYSDATE, 'Activate account', 'Активировать аккаунт', 'Activează contul', 'Konto aktivieren')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3006, 'email_activate_subject', 'Activate your account', 'SERVER', 'ACTIVE', SYSDATE, 'Activate your account', 'Активируй свой аккаунт', 'Activează-ți contul', 'Aktiviere dein Konto')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3007, 'email_greeting', 'Hello human!', 'SERVER', 'ACTIVE', SYSDATE, 'Hello human!', 'Привет, человек!', 'Salut, omule!', 'Hallo, Mensch!')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3008, 'email_regards', 'Kind Regards,', 'SERVER', 'ACTIVE', SYSDATE, 'Kind Regards,', 'С наилучшими пожеланиями,', 'Cu drag,', 'Viele Grüße,')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3009, 'email_team', 'The Play Quiz Team', 'SERVER', 'ACTIVE', SYSDATE, 'The Play Quiz Team', 'Команда Play Quiz', 'Echipa Play Quiz', 'Dein Play-Quiz-Team')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3010, 'err_accent_colour', 'The accent must be a colour like #00a8e8', 'SERVER', 'ACTIVE', SYSDATE, 'The accent must be a colour like #00a8e8', 'Акцентный цвет должен быть цветом вида #00a8e8', 'Culoarea de accent trebuie să fie de forma #00a8e8', 'Die Akzentfarbe muss eine Farbe wie #00a8e8 sein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3011, 'err_accent_text_colour', 'The text on the accent must be a colour like #ffffff', 'SERVER', 'ACTIVE', SYSDATE, 'The text on the accent must be a colour like #ffffff', 'Цвет текста на акценте должен быть вида #ffffff', 'Culoarea textului pe accent trebuie să fie de forma #ffffff', 'Die Textfarbe auf dem Akzent muss eine Farbe wie #ffffff sein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3012, 'err_access_denied', 'You are not allowed to do that.', 'SERVER', 'ACTIVE', SYSDATE, 'You are not allowed to do that.', 'У тебя нет прав на это действие.', 'Nu ai voie să faci asta.', 'Das darfst du nicht.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3013, 'err_account_blocked', 'Account {{email}} has been blocked by an administrator', 'SERVER', 'ACTIVE', SYSDATE, 'Account {{email}} has been blocked by an administrator', 'Аккаунт {{email}} заблокирован администратором', 'Contul {{email}} a fost blocat de un administrator', 'Das Konto {{email}} wurde von einem Administrator gesperrt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3014, 'err_account_disabled', 'Account {{email}} is disabled', 'SERVER', 'ACTIVE', SYSDATE, 'Account {{email}} is disabled', 'Аккаунт {{email}} отключён', 'Contul {{email}} este dezactivat', 'Das Konto {{email}} ist deaktiviert')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3015, 'err_appearance_not_stored', 'Could not store the appearance', 'SERVER', 'ACTIVE', SYSDATE, 'Could not store the appearance', 'Не удалось сохранить оформление', 'Aspectul nu a putut fi salvat', 'Das Erscheinungsbild konnte nicht gespeichert werden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3016, 'err_bad_login', 'Wrong email or password', 'SERVER', 'ACTIVE', SYSDATE, 'Wrong email or password', 'Неверный email или пароль', 'Email sau parolă greșită', 'Falsche E-Mail oder falsches Passwort')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3017, 'err_block_self', 'You cannot block your own account', 'SERVER', 'ACTIVE', SYSDATE, 'You cannot block your own account', 'Нельзя заблокировать собственный аккаунт', 'Nu îți poți bloca propriul cont', 'Du kannst dein eigenes Konto nicht sperren')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3018, 'err_category_no_questions', 'This category has no questions yet', 'SERVER', 'ACTIVE', SYSDATE, 'This category has no questions yet', 'В этой категории пока нет вопросов', 'Această categorie nu are încă întrebări', 'Diese Kategorie hat noch keine Fragen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3019, 'err_category_own_parent', 'A category cannot be its own parent', 'SERVER', 'ACTIVE', SYSDATE, 'A category cannot be its own parent', 'Категория не может быть родителем самой себя', 'O categorie nu poate fi propriul părinte', 'Eine Kategorie kann nicht ihre eigene Elternkategorie sein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3020, 'err_challenge_not_yours', 'That challenge is not yours', 'SERVER', 'ACTIVE', SYSDATE, 'That challenge is not yours', 'Это не твой вызов', 'Provocarea aceasta nu este a ta', 'Diese Herausforderung gehört nicht dir')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3021, 'err_challenge_only_friends', 'You can only challenge your friends', 'SERVER', 'ACTIVE', SYSDATE, 'You can only challenge your friends', 'Бросать вызов можно только друзьям', 'Poți provoca doar prietenii tăi', 'Du kannst nur deine Freunde herausfordern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3022, 'err_conquest_closed', 'Conquest is closed today; it opens again {{at}}', 'SERVER', 'ACTIVE', SYSDATE, 'Conquest is closed today; it opens again {{at}}', 'Сегодня Завоевание закрыто; оно снова откроется {{at}}', 'Cucerirea este închisă azi; se redeschide {{at}}', 'Die Eroberung ist heute geschlossen; sie öffnet wieder {{at}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3023, 'err_conquest_cooldown', 'Another go at {{country}} in {{hours}}h {{minutes}}m', 'SERVER', 'ACTIVE', SYSDATE, 'Another go at {{country}} in {{hours}}h {{minutes}}m', 'Следующая попытка за {{country}} через {{hours}} ч {{minutes}} мин', 'Altă încercare la {{country}} în {{hours}}h {{minutes}}m', 'Nächster Versuch bei {{country}} in {{hours}} Std. {{minutes}} Min.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3024, 'err_conquest_not_your_group', 'You can only play for a group you are in', 'SERVER', 'ACTIVE', SYSDATE, 'You can only play for a group you are in', 'Играть можно только за группу, в которой ты состоишь', 'Poți juca doar pentru un grup din care faci parte', 'Du kannst nur für eine Gruppe spielen, in der du bist')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3025, 'err_country_not_open', '{{country}} is not open this round', 'SERVER', 'ACTIVE', SYSDATE, '{{country}} is not open this round', '{{country}} не открыта в этом раунде', '{{country}} nu este deschisă în această rundă', '{{country}} ist in dieser Runde nicht offen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3026, 'err_crypto_address_pattern', 'A wallet address is 10 to 130 letters and digits', 'SERVER', 'ACTIVE', SYSDATE, 'A wallet address is 10 to 130 letters and digits', 'Адрес кошелька — от 10 до 130 букв и цифр', 'O adresă de portofel are între 10 și 130 de litere și cifre', 'Eine Wallet-Adresse besteht aus 10 bis 130 Buchstaben und Ziffern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3027, 'err_custom_quiz_delete_denied', 'Only the quiz''s creator and the admins can delete it', 'SERVER', 'ACTIVE', SYSDATE, 'Only the quiz''s creator and the admins can delete it', 'Удалить викторину могут только её автор и администраторы', 'Doar creatorul quizului și administratorii îl pot șterge', 'Nur der Ersteller des Quiz und die Admins können es löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3028, 'err_custom_quiz_own_page', 'A custom quiz is played from its own page', 'SERVER', 'ACTIVE', SYSDATE, 'A custom quiz is played from its own page', 'Пользовательская викторина проходится на своей странице', 'Un quiz personalizat se joacă de pe pagina lui', 'Ein eigenes Quiz wird auf seiner eigenen Seite gespielt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3029, 'err_custom_quiz_play_denied', 'Only the quiz''s creator and the people invited can play it', 'SERVER', 'ACTIVE', SYSDATE, 'Only the quiz''s creator and the people invited can play it', 'Играть в эту викторину могут только её автор и приглашённые', 'Doar creatorul quizului și persoanele invitate îl pot juca', 'Nur der Ersteller des Quiz und eingeladene Personen können es spielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3030, 'err_custom_quiz_share', 'A custom quiz is shared by inviting friends to it', 'SERVER', 'ACTIVE', SYSDATE, 'A custom quiz is shared by inviting friends to it', 'Пользовательской викториной делятся, приглашая в неё друзей', 'Un quiz personalizat se distribuie invitând prieteni la el', 'Ein eigenes Quiz teilst du, indem du Freunde dazu einlädst')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3031, 'err_daily_challenge_played', 'You have played today''s challenge; a new one starts tomorrow', 'SERVER', 'ACTIVE', SYSDATE, 'You have played today''s challenge; a new one starts tomorrow', 'Ты уже прошёл сегодняшний вызов; новый начнётся завтра', 'Ai jucat deja provocarea de azi; una nouă începe mâine', 'Du hast die heutige Challenge schon gespielt; morgen startet eine neue')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3032, 'err_delete_post_denied', 'Only its author or an admin can delete post {{id}}', 'SERVER', 'ACTIVE', SYSDATE, 'Only its author or an admin can delete post {{id}}', 'Удалить пост {{id}} может только его автор или администратор', 'Doar autorul sau un administrator poate șterge postarea {{id}}', 'Nur der Autor oder ein Admin kann den Beitrag {{id}} löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3033, 'err_delete_self', 'You cannot delete your own account', 'SERVER', 'ACTIVE', SYSDATE, 'You cannot delete your own account', 'Нельзя удалить собственный аккаунт', 'Nu îți poți șterge propriul cont', 'Du kannst dein eigenes Konto nicht löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3034, 'err_donation_not_stored', 'Donation settings could not be stored', 'SERVER', 'ACTIVE', SYSDATE, 'Donation settings could not be stored', 'Не удалось сохранить настройки пожертвований', 'Setările pentru donații nu au putut fi salvate', 'Die Spendeneinstellungen konnten nicht gespeichert werden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3035, 'err_donation_too_much', 'Too much to store: remove a wallet or shorten the names', 'SERVER', 'ACTIVE', SYSDATE, 'Too much to store: remove a wallet or shorten the names', 'Слишком много данных: удалите кошелёк или сократите названия', 'Prea multe date de salvat: eliminați un portofel sau scurtați numele', 'Zu viel zum Speichern: Entferne eine Wallet oder kürze die Namen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3036, 'err_duel_one_friend', 'A duel is against one friend', 'SERVER', 'ACTIVE', SYSDATE, 'A duel is against one friend', 'Дуэль проводится против одного друга', 'Un duel se joacă împotriva unui singur prieten', 'Ein Duell ist gegen genau einen Freund')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3037, 'err_duel_other_players', 'That duel is between two other players', 'SERVER', 'ACTIVE', SYSDATE, 'That duel is between two other players', 'Это дуэль двух других игроков', 'Duelul acesta este între alți doi jucători', 'Dieses Duell ist zwischen zwei anderen Spielern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3038, 'err_email_email', 'Please provide a valid email address', 'SERVER', 'ACTIVE', SYSDATE, 'Please provide a valid email address', 'Укажи действительный email', 'Te rugăm să introduci o adresă de email validă', 'Bitte gib eine gültige E-Mail-Adresse an')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3039, 'err_email_not_sent', 'The email could not be sent. Please try again later.', 'SERVER', 'ACTIVE', SYSDATE, 'The email could not be sent. Please try again later.', 'Не удалось отправить письмо. Попробуй позже.', 'Emailul nu a putut fi trimis. Încearcă din nou mai târziu.', 'Die E-Mail konnte nicht gesendet werden. Bitte versuche es später erneut.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3040, 'err_feedback_rate_limited', 'You have sent several messages in a short time. Please try again in a few minutes.', 'SERVER', 'ACTIVE', SYSDATE, 'You have sent several messages in a short time. Please try again in a few minutes.', 'Ты отправил несколько сообщений за короткое время. Попробуй снова через несколько минут.', 'Ai trimis mai multe mesaje într-un timp scurt. Încearcă din nou peste câteva minute.', 'Du hast in kurzer Zeit mehrere Nachrichten gesendet. Bitte versuche es in ein paar Minuten erneut.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3041, 'err_friend_self', 'You cannot add yourself as a friend', 'SERVER', 'ACTIVE', SYSDATE, 'You cannot add yourself as a friend', 'Нельзя добавить в друзья самого себя', 'Nu te poți adăuga singur ca prieten', 'Du kannst dich nicht selbst als Freund hinzufügen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3042, 'err_glossary_answer', 'This glossary is the answer to {{count}} questions. Delete or change those questions first.', 'SERVER', 'ACTIVE', SYSDATE, 'This glossary is the answer to {{count}} questions. Delete or change those questions first.', 'Этот глоссарий является ответом на вопросы ({{count}}). Сначала удалите или измените эти вопросы.', 'Acest glosar este răspunsul la {{count}} întrebări. Ștergeți sau modificați mai întâi acele întrebări.', 'Dieser Glossar-Eintrag ist die Antwort auf {{count}} Fragen. Lösche oder ändere zuerst diese Fragen.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3043, 'err_glossary_answer_one', 'This glossary is the answer to 1 question. Delete or change those questions first.', 'SERVER', 'ACTIVE', SYSDATE, 'This glossary is the answer to 1 question. Delete or change those questions first.', 'Этот глоссарий является ответом на 1 вопрос. Сначала удалите или измените этот вопрос.', 'Acest glosar este răspunsul la 1 întrebare. Ștergeți sau modificați mai întâi acea întrebare.', 'Dieser Glossar-Eintrag ist die Antwort auf 1 Frage. Lösche oder ändere zuerst diese Frage.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3044, 'err_glossary_not_saved', 'Exception during glossary saving', 'SERVER', 'ACTIVE', SYSDATE, 'Exception during glossary saving', 'Ошибка при сохранении глоссария', 'Eroare la salvarea glosarului', 'Fehler beim Speichern des Glossars')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3045, 'err_glossary_parent', 'This glossary is the parent of {{count}} other glossaries. Move or delete them first.', 'SERVER', 'ACTIVE', SYSDATE, 'This glossary is the parent of {{count}} other glossaries. Move or delete them first.', 'Этот глоссарий является родителем для других глоссариев ({{count}}). Сначала переместите или удалите их.', 'Acest glosar este părintele altor {{count}} glosare. Mutați-le sau ștergeți-le mai întâi.', 'Dieser Glossar-Eintrag ist übergeordnet für {{count}} andere Einträge. Verschiebe oder lösche sie zuerst.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3046, 'err_glossary_parent_one', 'This glossary is the parent of 1 other glossary. Move or delete them first.', 'SERVER', 'ACTIVE', SYSDATE, 'This glossary is the parent of 1 other glossary. Move or delete them first.', 'Этот глоссарий является родителем для 1 другого глоссария. Сначала переместите или удалите его.', 'Acest glosar este părintele unui alt glosar. Mutați-l sau ștergeți-l mai întâi.', 'Dieser Glossar-Eintrag ist übergeordnet für 1 anderen Eintrag. Verschiebe oder lösche ihn zuerst.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3047, 'err_glossary_type_used', 'This type is used by {{count}} glossaries. Change their type first.', 'SERVER', 'ACTIVE', SYSDATE, 'This type is used by {{count}} glossaries. Change their type first.', 'Этот тип используется глоссариями ({{count}}). Сначала измените их тип.', 'Acest tip este folosit de {{count}} glosare. Schimbați mai întâi tipul lor.', 'Dieser Typ wird von {{count}} Glossar-Einträgen verwendet. Ändere zuerst deren Typ.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3048, 'err_glossary_type_used_one', 'This type is used by 1 glossary. Change their type first.', 'SERVER', 'ACTIVE', SYSDATE, 'This type is used by 1 glossary. Change their type first.', 'Этот тип используется 1 глоссарием. Сначала измените его тип.', 'Acest tip este folosit de 1 glosar. Schimbați mai întâi tipul lui.', 'Dieser Typ wird von 1 Glossar-Eintrag verwendet. Ändere zuerst dessen Typ.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3049, 'err_group_delete_denied', 'Only members can delete group {{id}}', 'SERVER', 'ACTIVE', SYSDATE, 'Only members can delete group {{id}}', 'Удалить группу {{id}} могут только её участники', 'Doar membrii pot șterge grupul {{id}}', 'Nur Mitglieder können die Gruppe {{id}} löschen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3050, 'err_group_mute_denied', 'Only members can mute group {{id}}', 'SERVER', 'ACTIVE', SYSDATE, 'Only members can mute group {{id}}', 'Отключить уведомления группы {{id}} могут только её участники', 'Doar membrii pot pune pe silențios grupul {{id}}', 'Nur Mitglieder können die Gruppe {{id}} stummschalten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3051, 'err_group_needs_participant', 'A group needs at least one other participant', 'SERVER', 'ACTIVE', SYSDATE, 'A group needs at least one other participant', 'В группе должен быть хотя бы ещё один участник', 'Un grup are nevoie de cel puțin încă un participant', 'Eine Gruppe braucht mindestens einen weiteren Teilnehmer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3052, 'err_group_picture_denied', 'Only members can set the picture of group {{id}}', 'SERVER', 'ACTIVE', SYSDATE, 'Only members can set the picture of group {{id}}', 'Установить картинку группы {{id}} могут только её участники', 'Doar membrii pot seta imaginea grupului {{id}}', 'Nur Mitglieder können das Bild der Gruppe {{id}} festlegen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3053, 'err_group_picture_not_image', 'A group picture has to be an image', 'SERVER', 'ACTIVE', SYSDATE, 'A group picture has to be an image', 'Картинка группы должна быть изображением', 'Imaginea grupului trebuie să fie o imagine', 'Ein Gruppenbild muss ein Bild sein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3054, 'err_group_picture_unreadable', 'Could not read the group picture', 'SERVER', 'ACTIVE', SYSDATE, 'Could not read the group picture', 'Не удалось прочитать картинку группы', 'Imaginea grupului nu a putut fi citită', 'Das Gruppenbild konnte nicht gelesen werden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3055, 'err_invite_only_friends', 'You can only invite your friends', 'SERVER', 'ACTIVE', SYSDATE, 'You can only invite your friends', 'Приглашать можно только друзей', 'Poți invita doar prietenii tăi', 'Du kannst nur deine Freunde einladen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3056, 'err_join_room_first', 'Join the room to see it', 'SERVER', 'ACTIVE', SYSDATE, 'Join the room to see it', 'Войди в комнату, чтобы её увидеть', 'Intră în cameră ca s-o vezi', 'Tritt dem Raum bei, um ihn zu sehen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3057, 'err_match_started', 'The match has already started', 'SERVER', 'ACTIVE', SYSDATE, 'The match has already started', 'Матч уже начался', 'Meciul a început deja', 'Das Match hat bereits begonnen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3058, 'err_match_started_that', 'That match has already started', 'SERVER', 'ACTIVE', SYSDATE, 'That match has already started', 'Этот матч уже начался', 'Meciul acela a început deja', 'Dieses Match hat bereits begonnen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3059, 'err_max_streak_freezes', 'You already hold {{max}} streak freezes', 'SERVER', 'ACTIVE', SYSDATE, 'You already hold {{max}} streak freezes', 'У тебя уже есть {{max}} заморозок серии', 'Ai deja {{max}} înghețări ale seriei', 'Du hast bereits {{max}} Serien-Freezes')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3060, 'err_message_change_denied', 'Only the author can change message {{id}}', 'SERVER', 'ACTIVE', SYSDATE, 'Only the author can change message {{id}}', 'Изменить сообщение {{id}} может только его автор', 'Doar autorul poate modifica mesajul {{id}}', 'Nur der Autor kann die Nachricht {{id}} ändern')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3061, 'err_message_empty', 'A message cannot be empty', 'SERVER', 'ACTIVE', SYSDATE, 'A message cannot be empty', 'Сообщение не может быть пустым', 'Un mesaj nu poate fi gol', 'Eine Nachricht darf nicht leer sein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3062, 'err_message_too_long', 'A message can be at most {{max}} characters long', 'SERVER', 'ACTIVE', SYSDATE, 'A message can be at most {{max}} characters long', 'Сообщение может содержать не более {{max}} символов', 'Un mesaj poate avea cel mult {{max}} de caractere', 'Eine Nachricht darf höchstens {{max}} Zeichen lang sein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3063, 'err_new_account_password', 'A new account needs a password', 'SERVER', 'ACTIVE', SYSDATE, 'A new account needs a password', 'Для нового аккаунта нужен пароль', 'Un cont nou are nevoie de o parolă', 'Ein neues Konto braucht ein Passwort')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3064, 'err_no_questions_yet', 'There are no questions to play yet', 'SERVER', 'ACTIVE', SYSDATE, 'There are no questions to play yet', 'Пока нет вопросов для игры', 'Încă nu există întrebări de jucat', 'Es gibt noch keine Fragen zum Spielen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3065, 'err_not_enough_coins', 'Not enough coins: this costs {{price}}', 'SERVER', 'ACTIVE', SYSDATE, 'Not enough coins: this costs {{price}}', 'Недостаточно монет: это стоит {{price}}', 'Nu ai destule monede: costă {{price}}', 'Nicht genug Münzen: Das kostet {{price}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3066, 'err_not_found', 'This could not be found. It may have been deleted.', 'SERVER', 'ACTIVE', SYSDATE, 'This could not be found. It may have been deleted.', 'Не найдено. Возможно, это было удалено.', 'Nu a fost găsit. Este posibil să fi fost șters.', 'Das wurde nicht gefunden. Vielleicht wurde es gelöscht.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3067, 'err_not_group_member', 'Not a member of group {{id}}', 'SERVER', 'ACTIVE', SYSDATE, 'Not a member of group {{id}}', 'Ты не участник группы {{id}}', 'Nu ești membru al grupului {{id}}', 'Du bist kein Mitglied der Gruppe {{id}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3068, 'err_not_in_group', 'You are not in that group', 'SERVER', 'ACTIVE', SYSDATE, 'You are not in that group', 'Ты не состоишь в этой группе', 'Nu faci parte din acel grup', 'Du bist nicht in dieser Gruppe')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3069, 'err_not_news_line', 'Not a news line: {{key}}', 'SERVER', 'ACTIVE', SYSDATE, 'Not a news line: {{key}}', 'Это не строка новостей: {{key}}', 'Nu este o știre: {{key}}', 'Keine Nachrichtenzeile: {{key}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3070, 'err_not_playing_match', 'You are not playing in that match', 'SERVER', 'ACTIVE', SYSDATE, 'You are not playing in that match', 'Ты не участвуешь в этом матче', 'Nu joci în acest meci', 'Du spielst in diesem Match nicht mit')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3071, 'err_nothing_to_take_away', 'Nothing left to take away', 'SERVER', 'ACTIVE', SYSDATE, 'Nothing left to take away', 'Больше нечего убирать', 'Nu mai e nimic de eliminat', 'Es gibt nichts mehr wegzunehmen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3072, 'err_only_host_starts', 'Only the host starts the match', 'SERVER', 'ACTIVE', SYSDATE, 'Only the host starts the match', 'Только хозяин комнаты может начать матч', 'Doar gazda poate începe meciul', 'Nur der Host kann das Match starten')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3073, 'err_only_news_hidden', 'Only news can be hidden: {{type}}', 'SERVER', 'ACTIVE', SYSDATE, 'Only news can be hidden: {{type}}', 'Скрывать можно только новости: {{type}}', 'Doar știrile pot fi ascunse: {{type}}', 'Nur Nachrichten können ausgeblendet werden: {{type}}')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3074, 'err_password_common', 'That password is too common and easy to guess: choose another', 'SERVER', 'ACTIVE', SYSDATE, 'That password is too common and easy to guess: choose another', 'Этот пароль слишком распространён и легко угадывается: выбери другой', 'Parola este prea comună și ușor de ghicit: alege alta', 'Dieses Passwort ist zu verbreitet und leicht zu erraten: wähle ein anderes')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3075, 'err_password_not_updated', 'User password could not be updated', 'SERVER', 'ACTIVE', SYSDATE, 'User password could not be updated', 'Не удалось обновить пароль пользователя', 'Parola utilizatorului nu a putut fi actualizată', 'Das Passwort des Benutzers konnte nicht aktualisiert werden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3076, 'err_password_weak', 'The password must be at least {{min}} characters and mix at least {{kinds}} of: lowercase, uppercase, digits, symbols', 'SERVER', 'ACTIVE', SYSDATE, 'The password must be at least {{min}} characters and mix at least {{kinds}} of: lowercase, uppercase, digits, symbols', 'Пароль должен содержать не менее {{min}} символов и сочетать хотя бы {{kinds}} из: строчные буквы, заглавные буквы, цифры, символы', 'Parola trebuie să aibă cel puțin {{min}} caractere și să combine cel puțin {{kinds}} dintre: litere mici, litere mari, cifre, simboluri', 'Das Passwort muss mindestens {{min}} Zeichen lang sein und mindestens {{kinds}} dieser Arten mischen: Kleinbuchstaben, Großbuchstaben, Ziffern, Sonderzeichen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3077, 'err_paypal_link_pattern', 'The PayPal link must start with https://', 'SERVER', 'ACTIVE', SYSDATE, 'The PayPal link must start with https://', 'Ссылка PayPal должна начинаться с https://', 'Linkul PayPal trebuie să înceapă cu https://', 'Der PayPal-Link muss mit https:// beginnen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3078, 'err_post_needs_title', 'A post needs a title', 'SERVER', 'ACTIVE', SYSDATE, 'A post needs a title', 'У публикации должен быть заголовок', 'O postare trebuie să aibă un titlu', 'Ein Beitrag braucht einen Titel')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3079, 'err_post_too_long', 'A post can have at most {{title}} characters of title and {{text}} of text', 'SERVER', 'ACTIVE', SYSDATE, 'A post can have at most {{title}} characters of title and {{text}} of text', 'В публикации может быть не более {{title}} символов в заголовке и {{text}} в тексте', 'O postare poate avea cel mult {{title}} caractere în titlu și {{text}} în text', 'Ein Beitrag darf höchstens {{title}} Zeichen im Titel und {{text}} im Text haben')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3080, 'err_question_no_hint', 'This question has no hint', 'SERVER', 'ACTIVE', SYSDATE, 'This question has no hint', 'У этого вопроса нет подсказки', 'Această întrebare nu are indiciu', 'Diese Frage hat keinen Hinweis')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3081, 'err_question_not_template', 'Question [{{id}}] is not of type TEMPLATE', 'SERVER', 'ACTIVE', SYSDATE, 'Question [{{id}}] is not of type TEMPLATE', 'Вопрос [{{id}}] не относится к типу TEMPLATE', 'Întrebarea [{{id}}] nu este de tip TEMPLATE', 'Frage [{{id}}] ist nicht vom Typ TEMPLATE')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3082, 'err_question_unfit_type', '"{{question}}" does not fit a {{type}} quiz', 'SERVER', 'ACTIVE', SYSDATE, '"{{question}}" does not fit a {{type}} quiz', '«{{question}}» не подходит для викторины типа {{type}}', '„{{question}}” nu se potrivește unui quiz de tip {{type}}', '„{{question}}“ passt nicht zu einem {{type}}-Quiz')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3083, 'err_quiz_type_not_custom', 'This quiz type is not available for a custom quiz', 'SERVER', 'ACTIVE', SYSDATE, 'This quiz type is not available for a custom quiz', 'Этот тип викторины недоступен для собственной викторины', 'Acest tip de quiz nu este disponibil pentru un quiz personalizat', 'Dieser Quiztyp ist für ein eigenes Quiz nicht verfügbar')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3084, 'err_roles_notempty', 'must give the account at least one role', 'SERVER', 'ACTIVE', SYSDATE, 'must give the account at least one role', 'у аккаунта должна быть хотя бы одна роль', 'contul trebuie să aibă cel puțin un rol', 'dem Konto muss mindestens eine Rolle zugewiesen werden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3085, 'err_room_full', 'That room is full', 'SERVER', 'ACTIVE', SYSDATE, 'That room is full', 'Эта комната заполнена', 'Camera este plină', 'Dieser Raum ist voll')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3086, 'err_run_already_entered', 'That quiz run has already been entered', 'SERVER', 'ACTIVE', SYSDATE, 'That quiz run has already been entered', 'Это прохождение викторины уже заявлено', 'Această rundă de quiz a fost deja înscrisă', 'Dieser Quizdurchlauf wurde bereits eingereicht')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3087, 'err_run_no_score_to_beat', 'That quiz run has no score to beat', 'SERVER', 'ACTIVE', SYSDATE, 'That quiz run has no score to beat', 'У этого прохождения викторины нет результата, который можно побить', 'Această rundă de quiz nu are un scor de depășit', 'Dieser Quizdurchlauf hat kein Ergebnis, das man schlagen könnte')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3088, 'err_run_no_score_to_enter', 'That quiz run has no score to enter', 'SERVER', 'ACTIVE', SYSDATE, 'That quiz run has no score to enter', 'У этого прохождения викторины нет результата для заявки', 'Această rundă de quiz nu are un scor de înscris', 'Dieser Quizdurchlauf hat kein Ergebnis zum Einreichen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3089, 'err_run_not_yours', 'That quiz run is not yours', 'SERVER', 'ACTIVE', SYSDATE, 'That quiz run is not yours', 'Это прохождение викторины не твоё', 'Această rundă de quiz nu este a ta', 'Dieser Quizdurchlauf gehört nicht dir')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3090, 'err_screenshot_not_image', 'A screenshot has to be an image', 'SERVER', 'ACTIVE', SYSDATE, 'A screenshot has to be an image', 'Скриншот должен быть изображением', 'Captura de ecran trebuie să fie o imagine', 'Ein Screenshot muss ein Bild sein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3091, 'err_screenshot_unreadable', 'Could not read the screenshot', 'SERVER', 'ACTIVE', SYSDATE, 'Could not read the screenshot', 'Не удалось прочитать скриншот', 'Captura de ecran nu a putut fi citită', 'Der Screenshot konnte nicht gelesen werden')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3092, 'err_server', 'Something went wrong on our side. Please try again.', 'SERVER', 'ACTIVE', SYSDATE, 'Something went wrong on our side. Please try again.', 'У нас что-то пошло не так. Попробуй ещё раз.', 'Ceva nu a mers bine la noi. Încearcă din nou.', 'Bei uns ist etwas schiefgelaufen. Bitte versuch es noch einmal.')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3093, 'err_text_size', 'The text size must be one of {{sizes}}', 'SERVER', 'ACTIVE', SYSDATE, 'The text size must be one of {{sizes}}', 'Размер текста должен быть одним из: {{sizes}}', 'Dimensiunea textului trebuie să fie una dintre {{sizes}}', 'Die Textgröße muss eine von {{sizes}} sein')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3094, 'err_trophy_not_earned', 'That trophy has not been earned', 'SERVER', 'ACTIVE', SYSDATE, 'That trophy has not been earned', 'Этот трофей ещё не получен', 'Acest trofeu nu a fost câștigat', 'Diese Trophäe wurde noch nicht verdient')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3095, 'err_user_exists', 'User {{email}} already exists', 'SERVER', 'ACTIVE', SYSDATE, 'User {{email}} already exists', 'Пользователь {{email}} уже существует', 'Utilizatorul {{email}} există deja', 'Benutzer {{email}} existiert bereits')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3096, 'err_wait_for_player', 'Wait for someone to join first', 'SERVER', 'ACTIVE', SYSDATE, 'Wait for someone to join first', 'Сначала дождись, пока кто-нибудь присоединится', 'Așteaptă mai întâi să se alăture cineva', 'Warte zuerst, bis jemand beitritt')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3097, 'trophy_category_desc', 'Finish a quiz in {{category}}', 'SERVER', 'ACTIVE', SYSDATE, 'Finish a quiz in {{category}}', 'Пройди викторину в категории {{category}}', 'Termină un quiz din categoria {{category}}', 'Schließe ein Quiz in {{category}} ab')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3098, 'trophy_clean_sweep_desc', 'Finish all of a day''s tasks in one day', 'SERVER', 'ACTIVE', SYSDATE, 'Finish all of a day''s tasks in one day', 'Выполни все задания дня за один день', 'Termină toate sarcinile unei zile în aceeași zi', 'Erledige alle Aufgaben eines Tages an einem Tag')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3099, 'trophy_clean_sweep_title', 'Clean sweep', 'SERVER', 'ACTIVE', SYSDATE, 'Clean sweep', 'Чистая победа', 'Curățenie generală', 'Reiner Tisch')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3100, 'trophy_conqueror_desc', 'Hold a country when a conquest round closes', 'SERVER', 'ACTIVE', SYSDATE, 'Hold a country when a conquest round closes', 'Удерживай страну в момент закрытия раунда Завоевания', 'Deține o țară când se încheie o rundă de Cucerire', 'Halte ein Land, wenn eine Eroberungsrunde endet')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3101, 'trophy_conqueror_title', 'Conqueror', 'SERVER', 'ACTIVE', SYSDATE, 'Conqueror', 'Завоеватель', 'Cuceritor', 'Eroberer')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3102, 'trophy_emperor_desc', 'Conquer 10 countries', 'SERVER', 'ACTIVE', SYSDATE, 'Conquer 10 countries', 'Завоюй 10 стран', 'Cucerește 10 țări', 'Erobere 10 Länder')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3103, 'trophy_emperor_title', 'Emperor', 'SERVER', 'ACTIVE', SYSDATE, 'Emperor', 'Император', 'Împărat', 'Kaiser')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3104, 'trophy_every_category_desc', 'Finish a quiz in every category', 'SERVER', 'ACTIVE', SYSDATE, 'Finish a quiz in every category', 'Пройди викторину в каждой категории', 'Termină un quiz din fiecare categorie', 'Schließe in jeder Kategorie ein Quiz ab')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3105, 'trophy_every_category_title', 'Round the houses', 'SERVER', 'ACTIVE', SYSDATE, 'Round the houses', 'Везде побывал', 'Peste tot pe unde', 'Überall zu Hause')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3106, 'trophy_fifty_quizzes_desc', 'Finish 50 quizzes', 'SERVER', 'ACTIVE', SYSDATE, 'Finish 50 quizzes', 'Пройди 50 викторин', 'Termină 50 de quizuri', 'Schließe 50 Quiz ab')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3107, 'trophy_fifty_quizzes_title', 'Regular', 'SERVER', 'ACTIVE', SYSDATE, 'Regular', 'Завсегдатай', 'Obișnuit al casei', 'Stammgast')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3108, 'trophy_first_quiz_desc', 'Finish your first quiz', 'SERVER', 'ACTIVE', SYSDATE, 'Finish your first quiz', 'Пройди свою первую викторину', 'Termină primul tău quiz', 'Schließe dein erstes Quiz ab')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3109, 'trophy_first_quiz_title', 'First steps', 'SERVER', 'ACTIVE', SYSDATE, 'First steps', 'Первые шаги', 'Primii pași', 'Erste Schritte')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3110, 'trophy_five_hundred_quizzes_desc', 'Finish 500 quizzes', 'SERVER', 'ACTIVE', SYSDATE, 'Finish 500 quizzes', 'Пройди 500 викторин', 'Termină 500 de quizuri', 'Schließe 500 Quiz ab')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3111, 'trophy_five_hundred_quizzes_title', 'Encyclopaedia', 'SERVER', 'ACTIVE', SYSDATE, 'Encyclopaedia', 'Ходячая энциклопедия', 'Enciclopedie', 'Enzyklopädie')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3112, 'trophy_flawless_desc', 'Finish a quiz without a single wrong answer', 'SERVER', 'ACTIVE', SYSDATE, 'Finish a quiz without a single wrong answer', 'Пройди викторину без единой ошибки', 'Termină un quiz fără niciun răspuns greșit', 'Schließe ein Quiz ohne eine einzige falsche Antwort ab')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3113, 'trophy_flawless_title', 'Flawless', 'SERVER', 'ACTIVE', SYSDATE, 'Flawless', 'Безупречно', 'Impecabil', 'Makellos')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3114, 'trophy_hundred_quizzes_desc', 'Finish 100 quizzes', 'SERVER', 'ACTIVE', SYSDATE, 'Finish 100 quizzes', 'Пройди 100 викторин', 'Termină 100 de quizuri', 'Schließe 100 Quiz ab')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3115, 'trophy_hundred_quizzes_title', 'Centurion', 'SERVER', 'ACTIVE', SYSDATE, 'Centurion', 'Центурион', 'Centurion', 'Zenturio')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3116, 'trophy_measured_desc', 'Sit the IQ test through to the end', 'SERVER', 'ACTIVE', SYSDATE, 'Sit the IQ test through to the end', 'Пройди тест IQ до конца', 'Parcurge testul IQ până la capăt', 'Mach den IQ-Test bis zum Ende')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3117, 'trophy_measured_title', 'Measured', 'SERVER', 'ACTIVE', SYSDATE, 'Measured', 'Измерено', 'Măsurat', 'Vermessen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3118, 'trophy_night_owl_desc', 'Finish a quiz between two and five in the morning', 'SERVER', 'ACTIVE', SYSDATE, 'Finish a quiz between two and five in the morning', 'Пройди викторину между двумя и пятью часами ночи', 'Termină un quiz între două și cinci dimineața', 'Schließe ein Quiz zwischen zwei und fünf Uhr morgens ab')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3119, 'trophy_night_owl_title', 'Night owl', 'SERVER', 'ACTIVE', SYSDATE, 'Night owl', 'Ночная сова', 'Bufniță de noapte', 'Nachteule')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3120, 'trophy_play_streak_7_desc', 'Finish a quiz on 7 days in a row', 'SERVER', 'ACTIVE', SYSDATE, 'Finish a quiz on 7 days in a row', 'Проходи викторину 7 дней подряд', 'Termină un quiz 7 zile la rând', 'Schließe an 7 Tagen in Folge ein Quiz ab')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3121, 'trophy_play_streak_7_title', 'Seven days of play', 'SERVER', 'ACTIVE', SYSDATE, 'Seven days of play', 'Семь дней игры', 'Șapte zile de joc', 'Sieben Tage Spiel')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3122, 'trophy_quick_off_the_mark_desc', 'Finish a quiz of 10 questions or more in under a minute', 'SERVER', 'ACTIVE', SYSDATE, 'Finish a quiz of 10 questions or more in under a minute', 'Пройди викторину из 10 или более вопросов меньше чем за минуту', 'Termină un quiz de 10 întrebări sau mai multe în mai puțin de un minut', 'Schließe ein Quiz mit 10 oder mehr Fragen in unter einer Minute ab')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3123, 'trophy_quick_off_the_mark_title', 'Quick off the mark', 'SERVER', 'ACTIVE', SYSDATE, 'Quick off the mark', 'Молниеносный старт', 'Rapid ca fulgerul', 'Blitzstart')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3124, 'trophy_sharp_desc', 'Score 130 or more on the IQ test', 'SERVER', 'ACTIVE', SYSDATE, 'Score 130 or more on the IQ test', 'Набери 130 или больше в тесте IQ', 'Obține 130 sau mai mult la testul IQ', 'Erreiche 130 oder mehr im IQ-Test')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3125, 'trophy_sharp_title', 'Sharp', 'SERVER', 'ACTIVE', SYSDATE, 'Sharp', 'Острый ум', 'Minte ageră', 'Messerscharf')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3126, 'trophy_streak_100_desc', 'Visit on 100 days in a row', 'SERVER', 'ACTIVE', SYSDATE, 'Visit on 100 days in a row', 'Заходи 100 дней подряд', 'Intră 100 de zile la rând', 'Schau an 100 Tagen in Folge vorbei')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3127, 'trophy_streak_100_title', 'Unbroken', 'SERVER', 'ACTIVE', SYSDATE, 'Unbroken', 'Несокрушимый', 'Neîntrerupt', 'Ungebrochen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3128, 'trophy_streak_3_desc', 'Visit on 3 days in a row', 'SERVER', 'ACTIVE', SYSDATE, 'Visit on 3 days in a row', 'Заходи 3 дня подряд', 'Intră 3 zile la rând', 'Schau an 3 Tagen in Folge vorbei')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3129, 'trophy_streak_3_title', 'Three in a row', 'SERVER', 'ACTIVE', SYSDATE, 'Three in a row', 'Три подряд', 'Trei la rând', 'Drei am Stück')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3130, 'trophy_streak_30_desc', 'Visit on 30 days in a row', 'SERVER', 'ACTIVE', SYSDATE, 'Visit on 30 days in a row', 'Заходи 30 дней подряд', 'Intră 30 de zile la rând', 'Schau an 30 Tagen in Folge vorbei')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3131, 'trophy_streak_30_title', 'A month of days', 'SERVER', 'ACTIVE', SYSDATE, 'A month of days', 'Целый месяц', 'O lună întreagă', 'Ein ganzer Monat')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3132, 'trophy_streak_7_desc', 'Visit on 7 days in a row', 'SERVER', 'ACTIVE', SYSDATE, 'Visit on 7 days in a row', 'Заходи 7 дней подряд', 'Intră 7 zile la rând', 'Schau an 7 Tagen in Folge vorbei')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3133, 'trophy_streak_7_title', 'A full week', 'SERVER', 'ACTIVE', SYSDATE, 'A full week', 'Целая неделя', 'O săptămână întreagă', 'Eine ganze Woche')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3134, 'trophy_ten_quizzes_desc', 'Finish 10 quizzes', 'SERVER', 'ACTIVE', SYSDATE, 'Finish 10 quizzes', 'Пройди 10 викторин', 'Termină 10 quizuri', 'Schließe 10 Quiz ab')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3135, 'trophy_ten_quizzes_title', 'Getting the hang of it', 'SERVER', 'ACTIVE', SYSDATE, 'Getting the hang of it', 'Входишь во вкус', 'Prinzi gustul', 'Auf den Geschmack gekommen')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3136, 'trophy_warlord_desc', 'Conquer 5 countries', 'SERVER', 'ACTIVE', SYSDATE, 'Conquer 5 countries', 'Завоюй 5 стран', 'Cucerește 5 țări', 'Erobere 5 Länder')
/execute/
INSERT INTO Q_TRANSLATION (ID, "KEY", DEFAULT_VALUE, T_GROUP, STATUS, CREATED_DATE, EN, RU, RO, DE)
VALUES (3137, 'trophy_warlord_title', 'Warlord', 'SERVER', 'ACTIVE', SYSDATE, 'Warlord', 'Полководец', 'Căpetenie', 'Kriegsherr')
/execute/

-- //@UNDO
DELETE FROM Q_TRANSLATION WHERE T_GROUP = 'SERVER'
/execute/
