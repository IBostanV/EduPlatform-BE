-- // create_client_error_table
-- Errors players' browsers hit and sent in (ClientErrorController), for the admin dashboard.
-- The player is CREATED_BY (empty for a guest); KIND is uncaught error, unhandled promise
-- rejection or network error. The stack runs past a VARCHAR, so it is a CLOB.
CREATE TABLE Q_CLIENT_ERROR (
    CLIENT_ERROR_ID NUMERIC(20, 0) NOT NULL,
    KIND VARCHAR(50) NOT NULL,
    MESSAGE VARCHAR(2000) NOT NULL,
    STACK CLOB,
    PAGE VARCHAR(500),
    USER_AGENT VARCHAR(500),
    CREATED_BY NUMERIC(20, 0),
    UPDATED_BY NUMERIC(20, 0),
    CREATED_DATE DATE NOT NULL,
    UPDATED_DATE DATE
)
/execute/

ALTER TABLE Q_CLIENT_ERROR
    ADD CONSTRAINT Q_CLIENT_ERROR_PK
        PRIMARY KEY (CLIENT_ERROR_ID)
/execute/

CREATE SEQUENCE client_error_seq
    START WITH     1
    INCREMENT BY   1
    NOCACHE
    NOCYCLE
/execute/

-- //@UNDO
DROP SEQUENCE client_error_seq
/execute/
DROP TABLE Q_CLIENT_ERROR
/execute/
