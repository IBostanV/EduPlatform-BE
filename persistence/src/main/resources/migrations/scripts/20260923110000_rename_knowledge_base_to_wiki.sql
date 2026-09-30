-- // rename_knowledge_base_to_wiki
-- The section is called the Wiki now. Only what people read changes: the key stays
-- 'knowledge_base', as do the route, the tables and the code, so nothing that points at it breaks.
UPDATE Q_TRANSLATION
   SET DEFAULT_VALUE = 'Wiki'
     , EN = 'Wiki'
     , RU = 'Вики'
     , RO = 'Wiki'
     , DE = 'Wiki'
     , UPDATED_DATE = SYSDATE
 WHERE "KEY" = 'knowledge_base'
/execute/

-- //@UNDO
UPDATE Q_TRANSLATION
   SET DEFAULT_VALUE = 'Knowledge base'
     , EN = 'Knowledge base'
     , RU = 'База знаний'
     , RO = 'Bază de cunoștințe'
     , DE = 'Wissensbasis'
     , UPDATED_DATE = SYSDATE
 WHERE "KEY" = 'knowledge_base'
/execute/
