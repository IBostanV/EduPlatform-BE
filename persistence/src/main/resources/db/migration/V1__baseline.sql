-- Postgres baseline: the schema as it was in Oracle on 2026-10-09 (all of the MyBatis/Oracle
-- migrations up to 20261008110000), converted with ora2pg and typed to match the entities.

CREATE SEQUENCE announcement_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE answers_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE answer_transl_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE categories_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE cat_transl_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE challenge_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE client_error_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE conquest_attempt_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE custom_answer_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE custom_question_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE daily_task_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE duel_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE feedback_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE glossaries_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE glossary_type_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE group_post_comment_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE group_post_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE iq_item_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE iq_response_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE iq_session_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE knowledge_base_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE knowledge_base_transl_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE languages_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE level_up_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE message_group_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE message_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE mistake_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE news_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE occupation_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE property_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE questions_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE question_transl_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE quiz_invite_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE quiz_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE quiz_type_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE reaction_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE roles_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE social_group_member_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE social_group_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE term_transl_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE translation_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE trophies_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE users_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE user_group_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE user_history_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE user_trophy_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;
CREATE SEQUENCE verification_tokens_seq INCREMENT 1 MINVALUE 1 NO MAXVALUE START 1;

CREATE TABLE q_announcement (
	announcement_id bigint NOT NULL,
	title varchar(200) NOT NULL,
	content varchar(4000),
	created_by bigint,
	created_date timestamp(6) NOT NULL
) ;
ALTER TABLE q_announcement ADD PRIMARY KEY (announcement_id);
ALTER TABLE q_announcement ALTER COLUMN announcement_id SET NOT NULL;
ALTER TABLE q_announcement ALTER COLUMN title SET NOT NULL;
ALTER TABLE q_announcement ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_answer (
	ans_id bigint NOT NULL,
	question_id bigint NOT NULL,
	content varchar(1000) NOT NULL,
	term_id bigint,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_answer ADD PRIMARY KEY (ans_id);
ALTER TABLE q_answer ALTER COLUMN ans_id SET NOT NULL;
ALTER TABLE q_answer ALTER COLUMN question_id SET NOT NULL;
ALTER TABLE q_answer ALTER COLUMN content SET NOT NULL;
ALTER TABLE q_answer ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_answer_translations (
	transl_id bigint NOT NULL,
	name varchar(150),
	description varchar(150) NOT NULL,
	lang_id bigint,
	ans_id bigint NOT NULL
) ;
ALTER TABLE q_answer_translations ADD PRIMARY KEY (transl_id);
ALTER TABLE q_answer_translations ALTER COLUMN transl_id SET NOT NULL;
ALTER TABLE q_answer_translations ALTER COLUMN description SET NOT NULL;
ALTER TABLE q_answer_translations ALTER COLUMN ans_id SET NOT NULL;

CREATE TABLE q_category (
	cat_id bigint NOT NULL,
	name varchar(150),
	natural_id varchar(150) NOT NULL,
	subcategory_id bigint,
	attachment bytea,
	visible boolean DEFAULT true,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_category ADD PRIMARY KEY (cat_id);
ALTER TABLE q_category ADD UNIQUE (name);
ALTER TABLE q_category ALTER COLUMN cat_id SET NOT NULL;
ALTER TABLE q_category ALTER COLUMN natural_id SET NOT NULL;
ALTER TABLE q_category ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_category_translations (
	transl_id bigint NOT NULL,
	name varchar(150),
	description varchar(150) NOT NULL,
	lang_id bigint,
	cat_id bigint NOT NULL
) ;
ALTER TABLE q_category_translations ADD PRIMARY KEY (transl_id);
ALTER TABLE q_category_translations ALTER COLUMN transl_id SET NOT NULL;
ALTER TABLE q_category_translations ALTER COLUMN description SET NOT NULL;
ALTER TABLE q_category_translations ALTER COLUMN cat_id SET NOT NULL;

CREATE TABLE q_challenge (
	challenge_id bigint NOT NULL,
	quiz_id bigint NOT NULL,
	challenger_id bigint NOT NULL,
	challenger_history_id bigint NOT NULL,
	opponent_id bigint NOT NULL,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
CREATE INDEX q_challenge_challenger_ix ON q_challenge (challenger_id);
CREATE INDEX q_challenge_opponent_ix ON q_challenge (opponent_id);
ALTER TABLE q_challenge ADD UNIQUE (quiz_id,opponent_id);
ALTER TABLE q_challenge ADD PRIMARY KEY (challenge_id);
ALTER TABLE q_challenge ALTER COLUMN challenge_id SET NOT NULL;
ALTER TABLE q_challenge ALTER COLUMN quiz_id SET NOT NULL;
ALTER TABLE q_challenge ALTER COLUMN challenger_id SET NOT NULL;
ALTER TABLE q_challenge ALTER COLUMN challenger_history_id SET NOT NULL;
ALTER TABLE q_challenge ALTER COLUMN opponent_id SET NOT NULL;
ALTER TABLE q_challenge ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_client_error (
	client_error_id bigint NOT NULL,
	kind varchar(50) NOT NULL,
	message varchar(2000) NOT NULL,
	stack text,
	page varchar(500),
	user_agent varchar(500),
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_client_error ADD PRIMARY KEY (client_error_id);
ALTER TABLE q_client_error ALTER COLUMN client_error_id SET NOT NULL;
ALTER TABLE q_client_error ALTER COLUMN kind SET NOT NULL;
ALTER TABLE q_client_error ALTER COLUMN message SET NOT NULL;
ALTER TABLE q_client_error ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_conquest_attempt (
	attempt_id bigint NOT NULL,
	round_no bigint NOT NULL,
	cat_id bigint NOT NULL,
	account_id bigint NOT NULL,
	history_id bigint NOT NULL,
	right_answers integer NOT NULL,
	total_answers integer NOT NULL,
	spent_time double precision DEFAULT 0,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6),
	bonus_paid boolean NOT NULL DEFAULT false
) ;
CREATE INDEX q_conquest_attempt_ix ON q_conquest_attempt (cat_id, round_no);
ALTER TABLE q_conquest_attempt ADD UNIQUE (history_id);
ALTER TABLE q_conquest_attempt ADD PRIMARY KEY (attempt_id);
ALTER TABLE q_conquest_attempt ALTER COLUMN attempt_id SET NOT NULL;
ALTER TABLE q_conquest_attempt ALTER COLUMN round_no SET NOT NULL;
ALTER TABLE q_conquest_attempt ALTER COLUMN cat_id SET NOT NULL;
ALTER TABLE q_conquest_attempt ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_conquest_attempt ALTER COLUMN history_id SET NOT NULL;
ALTER TABLE q_conquest_attempt ALTER COLUMN right_answers SET NOT NULL;
ALTER TABLE q_conquest_attempt ALTER COLUMN total_answers SET NOT NULL;
ALTER TABLE q_conquest_attempt ALTER COLUMN created_date SET NOT NULL;
ALTER TABLE q_conquest_attempt ALTER COLUMN bonus_paid SET NOT NULL;

CREATE TABLE q_custom_answer (
	answer_id bigint NOT NULL,
	question_id bigint NOT NULL,
	content varchar(1000) NOT NULL,
	is_right boolean NOT NULL,
	position integer NOT NULL,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_custom_answer ADD PRIMARY KEY (answer_id);
ALTER TABLE q_custom_answer ALTER COLUMN answer_id SET NOT NULL;
ALTER TABLE q_custom_answer ALTER COLUMN question_id SET NOT NULL;
ALTER TABLE q_custom_answer ALTER COLUMN content SET NOT NULL;
ALTER TABLE q_custom_answer ALTER COLUMN is_right SET NOT NULL;
ALTER TABLE q_custom_answer ALTER COLUMN position SET NOT NULL;
ALTER TABLE q_custom_answer ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_custom_question (
	question_id bigint NOT NULL,
	content varchar(1000) NOT NULL,
	position integer NOT NULL,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_custom_question ADD PRIMARY KEY (question_id);
ALTER TABLE q_custom_question ALTER COLUMN question_id SET NOT NULL;
ALTER TABLE q_custom_question ALTER COLUMN content SET NOT NULL;
ALTER TABLE q_custom_question ALTER COLUMN position SET NOT NULL;
ALTER TABLE q_custom_question ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_daily_challenge (
	challenge_day date NOT NULL,
	quiz_id bigint NOT NULL
) ;
ALTER TABLE q_daily_challenge ADD PRIMARY KEY (challenge_day);
ALTER TABLE q_daily_challenge ALTER COLUMN challenge_day SET NOT NULL;
ALTER TABLE q_daily_challenge ALTER COLUMN quiz_id SET NOT NULL;

CREATE TABLE q_daily_task (
	task_id bigint NOT NULL,
	account_id bigint NOT NULL,
	task_code varchar(50) NOT NULL,
	task_day date NOT NULL,
	experience integer NOT NULL,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(0)
) ;
ALTER TABLE q_daily_task ADD PRIMARY KEY (task_id);
ALTER TABLE q_daily_task ADD UNIQUE (account_id,task_code,task_day);
ALTER TABLE q_daily_task ALTER COLUMN task_id SET NOT NULL;
ALTER TABLE q_daily_task ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_daily_task ALTER COLUMN task_code SET NOT NULL;
ALTER TABLE q_daily_task ALTER COLUMN task_day SET NOT NULL;
ALTER TABLE q_daily_task ALTER COLUMN experience SET NOT NULL;
ALTER TABLE q_daily_task ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_duel (
	duel_id bigint NOT NULL,
	challenger_id bigint NOT NULL,
	opponent_id bigint NOT NULL,
	created_date timestamp(6) NOT NULL
) ;
ALTER TABLE q_duel ADD PRIMARY KEY (duel_id);
ALTER TABLE q_duel ALTER COLUMN duel_id SET NOT NULL;
ALTER TABLE q_duel ALTER COLUMN challenger_id SET NOT NULL;
ALTER TABLE q_duel ALTER COLUMN opponent_id SET NOT NULL;
ALTER TABLE q_duel ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_duel_round (
	duel_id bigint NOT NULL,
	round_no integer NOT NULL,
	quiz_id bigint NOT NULL,
	created_date timestamp(6) NOT NULL
) ;
ALTER TABLE q_duel_round ADD PRIMARY KEY (duel_id,round_no);
ALTER TABLE q_duel_round ALTER COLUMN duel_id SET NOT NULL;
ALTER TABLE q_duel_round ALTER COLUMN round_no SET NOT NULL;
ALTER TABLE q_duel_round ALTER COLUMN quiz_id SET NOT NULL;
ALTER TABLE q_duel_round ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_feedback (
	feedback_id bigint NOT NULL,
	type varchar(20) NOT NULL,
	message varchar(2000) NOT NULL,
	page varchar(500),
	resolved boolean NOT NULL DEFAULT false,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6),
	contact_email varchar(254),
	question varchar(4000),
	screenshot bytea,
	screenshot_type varchar(100)
) ;
ALTER TABLE q_feedback ADD PRIMARY KEY (feedback_id);
ALTER TABLE q_feedback ALTER COLUMN feedback_id SET NOT NULL;
ALTER TABLE q_feedback ALTER COLUMN type SET NOT NULL;
ALTER TABLE q_feedback ALTER COLUMN message SET NOT NULL;
ALTER TABLE q_feedback ALTER COLUMN resolved SET NOT NULL;
ALTER TABLE q_feedback ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_glossary (
	term_id bigint NOT NULL,
	key varchar(100) NOT NULL,
	value varchar(100),
	cat_id bigint NOT NULL,
	type_id bigint,
	attachment bytea,
	options varchar(100),
	parent_id bigint,
	is_active boolean NOT NULL DEFAULT false,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_glossary ADD PRIMARY KEY (term_id);
ALTER TABLE q_glossary ADD UNIQUE (key,value,cat_id);
ALTER TABLE q_glossary ALTER COLUMN term_id SET NOT NULL;
ALTER TABLE q_glossary ALTER COLUMN key SET NOT NULL;
ALTER TABLE q_glossary ALTER COLUMN cat_id SET NOT NULL;
ALTER TABLE q_glossary ALTER COLUMN is_active SET NOT NULL;
ALTER TABLE q_glossary ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_glossary_translations (
	transl_id bigint NOT NULL,
	name varchar(150),
	description varchar(150) NOT NULL,
	lang_id bigint,
	term_id bigint NOT NULL
) ;
ALTER TABLE q_glossary_translations ADD PRIMARY KEY (transl_id);
ALTER TABLE q_glossary_translations ALTER COLUMN transl_id SET NOT NULL;
ALTER TABLE q_glossary_translations ALTER COLUMN description SET NOT NULL;
ALTER TABLE q_glossary_translations ALTER COLUMN term_id SET NOT NULL;

CREATE TABLE q_glossary_type (
	id bigint NOT NULL,
	name varchar(100),
	options varchar(100),
	is_active boolean NOT NULL DEFAULT false,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_glossary_type ADD PRIMARY KEY (id);
ALTER TABLE q_glossary_type ALTER COLUMN id SET NOT NULL;
ALTER TABLE q_glossary_type ALTER COLUMN is_active SET NOT NULL;
ALTER TABLE q_glossary_type ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_group_post (
	post_id bigint NOT NULL,
	group_id bigint NOT NULL,
	account_id bigint NOT NULL,
	content varchar(4000) NOT NULL,
	created_date timestamp(6) NOT NULL
) ;
CREATE INDEX q_group_post_group_ix ON q_group_post (group_id, created_date);
ALTER TABLE q_group_post ADD PRIMARY KEY (post_id);
ALTER TABLE q_group_post ALTER COLUMN post_id SET NOT NULL;
ALTER TABLE q_group_post ALTER COLUMN group_id SET NOT NULL;
ALTER TABLE q_group_post ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_group_post ALTER COLUMN content SET NOT NULL;
ALTER TABLE q_group_post ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_group_post_comment (
	comment_id bigint NOT NULL,
	post_id bigint NOT NULL,
	account_id bigint NOT NULL,
	content varchar(1000) NOT NULL,
	created_date timestamp(6) NOT NULL
) ;
CREATE INDEX q_group_post_comment_post_ix ON q_group_post_comment (post_id, created_date);
ALTER TABLE q_group_post_comment ADD PRIMARY KEY (comment_id);
ALTER TABLE q_group_post_comment ALTER COLUMN comment_id SET NOT NULL;
ALTER TABLE q_group_post_comment ALTER COLUMN post_id SET NOT NULL;
ALTER TABLE q_group_post_comment ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_group_post_comment ALTER COLUMN content SET NOT NULL;
ALTER TABLE q_group_post_comment ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_iq_item (
	item_id bigint NOT NULL,
	code varchar(50) NOT NULL,
	item_type varchar(20) NOT NULL,
	payload text NOT NULL,
	answer_index integer NOT NULL,
	difficulty double precision NOT NULL,
	attempts integer NOT NULL DEFAULT 0,
	correct_count integer NOT NULL DEFAULT 0,
	theta_sum double precision NOT NULL DEFAULT 0,
	created_date timestamp(6) NOT NULL
) ;
ALTER TABLE q_iq_item ADD PRIMARY KEY (item_id);
ALTER TABLE q_iq_item ADD UNIQUE (code);
ALTER TABLE q_iq_item ALTER COLUMN item_id SET NOT NULL;
ALTER TABLE q_iq_item ALTER COLUMN code SET NOT NULL;
ALTER TABLE q_iq_item ALTER COLUMN item_type SET NOT NULL;
ALTER TABLE q_iq_item ALTER COLUMN payload SET NOT NULL;
ALTER TABLE q_iq_item ALTER COLUMN answer_index SET NOT NULL;
ALTER TABLE q_iq_item ALTER COLUMN difficulty SET NOT NULL;
ALTER TABLE q_iq_item ALTER COLUMN attempts SET NOT NULL;
ALTER TABLE q_iq_item ALTER COLUMN correct_count SET NOT NULL;
ALTER TABLE q_iq_item ALTER COLUMN theta_sum SET NOT NULL;
ALTER TABLE q_iq_item ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_iq_response (
	response_id bigint NOT NULL,
	session_id bigint NOT NULL,
	item_id bigint NOT NULL,
	chosen_index integer NOT NULL,
	correct boolean NOT NULL,
	seconds integer NOT NULL
) ;
CREATE INDEX q_iq_response_ix ON q_iq_response (session_id);
ALTER TABLE q_iq_response ADD PRIMARY KEY (response_id);
ALTER TABLE q_iq_response ALTER COLUMN response_id SET NOT NULL;
ALTER TABLE q_iq_response ALTER COLUMN session_id SET NOT NULL;
ALTER TABLE q_iq_response ALTER COLUMN item_id SET NOT NULL;
ALTER TABLE q_iq_response ALTER COLUMN chosen_index SET NOT NULL;
ALTER TABLE q_iq_response ALTER COLUMN correct SET NOT NULL;
ALTER TABLE q_iq_response ALTER COLUMN seconds SET NOT NULL;

CREATE TABLE q_iq_session (
	session_id bigint NOT NULL,
	account_id bigint NOT NULL,
	started_date timestamp(6) NOT NULL,
	finished_date timestamp(6),
	current_item_id bigint,
	served_date timestamp(6),
	answered integer NOT NULL DEFAULT 0,
	theta double precision,
	standard_error double precision,
	iq integer,
	percentile integer,
	normed boolean NOT NULL DEFAULT false,
	attempt_no integer NOT NULL DEFAULT 0
) ;
CREATE INDEX q_iq_session_ix ON q_iq_session (account_id, finished_date);
CREATE INDEX q_iq_session_norm_ix ON q_iq_session (attempt_no, theta);
ALTER TABLE q_iq_session ADD PRIMARY KEY (session_id);
ALTER TABLE q_iq_session ALTER COLUMN session_id SET NOT NULL;
ALTER TABLE q_iq_session ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_iq_session ALTER COLUMN started_date SET NOT NULL;
ALTER TABLE q_iq_session ALTER COLUMN answered SET NOT NULL;
ALTER TABLE q_iq_session ALTER COLUMN normed SET NOT NULL;
ALTER TABLE q_iq_session ALTER COLUMN attempt_no SET NOT NULL;

CREATE TABLE q_knowledge_base (
	id bigint NOT NULL,
	cat_id bigint NOT NULL,
	title varchar(150),
	content varchar(4000),
	parent_id bigint,
	attachment bytea,
	visible boolean DEFAULT true,
	upvotes integer,
	downvotes integer,
	tags varchar(300),
	status varchar(20),
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_knowledge_base ADD PRIMARY KEY (id);
ALTER TABLE q_knowledge_base ALTER COLUMN id SET NOT NULL;
ALTER TABLE q_knowledge_base ALTER COLUMN cat_id SET NOT NULL;
ALTER TABLE q_knowledge_base ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_knowledge_base_translation (
	transl_id bigint NOT NULL,
	name varchar(150),
	description varchar(150) NOT NULL,
	lang_id bigint,
	k_base_id bigint NOT NULL
) ;
ALTER TABLE q_knowledge_base_translation ADD PRIMARY KEY (transl_id);
ALTER TABLE q_knowledge_base_translation ALTER COLUMN transl_id SET NOT NULL;
ALTER TABLE q_knowledge_base_translation ALTER COLUMN description SET NOT NULL;
ALTER TABLE q_knowledge_base_translation ALTER COLUMN k_base_id SET NOT NULL;

CREATE TABLE q_language (
	lang_id bigint NOT NULL,
	lang_code varchar(20),
	name varchar(100) NOT NULL,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_language ADD PRIMARY KEY (lang_id);
ALTER TABLE q_language ALTER COLUMN lang_id SET NOT NULL;
ALTER TABLE q_language ALTER COLUMN name SET NOT NULL;
ALTER TABLE q_language ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_level_up (
	level_up_id bigint NOT NULL,
	account_id bigint NOT NULL,
	level_no integer NOT NULL,
	reached_date timestamp(6) NOT NULL
) ;
CREATE INDEX q_level_up_account_ix ON q_level_up (account_id, reached_date);
ALTER TABLE q_level_up ADD PRIMARY KEY (level_up_id);
ALTER TABLE q_level_up ALTER COLUMN level_up_id SET NOT NULL;
ALTER TABLE q_level_up ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_level_up ALTER COLUMN level_no SET NOT NULL;
ALTER TABLE q_level_up ALTER COLUMN reached_date SET NOT NULL;

CREATE TABLE q_message (
	msg_id bigint NOT NULL,
	content varchar(800),
	source varchar(100) NOT NULL,
	destination bigint NOT NULL,
	session_id varchar(20),
	attachment bytea,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_message ADD PRIMARY KEY (msg_id);
ALTER TABLE q_message ALTER COLUMN msg_id SET NOT NULL;
ALTER TABLE q_message ALTER COLUMN source SET NOT NULL;
ALTER TABLE q_message ALTER COLUMN destination SET NOT NULL;
ALTER TABLE q_message ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_message_group (
	group_id bigint NOT NULL,
	name varchar(150),
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6),
	photo bytea,
	photo_type varchar(100)
) ;
ALTER TABLE q_message_group ADD PRIMARY KEY (group_id);
ALTER TABLE q_message_group ALTER COLUMN group_id SET NOT NULL;
ALTER TABLE q_message_group ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_mistake (
	mistake_id bigint NOT NULL,
	account_id bigint NOT NULL,
	question_id bigint NOT NULL,
	box integer NOT NULL DEFAULT 0,
	due_date date NOT NULL
) ;
ALTER TABLE q_mistake ADD PRIMARY KEY (mistake_id);
ALTER TABLE q_mistake ADD UNIQUE (account_id,question_id);
ALTER TABLE q_mistake ALTER COLUMN mistake_id SET NOT NULL;
ALTER TABLE q_mistake ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_mistake ALTER COLUMN question_id SET NOT NULL;
ALTER TABLE q_mistake ALTER COLUMN box SET NOT NULL;
ALTER TABLE q_mistake ALTER COLUMN due_date SET NOT NULL;

CREATE TABLE q_news (
	news_id bigint NOT NULL,
	title varchar(200) NOT NULL,
	content varchar(4000),
	created_by bigint,
	created_date timestamp(6) NOT NULL,
	is_patch boolean NOT NULL DEFAULT true
) ;
ALTER TABLE q_news ADD PRIMARY KEY (news_id);
ALTER TABLE q_news ALTER COLUMN news_id SET NOT NULL;
ALTER TABLE q_news ALTER COLUMN title SET NOT NULL;
ALTER TABLE q_news ALTER COLUMN created_date SET NOT NULL;
ALTER TABLE q_news ALTER COLUMN is_patch SET NOT NULL;

CREATE TABLE q_occupation (
	id bigint NOT NULL,
	name varchar(150),
	domain varchar(150),
	parent_id bigint,
	status varchar(20),
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_occupation ADD PRIMARY KEY (id);
ALTER TABLE q_occupation ALTER COLUMN id SET NOT NULL;
ALTER TABLE q_occupation ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_property (
	property_id bigint NOT NULL,
	name varchar(1000) NOT NULL,
	value varchar(800) NOT NULL,
	old_value varchar(800),
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_property ADD PRIMARY KEY (property_id);
ALTER TABLE q_property ALTER COLUMN property_id SET NOT NULL;
ALTER TABLE q_property ALTER COLUMN name SET NOT NULL;
ALTER TABLE q_property ALTER COLUMN value SET NOT NULL;
ALTER TABLE q_property ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_question (
	question_id bigint NOT NULL,
	account_id bigint NOT NULL DEFAULT 1,
	type varchar(50) NOT NULL,
	tip_id bigint,
	cat_id bigint NOT NULL,
	is_active boolean NOT NULL,
	complexity_level integer,
	content varchar(1000) NOT NULL,
	topic varchar(200),
	priority integer,
	attributes varchar(500),
	exclude_type bigint DEFAULT 0,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_question ADD PRIMARY KEY (question_id);
ALTER TABLE q_question ALTER COLUMN question_id SET NOT NULL;
ALTER TABLE q_question ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_question ALTER COLUMN type SET NOT NULL;
ALTER TABLE q_question ALTER COLUMN cat_id SET NOT NULL;
ALTER TABLE q_question ALTER COLUMN is_active SET NOT NULL;
ALTER TABLE q_question ALTER COLUMN content SET NOT NULL;
ALTER TABLE q_question ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_question_translations (
	transl_id bigint NOT NULL,
	name varchar(150),
	description varchar(150) NOT NULL,
	lang_id bigint,
	question_id bigint NOT NULL
) ;
ALTER TABLE q_question_translations ADD PRIMARY KEY (transl_id);
ALTER TABLE q_question_translations ALTER COLUMN transl_id SET NOT NULL;
ALTER TABLE q_question_translations ALTER COLUMN description SET NOT NULL;
ALTER TABLE q_question_translations ALTER COLUMN question_id SET NOT NULL;

CREATE TABLE q_quiz (
	quiz_id bigint NOT NULL,
	cat_id bigint,
	type bigint NOT NULL DEFAULT 1,
	question_ids varchar(4000),
	questions_count integer,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6),
	question_time integer,
	is_custom boolean NOT NULL DEFAULT false
) ;
ALTER TABLE q_quiz ADD PRIMARY KEY (quiz_id);
ALTER TABLE q_quiz ALTER COLUMN quiz_id SET NOT NULL;
ALTER TABLE q_quiz ALTER COLUMN type SET NOT NULL;
ALTER TABLE q_quiz ALTER COLUMN created_date SET NOT NULL;
ALTER TABLE q_quiz ALTER COLUMN is_custom SET NOT NULL;

CREATE TABLE q_quiz_invite (
	invite_id bigint NOT NULL,
	quiz_id bigint NOT NULL,
	account_id bigint NOT NULL,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_quiz_invite ADD PRIMARY KEY (invite_id);
ALTER TABLE q_quiz_invite ADD UNIQUE (quiz_id,account_id);
ALTER TABLE q_quiz_invite ALTER COLUMN invite_id SET NOT NULL;
ALTER TABLE q_quiz_invite ALTER COLUMN quiz_id SET NOT NULL;
ALTER TABLE q_quiz_invite ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_quiz_invite ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_quiz_type (
	id bigint NOT NULL,
	name varchar(800),
	is_active boolean DEFAULT true,
	description varchar(200),
	bit_value integer NOT NULL,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_quiz_type ADD PRIMARY KEY (id);
ALTER TABLE q_quiz_type ALTER COLUMN id SET NOT NULL;
ALTER TABLE q_quiz_type ALTER COLUMN bit_value SET NOT NULL;
ALTER TABLE q_quiz_type ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_reaction (
	reaction_id bigint NOT NULL,
	account_id bigint NOT NULL,
	item_key varchar(200) NOT NULL,
	kind varchar(20) NOT NULL,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
CREATE INDEX q_reaction_key_ix ON q_reaction (item_key);
ALTER TABLE q_reaction ADD PRIMARY KEY (reaction_id);
ALTER TABLE q_reaction ADD UNIQUE (account_id,item_key,kind);
ALTER TABLE q_reaction ALTER COLUMN reaction_id SET NOT NULL;
ALTER TABLE q_reaction ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_reaction ALTER COLUMN item_key SET NOT NULL;
ALTER TABLE q_reaction ALTER COLUMN kind SET NOT NULL;
ALTER TABLE q_reaction ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_role (
	role_id bigint NOT NULL,
	name varchar(100) NOT NULL,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_role ADD PRIMARY KEY (role_id);
ALTER TABLE q_role ALTER COLUMN role_id SET NOT NULL;
ALTER TABLE q_role ALTER COLUMN name SET NOT NULL;
ALTER TABLE q_role ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_season_claim (
	account_id bigint NOT NULL,
	code varchar(40) NOT NULL,
	period_start date NOT NULL,
	points integer NOT NULL DEFAULT 0,
	claimed_date timestamp(6) NOT NULL
) ;
ALTER TABLE q_season_claim ADD PRIMARY KEY (account_id,code,period_start);
ALTER TABLE q_season_claim ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_season_claim ALTER COLUMN code SET NOT NULL;
ALTER TABLE q_season_claim ALTER COLUMN period_start SET NOT NULL;
ALTER TABLE q_season_claim ALTER COLUMN points SET NOT NULL;
ALTER TABLE q_season_claim ALTER COLUMN claimed_date SET NOT NULL;

CREATE TABLE q_social_group (
	group_id bigint NOT NULL,
	name varchar(80) NOT NULL,
	description varchar(500),
	is_private boolean NOT NULL DEFAULT false,
	owner_id bigint NOT NULL,
	created_date timestamp(6) NOT NULL
) ;
ALTER TABLE q_social_group ADD PRIMARY KEY (group_id);
ALTER TABLE q_social_group ALTER COLUMN group_id SET NOT NULL;
ALTER TABLE q_social_group ALTER COLUMN name SET NOT NULL;
ALTER TABLE q_social_group ALTER COLUMN is_private SET NOT NULL;
ALTER TABLE q_social_group ALTER COLUMN owner_id SET NOT NULL;
ALTER TABLE q_social_group ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_social_group_member (
	member_id bigint NOT NULL,
	group_id bigint NOT NULL,
	account_id bigint NOT NULL,
	status varchar(10) NOT NULL,
	joined_date timestamp(6) NOT NULL
) ;
CREATE INDEX q_social_group_member_user_ix ON q_social_group_member (account_id);
ALTER TABLE q_social_group_member ADD UNIQUE (group_id,account_id);
ALTER TABLE q_social_group_member ADD PRIMARY KEY (member_id);
ALTER TABLE q_social_group_member ALTER COLUMN member_id SET NOT NULL;
ALTER TABLE q_social_group_member ALTER COLUMN group_id SET NOT NULL;
ALTER TABLE q_social_group_member ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_social_group_member ALTER COLUMN status SET NOT NULL;
ALTER TABLE q_social_group_member ALTER COLUMN joined_date SET NOT NULL;

CREATE TABLE q_topic (
	id bigint NOT NULL,
	name varchar(100) NOT NULL
) ;
ALTER TABLE q_topic ADD PRIMARY KEY (id);
ALTER TABLE q_topic ALTER COLUMN id SET NOT NULL;
ALTER TABLE q_topic ALTER COLUMN name SET NOT NULL;

CREATE TABLE q_translation (
	id bigint NOT NULL,
	key varchar(100) NOT NULL,
	default_value varchar(1000),
	t_group varchar(150),
	status varchar(20),
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6),
	en varchar(1000),
	ru varchar(1000),
	ro varchar(1000),
	de varchar(1000)
) ;
ALTER TABLE q_translation ADD PRIMARY KEY (id);
ALTER TABLE q_translation ALTER COLUMN id SET NOT NULL;
ALTER TABLE q_translation ALTER COLUMN key SET NOT NULL;
ALTER TABLE q_translation ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_user (
	account_id bigint NOT NULL,
	username varchar(200),
	name varchar(100),
	surname varchar(100),
	avatar bytea,
	experience integer,
	lang_id bigint,
	email varchar(100) NOT NULL,
	password varchar(100) NOT NULL,
	birthday date,
	is_enabled boolean NOT NULL DEFAULT false,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6),
	is_blocked boolean NOT NULL DEFAULT false,
	last_seen_date date,
	login_streak integer NOT NULL DEFAULT 0,
	best_streak integer NOT NULL DEFAULT 0,
	preferred_trophy varchar(80),
	notifications_read_at timestamp(6),
	hidden_news varchar(200),
	appearance varchar(1000),
	conquest_team bigint,
	coins integer NOT NULL DEFAULT 0,
	streak_freezes integer NOT NULL DEFAULT 0,
	occupation_quizzes boolean NOT NULL DEFAULT true,
	tour_seen boolean NOT NULL DEFAULT false,
	profile_visibility varchar(10) NOT NULL DEFAULT 'FRIENDS',
	equipped_frame varchar(40),
	equipped_name_color varchar(40),
	announcement_seen_id bigint NOT NULL DEFAULT 0
) ;
ALTER TABLE q_user ADD PRIMARY KEY (account_id);
ALTER TABLE q_user ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN email SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN password SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN is_enabled SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN created_date SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN is_blocked SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN login_streak SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN best_streak SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN coins SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN streak_freezes SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN occupation_quizzes SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN tour_seen SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN profile_visibility SET NOT NULL;
ALTER TABLE q_user ALTER COLUMN announcement_seen_id SET NOT NULL;

CREATE TABLE q_user_background (
	account_id bigint NOT NULL,
	public_id varchar(36) NOT NULL,
	content_type varchar(20) NOT NULL,
	image bytea NOT NULL,
	created_date timestamp(6) NOT NULL
) ;
ALTER TABLE q_user_background ADD UNIQUE (public_id);
ALTER TABLE q_user_background ADD PRIMARY KEY (account_id);
ALTER TABLE q_user_background ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_user_background ALTER COLUMN public_id SET NOT NULL;
ALTER TABLE q_user_background ALTER COLUMN content_type SET NOT NULL;
ALTER TABLE q_user_background ALTER COLUMN image SET NOT NULL;
ALTER TABLE q_user_background ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_user_category (
	account_id bigint NOT NULL,
	cat_id bigint NOT NULL
) ;
ALTER TABLE q_user_category ADD PRIMARY KEY (account_id,cat_id);
ALTER TABLE q_user_category ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_user_category ALTER COLUMN cat_id SET NOT NULL;

CREATE TABLE q_user_friend (
	user_id bigint NOT NULL,
	friend_id bigint NOT NULL,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(0) NOT NULL,
	updated_date timestamp(0)
) ;
ALTER TABLE q_user_friend ADD PRIMARY KEY (user_id,friend_id);
ALTER TABLE q_user_friend ALTER COLUMN user_id SET NOT NULL;
ALTER TABLE q_user_friend ALTER COLUMN friend_id SET NOT NULL;
ALTER TABLE q_user_friend ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_user_group (
	id bigint NOT NULL,
	group_id bigint NOT NULL,
	participant bigint NOT NULL,
	theme varchar(20),
	muted boolean DEFAULT false,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6)
) ;
ALTER TABLE q_user_group ADD PRIMARY KEY (id);
ALTER TABLE q_user_group ALTER COLUMN id SET NOT NULL;
ALTER TABLE q_user_group ALTER COLUMN group_id SET NOT NULL;
ALTER TABLE q_user_group ALTER COLUMN participant SET NOT NULL;
ALTER TABLE q_user_group ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_user_history (
	history_id bigint NOT NULL,
	account_id bigint,
	quiz_id bigint NOT NULL,
	answers_json text,
	completed_date timestamp(6),
	spent_time double precision DEFAULT 0,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(6) NOT NULL,
	updated_date timestamp(6),
	right_answers integer,
	total_answers integer
) ;
ALTER TABLE q_user_history ADD PRIMARY KEY (history_id);
ALTER TABLE q_user_history ALTER COLUMN history_id SET NOT NULL;
ALTER TABLE q_user_history ALTER COLUMN quiz_id SET NOT NULL;
ALTER TABLE q_user_history ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_user_item (
	account_id bigint NOT NULL,
	item_code varchar(40) NOT NULL,
	acquired_date timestamp(6) NOT NULL
) ;
ALTER TABLE q_user_item ADD PRIMARY KEY (account_id,item_code);
ALTER TABLE q_user_item ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_user_item ALTER COLUMN item_code SET NOT NULL;
ALTER TABLE q_user_item ALTER COLUMN acquired_date SET NOT NULL;

CREATE TABLE q_user_occupation (
	account_id bigint NOT NULL,
	occupation_id bigint NOT NULL
) ;
ALTER TABLE q_user_occupation ADD PRIMARY KEY (account_id,occupation_id);

CREATE TABLE q_user_roles (
	account_id bigint NOT NULL,
	role_id bigint NOT NULL
) ;
ALTER TABLE q_user_roles ADD PRIMARY KEY (account_id,role_id);

CREATE TABLE q_user_trophy (
	user_trophy_id bigint NOT NULL,
	account_id bigint NOT NULL,
	code varchar(80) NOT NULL,
	earned_date timestamp(6) NOT NULL,
	created_by bigint,
	updated_by bigint,
	created_date timestamp(0) NOT NULL,
	updated_date timestamp(0)
) ;
ALTER TABLE q_user_trophy ADD PRIMARY KEY (user_trophy_id);
ALTER TABLE q_user_trophy ADD UNIQUE (account_id,code);
ALTER TABLE q_user_trophy ALTER COLUMN user_trophy_id SET NOT NULL;
ALTER TABLE q_user_trophy ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_user_trophy ALTER COLUMN code SET NOT NULL;
ALTER TABLE q_user_trophy ALTER COLUMN earned_date SET NOT NULL;
ALTER TABLE q_user_trophy ALTER COLUMN created_date SET NOT NULL;

CREATE TABLE q_verification_token (
	token_id bigint NOT NULL,
	account_id bigint NOT NULL,
	token varchar(120),
	issued_date timestamp(6) NOT NULL,
	validity_period integer,
	activation_date timestamp(6)
) ;
ALTER TABLE q_verification_token ADD PRIMARY KEY (token_id);
ALTER TABLE q_verification_token ALTER COLUMN token_id SET NOT NULL;
ALTER TABLE q_verification_token ALTER COLUMN account_id SET NOT NULL;
ALTER TABLE q_verification_token ALTER COLUMN issued_date SET NOT NULL;
ALTER TABLE q_answer ADD CONSTRAINT answers_glossaries_fk FOREIGN KEY (term_id) REFERENCES q_glossary(term_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_answer ADD CONSTRAINT answers_questions_fk FOREIGN KEY (question_id) REFERENCES q_question(question_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_answer_translations ADD CONSTRAINT answer_t_languages_fk FOREIGN KEY (lang_id) REFERENCES q_language(lang_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_answer_translations ADD CONSTRAINT answer_t_questions_fk FOREIGN KEY (ans_id) REFERENCES q_answer(ans_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_category ADD CONSTRAINT subcategories_categories_fk FOREIGN KEY (subcategory_id) REFERENCES q_category(cat_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_category_translations ADD CONSTRAINT category_t_categories_fk FOREIGN KEY (cat_id) REFERENCES q_category(cat_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_category_translations ADD CONSTRAINT category_t_languages_fk FOREIGN KEY (lang_id) REFERENCES q_language(lang_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_challenge ADD CONSTRAINT q_challenge_challenger FOREIGN KEY (challenger_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_challenge ADD CONSTRAINT q_challenge_opponent FOREIGN KEY (opponent_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_challenge ADD CONSTRAINT q_challenge_quiz FOREIGN KEY (quiz_id) REFERENCES q_quiz(quiz_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_conquest_attempt ADD CONSTRAINT q_conquest_attempt_cat FOREIGN KEY (cat_id) REFERENCES q_category(cat_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_conquest_attempt ADD CONSTRAINT q_conquest_attempt_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_custom_answer ADD CONSTRAINT q_custom_answer_question_fk FOREIGN KEY (question_id) REFERENCES q_custom_question(question_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_daily_challenge ADD CONSTRAINT q_daily_challenge_quiz FOREIGN KEY (quiz_id) REFERENCES q_quiz(quiz_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_daily_task ADD CONSTRAINT q_daily_task_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_duel ADD CONSTRAINT q_duel_challenger FOREIGN KEY (challenger_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_duel ADD CONSTRAINT q_duel_opponent FOREIGN KEY (opponent_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_duel_round ADD CONSTRAINT q_duel_round_duel FOREIGN KEY (duel_id) REFERENCES q_duel(duel_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_glossary ADD CONSTRAINT glossary_category_fk FOREIGN KEY (cat_id) REFERENCES q_category(cat_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_glossary ADD CONSTRAINT glossary_parent_fk FOREIGN KEY (parent_id) REFERENCES q_glossary(term_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_glossary ADD CONSTRAINT glossary_type_fk FOREIGN KEY (type_id) REFERENCES q_glossary_type(id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_glossary_translations ADD CONSTRAINT glossary_t_glossaries_fk FOREIGN KEY (term_id) REFERENCES q_glossary(term_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_glossary_translations ADD CONSTRAINT glossary_t_languages_fk FOREIGN KEY (lang_id) REFERENCES q_language(lang_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_group_post ADD CONSTRAINT q_group_post_group FOREIGN KEY (group_id) REFERENCES q_social_group(group_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_group_post ADD CONSTRAINT q_group_post_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_group_post_comment ADD CONSTRAINT q_group_post_comment_post FOREIGN KEY (post_id) REFERENCES q_group_post(post_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_group_post_comment ADD CONSTRAINT q_group_post_comment_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_iq_response ADD CONSTRAINT q_iq_response_item FOREIGN KEY (item_id) REFERENCES q_iq_item(item_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_iq_response ADD CONSTRAINT q_iq_response_session FOREIGN KEY (session_id) REFERENCES q_iq_session(session_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_iq_session ADD CONSTRAINT q_iq_session_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_knowledge_base ADD CONSTRAINT knowledge_base_category_fk FOREIGN KEY (cat_id) REFERENCES q_category(cat_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_knowledge_base ADD CONSTRAINT knowledge_base_parent_fk FOREIGN KEY (parent_id) REFERENCES q_knowledge_base(id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_knowledge_base_translation ADD CONSTRAINT knowledge_base_t_languages_fk FOREIGN KEY (lang_id) REFERENCES q_language(lang_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_knowledge_base_translation ADD CONSTRAINT k_base_t_k_base_fk FOREIGN KEY (k_base_id) REFERENCES q_knowledge_base(id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_level_up ADD CONSTRAINT q_level_up_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_message ADD CONSTRAINT message_message_group_fk FOREIGN KEY (destination) REFERENCES q_message_group(group_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_mistake ADD CONSTRAINT q_mistake_question FOREIGN KEY (question_id) REFERENCES q_question(question_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_mistake ADD CONSTRAINT q_mistake_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_occupation ADD CONSTRAINT occupation_parent_fk FOREIGN KEY (parent_id) REFERENCES q_occupation(id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_question ADD CONSTRAINT questions_cat_id FOREIGN KEY (cat_id) REFERENCES q_category(cat_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_question ADD CONSTRAINT questions_user_id FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_question_translations ADD CONSTRAINT question_t_languages_fk FOREIGN KEY (lang_id) REFERENCES q_language(lang_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_question_translations ADD CONSTRAINT question_t_questions_fk FOREIGN KEY (question_id) REFERENCES q_question(question_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_quiz ADD CONSTRAINT quiz_category_fk FOREIGN KEY (cat_id) REFERENCES q_category(cat_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_quiz ADD CONSTRAINT quiz_type_fk FOREIGN KEY (type) REFERENCES q_quiz_type(id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_quiz_invite ADD CONSTRAINT q_quiz_invite_quiz_fk FOREIGN KEY (quiz_id) REFERENCES q_quiz(quiz_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_quiz_invite ADD CONSTRAINT q_quiz_invite_user_fk FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_reaction ADD CONSTRAINT q_reaction_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_season_claim ADD CONSTRAINT q_season_claim_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_social_group ADD CONSTRAINT q_social_group_owner FOREIGN KEY (owner_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_social_group_member ADD CONSTRAINT q_social_group_member_group FOREIGN KEY (group_id) REFERENCES q_social_group(group_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_social_group_member ADD CONSTRAINT q_social_group_member_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_user_background ADD CONSTRAINT q_user_background_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_user_friend ADD CONSTRAINT q_user_friend_friend_fk FOREIGN KEY (friend_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_user_friend ADD CONSTRAINT q_user_friend_user_fk FOREIGN KEY (user_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_user_group ADD CONSTRAINT user_group_message_group_fk FOREIGN KEY (group_id) REFERENCES q_message_group(group_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_user_group ADD CONSTRAINT user_group_user_fk FOREIGN KEY (participant) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_user_history ADD CONSTRAINT history_account_fk FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_user_history ADD CONSTRAINT history_quiz_fk FOREIGN KEY (quiz_id) REFERENCES q_quiz(quiz_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_user_item ADD CONSTRAINT q_user_item_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_user_occupation ADD CONSTRAINT sys_c009064 FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_user_occupation ADD CONSTRAINT sys_c009065 FOREIGN KEY (occupation_id) REFERENCES q_occupation(id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_user_roles ADD CONSTRAINT sys_c008941 FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE q_user_roles ADD CONSTRAINT sys_c008942 FOREIGN KEY (role_id) REFERENCES q_role(role_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_user_trophy ADD CONSTRAINT q_user_trophy_user FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
ALTER TABLE q_verification_token ADD CONSTRAINT q_user_q_vt_fk FOREIGN KEY (account_id) REFERENCES q_user(account_id) ON DELETE CASCADE NOT DEFERRABLE INITIALLY IMMEDIATE;
