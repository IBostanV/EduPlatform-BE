-- // create_reaction_table
-- A player's reaction to a line of news. The line is named by its feed key ("FRIEND_POST-12"),
-- the key every feed line already carries; the kind is a word the browser draws as an emoji.
CREATE TABLE Q_REACTION (
                        REACTION_ID NUMERIC(20,0) NOT NULL,
                        ACCOUNT_ID NUMERIC(20,0) NOT NULL,
                        ITEM_KEY VARCHAR2(200) NOT NULL,
                        KIND VARCHAR2(20) NOT NULL,
                        CREATED_BY NUMERIC(20, 0),
                        UPDATED_BY NUMERIC(20, 0),
                        CREATED_DATE DATE NOT NULL,
                        UPDATED_DATE DATE
)
/execute/

ALTER TABLE Q_REACTION
    ADD CONSTRAINT Q_REACTION_PK PRIMARY KEY (REACTION_ID)
/execute/

-- One of each kind per player per line.
ALTER TABLE Q_REACTION
    ADD CONSTRAINT Q_REACTION_UK UNIQUE (ACCOUNT_ID, ITEM_KEY, KIND)
/execute/

ALTER TABLE Q_REACTION
    ADD CONSTRAINT Q_REACTION_USER FOREIGN KEY (ACCOUNT_ID) REFERENCES Q_USER(ACCOUNT_ID)
/execute/

CREATE INDEX Q_REACTION_KEY_IX ON Q_REACTION (ITEM_KEY)
/execute/

-- //@UNDO
DROP TABLE Q_REACTION
/execute/
