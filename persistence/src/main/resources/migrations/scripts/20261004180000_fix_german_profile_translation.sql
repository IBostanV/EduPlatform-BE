-- // fix_german_profile_translation
-- Three of the first German translations: "Profile" was left in English, and "Take quiz" and
-- "Retype new password" spoke to the player as Sie where the rest of the site says du.
UPDATE Q_TRANSLATION SET DE = 'Profil', UPDATED_DATE = SYSDATE WHERE "KEY" = 'profile'
/execute/
UPDATE Q_TRANSLATION SET DE = 'Mach ein Quiz', UPDATED_DATE = SYSDATE WHERE "KEY" = 'take_quiz'
/execute/
UPDATE Q_TRANSLATION SET DE = 'Gib das neue Passwort erneut ein', UPDATED_DATE = SYSDATE WHERE "KEY" = 'retype_new_password'
/execute/

-- //@UNDO
UPDATE Q_TRANSLATION SET DE = 'Profile', UPDATED_DATE = SYSDATE WHERE "KEY" = 'profile'
/execute/
UPDATE Q_TRANSLATION SET DE = 'Machen Sie ein Quiz', UPDATED_DATE = SYSDATE WHERE "KEY" = 'take_quiz'
/execute/
UPDATE Q_TRANSLATION SET DE = 'Geben Sie das neue Passwort erneut ein', UPDATED_DATE = SYSDATE WHERE "KEY" = 'retype_new_password'
/execute/
