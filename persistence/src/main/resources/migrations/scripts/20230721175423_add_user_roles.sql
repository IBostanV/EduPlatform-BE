-- // name_of_the_migration
INSERT INTO Q_ROLE VALUES(1, 'ROLE_ADMIN',null, null, trunc(SYSDATE), null)/execute/
INSERT INTO Q_ROLE VALUES(2, 'ROLE_USER',null, null, trunc(SYSDATE), null)/execute/
INSERT INTO Q_ROLE VALUES(3, 'ROLE_MODERATOR',null, null, trunc(SYSDATE), null)/execute/
INSERT INTO Q_ROLE VALUES(4, 'ROLE_CONTENT_EDITOR',null, null, trunc(SYSDATE), null)/execute/
INSERT INTO Q_ROLE VALUES(5, 'ROLE_CONTENT_PUBLISHER',null, null, trunc(SYSDATE), null)/execute/

-- //@UNDO
DELETE FROM Q_ROLE WHERE ROLE_ID = 1/execute/
DELETE FROM Q_ROLE WHERE ROLE_ID = 2/execute/


