ALTER TABLE SBYN_PHONESBR DISABLE CONSTRAINT FK_PHONESBR_Personid;
ALTER TABLE SBYN_PHONESBR DISABLE CONSTRAINT PK_PHONESBR;
ALTER TABLE SBYN_PHONESBR DISABLE CONSTRAINT U_PHONESBR;
ALTER TABLE SBYN_PHONE DISABLE CONSTRAINT FK_PHONE_Personid;
ALTER TABLE SBYN_PHONE DISABLE CONSTRAINT PK_PHONE;
ALTER TABLE SBYN_PHONE DISABLE CONSTRAINT U_PHONE;
ALTER TABLE SBYN_ADDRESSSBR DISABLE CONSTRAINT FK_ADDRESSSBR_Personid;
ALTER TABLE SBYN_ADDRESSSBR DISABLE CONSTRAINT PK_ADDRESSSBR;
ALTER TABLE SBYN_ADDRESSSBR DISABLE CONSTRAINT U_ADDRESSSBR;
ALTER TABLE SBYN_ADDRESS DISABLE CONSTRAINT FK_ADDRESS_Personid;
ALTER TABLE SBYN_ADDRESS DISABLE CONSTRAINT PK_ADDRESS;
ALTER TABLE SBYN_ADDRESS DISABLE CONSTRAINT U_ADDRESS;
ALTER TABLE SBYN_ALIASSBR DISABLE CONSTRAINT FK_ALIASSBR_Personid;
ALTER TABLE SBYN_ALIASSBR DISABLE CONSTRAINT PK_ALIASSBR;
ALTER TABLE SBYN_ALIASSBR DISABLE CONSTRAINT U_ALIASSBR;
ALTER TABLE SBYN_ALIAS DISABLE CONSTRAINT FK_ALIAS_Personid;
ALTER TABLE SBYN_ALIAS DISABLE CONSTRAINT PK_ALIAS;
ALTER TABLE SBYN_ALIAS DISABLE CONSTRAINT U_ALIAS;
ALTER TABLE SBYN_PERSONSBR DISABLE CONSTRAINT FK_PERSONSBR_EUID;
ALTER TABLE SBYN_PERSONSBR DISABLE CONSTRAINT PK_PERSONSBR;
ALTER TABLE SBYN_PERSONSBR DISABLE CONSTRAINT U_PERSONSBR;
ALTER TABLE SBYN_PERSON DISABLE CONSTRAINT FK_PERSON_SYSTEMCODE_LID;
ALTER TABLE SBYN_PERSON DISABLE CONSTRAINT PK_PERSON;
ALTER TABLE SBYN_PERSON DISABLE CONSTRAINT U_PERSON;


ALTER TABLE SBYN_OVERWRITE DISABLE CONSTRAINT FK_SYSTEMSBR_EUID ;
ALTER TABLE SBYN_OVERWRITE DISABLE CONSTRAINT PK_SBROVERWRITE ;
ALTER TABLE SBYN_ENTERPRISE DISABLE CONSTRAINT FK_ENTERPRISE_SYSTEMCODE_LID ;
ALTER TABLE SBYN_ENTERPRISE DISABLE CONSTRAINT FK_ENTERPRISE_EUID ;
ALTER TABLE SBYN_ENTERPRISE DISABLE CONSTRAINT PK_ENTERPRISE ;
ALTER TABLE SBYN_SYSTEMOBJECT DISABLE CONSTRAINT PK_SBYNSYSTEMOBJECT ;
ALTER TABLE SBYN_SYSTEMOBJECT DISABLE CONSTRAINT FK_SYSTEMOBJECT_SYSTEMCODE ;
ALTER TABLE SBYN_SYSTEMSBR DISABLE CONSTRAINT PK_SBYNSYSTEMSBR ;
ALTER TABLE SBYN_ASSUMEDMATCH DISABLE CONSTRAINT FK_AM_TRANSACTIONNUMBER ;
ALTER TABLE SBYN_POTENTIALDUPLICATES DISABLE CONSTRAINT PK_POTENTIALDUPLICATES ;
ALTER TABLE SBYN_ASSUMEDMATCH DISABLE CONSTRAINT PK_ASSUMEDMATCH;
ALTER TABLE SBYN_MERGE DISABLE CONSTRAINT FK_SBYN_MERGE;
ALTER TABLE SBYN_MERGE DISABLE CONSTRAINT PK_SBYN_MERGE;
ALTER TABLE SBYN_TRANSACTION DISABLE CONSTRAINT PK_TRANSACTION ;
ALTER TABLE SBYN_TRANSACTION DISABLE CONSTRAINT AK_TRANSACTION ;


DROP INDEX SBYN_ENTERPRISE1 ;
DROP INDEX SBYN_TRANSACTION1 ;
DROP INDEX SBYN_TRANSACTION2 ;
DROP INDEX SBYN_TRANSACTION4 ;
DROP INDEX SBYN_TRANSACTION3 ;
DROP INDEX SBYN_ASSUMEDMATCH1 ;
DROP INDEX SBYN_ASSUMEDMATCH2 ;
DROP INDEX SBYN_POTENTIALDUPLICATES1 ;
DROP INDEX SBYN_POTENTIALDUPLICATES2 ;
DROP INDEX SBYN_POTENTIALDUPLICATES3 ;
drop index U_PERSON;
drop index U_PERSONSBR;
drop index U_ALIAS;
drop index U_ALIASSBR;
drop index U_ADDRESS;
drop index U_ADDRESSSBR;
drop index U_PHONE;
drop index U_PHONESBR;
