-- // alter_user_add_coins
-- The site's own currency: earned alongside experience, spent on streak freezes, hints and extra
-- time. A balance rather than a ledger: every change is one conditional UPDATE, which is all that
-- keeps a player from spending the same coins twice.
ALTER TABLE Q_USER ADD COINS NUMERIC(10,0) DEFAULT 0 NOT NULL
/execute/

-- Streak freezes bought and not yet used: each covers one missed day of the visit streak.
ALTER TABLE Q_USER ADD STREAK_FREEZES NUMERIC(3,0) DEFAULT 0 NOT NULL
/execute/

-- //@UNDO
ALTER TABLE Q_USER DROP COLUMN STREAK_FREEZES
/execute/

ALTER TABLE Q_USER DROP COLUMN COINS
/execute/
