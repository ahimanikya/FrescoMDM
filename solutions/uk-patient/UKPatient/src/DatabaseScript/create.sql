CREATE TABLE SBYN_SYSTEMS
(
    SYSTEMCODE		VARCHAR2(20)	NOT NULL,
    DESCRIPTION		VARCHAR2(50)	NOT NULL,
    STATUS		CHAR		NOT NULL,
    ID_LENGTH		INTEGER		NOT NULL,
    FORMAT		VARCHAR2(60)	NULL    ,
    INPUT_MASK		VARCHAR2(60)	NULL    ,
    VALUE_MASK		VARCHAR2(60)	NULL    ,
    CREATE_DATE		DATE		NOT NULL,
    CREATE_USERID	VARCHAR2(20)	NOT NULL,
    UPDATE_DATE		DATE		NULL    ,
    UPDATE_USERID	VARCHAR2(20)	NULL    ,
    CONSTRAINT PK_SBYN_SYSTEM PRIMARY KEY (SYSTEMCODE)
        USING INDEX
);

CREATE TABLE SBYN_USER_CODE
(
    CODE_LIST		VARCHAR2(20)	NOT NULL,
    CODE			VARCHAR2(20)	NOT NULL,
    DESCR			VARCHAR2(50)	NOT NULL,
    FORMAT			VARCHAR2(60)	NULL    ,
    INPUT_MASK		VARCHAR2(60)	NULL    ,
    VALUE_MASK		VARCHAR2(60)	NULL    ,
    CONSTRAINT PK_SBYN_USER_CODE PRIMARY KEY (CODE_LIST, CODE)
        USING INDEX
);

CREATE TABLE SBYN_SYSTEMOBJECT (
	SYSTEMCODE VARCHAR2(20),
	LID VARCHAR2(25),
	CHILDTYPE VARCHAR2(20),
	CREATEUSER VARCHAR2(30),
	CREATEFUNCTION VARCHAR2(20),
	CREATEDATE DATE,
	UPDATEUSER VARCHAR2(30),
	UPDATEFUNCTION VARCHAR2(20),
	UPDATEDATE DATE,
	STATUS VARCHAR2(15));

ALTER TABLE SBYN_SYSTEMOBJECT ADD CONSTRAINT PK_SBYNSYSTEMOBJECT PRIMARY KEY (SYSTEMCODE, LID);
ALTER TABLE SBYN_SYSTEMOBJECT ADD CONSTRAINT FK_SYSTEMOBJECT_SYSTEMCODE FOREIGN KEY (SYSTEMCODE) REFERENCES SBYN_SYSTEMS(SYSTEMCODE);

CREATE TABLE SBYN_SYSTEMSBR (
	EUID VARCHAR2(20),
	CHILDTYPE VARCHAR2(20),
	CREATESYSTEM VARCHAR2(20),
	CREATEUSER VARCHAR2(30),
	CREATEFUNCTION VARCHAR2(20),
	CREATEDATE DATE,
	UPDATESYSTEM VARCHAR2(20),
	UPDATEUSER VARCHAR2(30),
	UPDATEFUNCTION VARCHAR2(20),
	UPDATEDATE DATE,
	STATUS VARCHAR2(15),
      REVISIONNUMBER NUMBER(38));

ALTER TABLE SBYN_SYSTEMSBR ADD CONSTRAINT PK_SBYNSYSTEMSBR PRIMARY KEY (EUID);

CREATE TABLE SBYN_OVERWRITE (
	EUID VARCHAR2(20),
	PATH VARCHAR2(200),
	TYPE VARCHAR2(20),
	INTEGERDATA NUMBER(38),
	BOOLEANDATA NUMBER(38),
	STRINGDATA VARCHAR2(200),
	BYTEDATA CHAR(2),
	LONGDATA LONG,
	DATEDATA DATE,
	FLOATDATA NUMBER(38, 4),
	TIMESTAMPDATA DATE);

ALTER TABLE SBYN_OVERWRITE ADD CONSTRAINT FK_SYSTEMSBR_EUID FOREIGN KEY (EUID) REFERENCES SBYN_SYSTEMSBR(EUID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_OVERWRITE ADD CONSTRAINT PK_SBROVERWRITE PRIMARY KEY (EUID, PATH);

CREATE TABLE SBYN_ENTERPRISE (
	SYSTEMCODE VARCHAR2(20),
	LID VARCHAR2(25),
	EUID VARCHAR2(20));

ALTER TABLE SBYN_ENTERPRISE ADD CONSTRAINT FK_ENTERPRISE_SYSTEMCODE_LID FOREIGN KEY (SYSTEMCODE, LID) REFERENCES SBYN_SYSTEMOBJECT(SYSTEMCODE, LID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_ENTERPRISE ADD CONSTRAINT FK_ENTERPRISE_EUID FOREIGN KEY (EUID) REFERENCES SBYN_SYSTEMSBR(EUID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_ENTERPRISE ADD CONSTRAINT PK_ENTERPRISE PRIMARY KEY (SYSTEMCODE, LID, EUID);

CREATE TABLE SBYN_TRANSACTION (
	TRANSACTIONNUMBER VARCHAR2(20),
	LID1 VARCHAR2(25),
	LID2 VARCHAR2(25),
	EUID1 VARCHAR2(20),
	EUID2 VARCHAR2(20),
	FUNCTION VARCHAR2(20),
	SYSTEMUSER VARCHAR2(30),
	TIMESTAMP TIMESTAMP,
	DELTA BLOB,
	SYSTEMCODE VARCHAR2(20),
	LID VARCHAR2(25),
	EUID VARCHAR2(20));

ALTER TABLE SBYN_TRANSACTION ADD CONSTRAINT PK_TRANSACTION PRIMARY KEY (TRANSACTIONNUMBER);
ALTER TABLE SBYN_TRANSACTION ADD CONSTRAINT AK_TRANSACTION UNIQUE (EUID, EUID2, TRANSACTIONNUMBER);

CREATE TABLE SBYN_ASSUMEDMATCH (
    ASSUMEDMATCHID VARCHAR2(20),
    EUID VARCHAR2(20),
    SYSTEMCODE VARCHAR2(20),
	LID VARCHAR2(25),
    WEIGHT VARCHAR2(20),
    TRANSACTIONNUMBER VARCHAR(20));
    

ALTER TABLE SBYN_ASSUMEDMATCH ADD CONSTRAINT FK_AM_TRANSACTIONNUMBER FOREIGN KEY (TRANSACTIONNUMBER) REFERENCES SBYN_TRANSACTION(TRANSACTIONNUMBER);

CREATE TABLE SBYN_POTENTIALDUPLICATES (
	POTENTIALDUPLICATEID VARCHAR2(20),
	WEIGHT VARCHAR2(20),
	TYPE VARCHAR2(15),
	DESCRIPTION VARCHAR2(120),
	STATUS VARCHAR2(15),
	HIGHMATCHFLAG VARCHAR2(15),
	RESOLVEDUSER VARCHAR2(30),
	RESOLVEDDATE DATE,
	RESOLVEDCOMMENT VARCHAR2(120),
	EUID2 VARCHAR2(20),
	TRANSACTIONNUMBER VARCHAR2(20),
	EUID1 VARCHAR2(20));

ALTER TABLE SBYN_POTENTIALDUPLICATES ADD CONSTRAINT PK_POTENTIALDUPLICATES PRIMARY KEY (POTENTIALDUPLICATEID);

CREATE TABLE SBYN_AUDIT (
	AUDIT_ID VARCHAR2(20) NOT NULL,
	PRIMARY_OBJECT_TYPE VARCHAR2(20) ,
	EUID VARCHAR2(20) ,
	EUID_AUX VARCHAR2(20),
	FUNCTION VARCHAR2(32) NOT NULL,
	DETAIL VARCHAR2(120),
	CREATE_DATE DATE NOT NULL,
	CREATE_BY VARCHAR2(20) NOT NULL);

ALTER TABLE SBYN_AUDIT ADD CONSTRAINT PK_SBYN_AUDIT PRIMARY KEY (AUDIT_ID);

CREATE TABLE SBYN_MERGE (
	MERGE_ID VARCHAR2(20) NOT NULL,
    KEPT_EUID VARCHAR2(20) NOT NULL,
    MERGED_EUID VARCHAR2(20),
    MERGE_TRANSACTIONNUM VARCHAR2(20) NOT NULL,
    UNMERGE_TRANSACTIONNUM VARCHAR2(20));

ALTER TABLE SBYN_MERGE ADD CONSTRAINT FK_SBYN_MERGE FOREIGN KEY (KEPT_EUID, MERGED_EUID, MERGE_TRANSACTIONNUM) REFERENCES SBYN_TRANSACTION(EUID, EUID2, TRANSACTIONNUMBER) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_MERGE ADD CONSTRAINT PK_SBYN_MERGE PRIMARY KEY (MERGE_ID);

CREATE TABLE SBYN_APPL
(
    APPL_ID                  NUMBER(10)             NOT NULL,
    CODE                     VARCHAR2(8)            NOT NULL,
    DESCR                    VARCHAR2(30)           NOT NULL,
    READ_ONLY                CHAR(1)                DEFAULT 'N' NOT NULL
        CONSTRAINT CHK_SBYN_APPL CHECK (READ_ONLY IN ('Y','N')),
    CREATE_DATE              DATE                   NOT NULL,
    CREATE_USERID            VARCHAR2(20)           NOT NULL,
    CONSTRAINT PK_UP_SBYN_APPL PRIMARY KEY (APPL_ID)
)
;
CREATE UNIQUE INDEX SBYN_APPL_IDX1 ON SBYN_APPL (CODE ASC);

CREATE TABLE SBYN_COMMON_HEADER
(
    COMMON_HEADER_ID         NUMBER(10)             NOT NULL,
    APPL_ID                  NUMBER(10)             NOT NULL,
    CODE                     VARCHAR2(8)            NOT NULL,
    DESCR                    VARCHAR2(50)           NOT NULL,
    READ_ONLY                CHAR(1)                DEFAULT 'N' NOT NULL,
    MAX_INPUT_LEN            NUMBER(10)             DEFAULT 1 NOT NULL,
    TYP_TABLE_CODE           VARCHAR2(3)            NOT NULL,
    CREATE_DATE              DATE                   NOT NULL,
    CREATE_USERID            VARCHAR2(20)           NOT NULL,
    CONSTRAINT PK_SBYN_COMMON_HEADER PRIMARY KEY (COMMON_HEADER_ID)
)
;
CREATE INDEX FK_SBYN_COMMON_HEADER ON SBYN_COMMON_HEADER (APPL_ID ASC);
CREATE UNIQUE INDEX SBYN_COMMOM_HEADER_IDX1 ON SBYN_COMMON_HEADER (APPL_ID ASC, CODE ASC);

CREATE TABLE SBYN_COMMON_DETAIL
(
    COMMON_DETAIL_ID         NUMBER(10)             NOT NULL
        CONSTRAINT CHK_COMMON_DETAIL_ID CHECK (COMMON_DETAIL_ID >= 0),
    COMMON_HEADER_ID         NUMBER(10)             NOT NULL,
    CODE                     VARCHAR2(20)           NOT NULL,
    DESCR                    VARCHAR2(50)           NOT NULL,
    READ_ONLY                CHAR(1)                DEFAULT 'N' NOT NULL
        CONSTRAINT CHK_SBYN_COMMON_DETAIL CHECK (READ_ONLY IN ('Y','N')),
    CREATE_DATE              DATE                   NOT NULL,
    CREATE_USERID            VARCHAR2(20)           NOT NULL,
    CONSTRAINT PK_SBYN_COMMON_DETAIL PRIMARY KEY (COMMON_DETAIL_ID)
)
;
CREATE UNIQUE INDEX SBYN_COMMOM_DETAIL_IDX1 ON SBYN_COMMON_DETAIL (COMMON_HEADER_ID ASC, CODE ASC);

ALTER TABLE SBYN_COMMON_DETAIL
    ADD CONSTRAINT FK_COMM_DET_COMM_HEAD FOREIGN KEY  (COMMON_HEADER_ID)
       REFERENCES SBYN_COMMON_HEADER (COMMON_HEADER_ID)
;

CREATE INDEX SBYN_ENTERPRISE1 on SBYN_ENTERPRISE (EUID ASC);
CREATE INDEX SBYN_TRANSACTION1 on SBYN_TRANSACTION (TIMESTAMP ASC);
CREATE INDEX SBYN_TRANSACTION2 on SBYN_TRANSACTION (FUNCTION ASC);
CREATE INDEX SBYN_TRANSACTION4 on SBYN_TRANSACTION (EUID2 ASC, TIMESTAMP ASC);
CREATE INDEX SBYN_TRANSACTION3 on SBYN_TRANSACTION (TIMESTAMP ASC, TRANSACTIONNUMBER ASC);
ALTER TABLE SBYN_ASSUMEDMATCH ADD CONSTRAINT PK_ASSUMEDMATCH PRIMARY KEY (ASSUMEDMATCHID);
CREATE INDEX SBYN_ASSUMEDMATCH1 on SBYN_ASSUMEDMATCH (EUID ASC);
CREATE INDEX SBYN_ASSUMEDMATCH2 ON SBYN_ASSUMEDMATCH (TRANSACTIONNUMBER ASC);
CREATE INDEX SBYN_POTENTIALDUPLICATES1 on SBYN_POTENTIALDUPLICATES (EUID1 ASC);
CREATE INDEX SBYN_POTENTIALDUPLICATES2 on SBYN_POTENTIALDUPLICATES (EUID2 ASC);
CREATE INDEX SBYN_POTENTIALDUPLICATES3 ON SBYN_POTENTIALDUPLICATES (TRANSACTIONNUMBER ASC);
CREATE INDEX SBYN_AUDIT1 on SBYN_AUDIT (EUID ASC);
CREATE INDEX SBYN_AUDIT2 on SBYN_AUDIT (CREATE_DATE ASC);
CREATE INDEX SBYN_MERGE1 on SBYN_MERGE (KEPT_EUID ASC);

CREATE TABLE SBYN_UKPATIENT (
	SYSTEMCODE VARCHAR2(20),
	LID VARCHAR2(25),
	UKPATIENTID VARCHAR2(20),
	GENDER VARCHAR2(32),
	DOB DATE,
	BIRTHORDER INTEGER,
	BIRTHTIME VARCHAR2(12),
	MARITALSTATUS VARCHAR2(32),
	DATEOFDEATH DATE,
	STATUSOFDEATHNOTI VARCHAR2(32),
	LANGUAGE VARCHAR2(32),
	ETHNICCATCODE CHAR,
	COMMONCONTACTMETH VARCHAR2(32),
	CONTACTPREF01STIME VARCHAR2(32),
	CONTACTPREF01ETIME VARCHAR2(32),
	CONTACTPREF02STIME VARCHAR2(32),
	CONTACTPREF02ETIME VARCHAR2(32),
	CONTACTPREF03STIME VARCHAR2(32),
	CONTACTPREF03ETIME VARCHAR2(32),
	COMMENTTXT VARCHAR2(256),
	NI VARCHAR2(32))
		;

ALTER TABLE SBYN_UKPATIENT ADD CONSTRAINT FK_UKPATIENT_SYSTEMCODE_LID FOREIGN KEY (SYSTEMCODE, LID) REFERENCES SBYN_SYSTEMOBJECT(SYSTEMCODE, LID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_UKPATIENT ADD CONSTRAINT PK_UKPATIENT PRIMARY KEY (UKPATIENTID);
ALTER TABLE SBYN_UKPATIENT ADD CONSTRAINT U_UKPATIENT UNIQUE (SYSTEMCODE, LID) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_UKPATIENTSBR (
	EUID VARCHAR2(20),
	UKPATIENTID VARCHAR2(20),
	GENDER VARCHAR2(32),
	DOB DATE,
	BIRTHORDER INTEGER,
	BIRTHTIME VARCHAR2(12),
	MARITALSTATUS VARCHAR2(32),
	DATEOFDEATH DATE,
	STATUSOFDEATHNOTI VARCHAR2(32),
	LANGUAGE VARCHAR2(32),
	ETHNICCATCODE CHAR,
	COMMONCONTACTMETH VARCHAR2(32),
	CONTACTPREF01STIME VARCHAR2(32),
	CONTACTPREF01ETIME VARCHAR2(32),
	CONTACTPREF02STIME VARCHAR2(32),
	CONTACTPREF02ETIME VARCHAR2(32),
	CONTACTPREF03STIME VARCHAR2(32),
	CONTACTPREF03ETIME VARCHAR2(32),
	COMMENTTXT VARCHAR2(256),
	NI VARCHAR2(32))
		;
	
ALTER TABLE SBYN_UKPATIENTSBR ADD CONSTRAINT FK_UKPATIENTSBR_EUID FOREIGN KEY (EUID) REFERENCES SBYN_SYSTEMSBR(EUID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_UKPATIENTSBR ADD CONSTRAINT PK_UKPATIENTSBR PRIMARY KEY (UKPATIENTID);
ALTER TABLE SBYN_UKPATIENTSBR ADD CONSTRAINT U_UKPATIENTSBR UNIQUE (EUID) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_NAME (
	UKPATIENTID VARCHAR2(20),
	NAMEID VARCHAR2(20),
	FAMILYNAME VARCHAR2(32),
	FAMILYNAME_STD VARCHAR2(40),
	FAMILYNAME_PHON VARCHAR2(8),
	GIVENNAME VARCHAR2(32),
	GIVENNAME_STD VARCHAR2(40),
	GIVENNAME_PHON VARCHAR2(8),
	OTHERGIVENNAME VARCHAR2(32),
	TITLE VARCHAR2(32),
	SUFFIX VARCHAR2(32))
		;

ALTER TABLE SBYN_NAME ADD CONSTRAINT FK_NAME_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENT(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_NAME ADD CONSTRAINT PK_NAME PRIMARY KEY (NAMEID);
ALTER TABLE SBYN_NAME ADD CONSTRAINT U_NAME UNIQUE (UKPATIENTID, FAMILYNAME, GIVENNAME, OTHERGIVENNAME, SUFFIX) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_NAMESBR (
	UKPATIENTID VARCHAR2(20),
	NAMEID VARCHAR2(20),
	FAMILYNAME VARCHAR2(32),
	FAMILYNAME_STD VARCHAR2(40),
	FAMILYNAME_PHON VARCHAR2(8),
	GIVENNAME VARCHAR2(32),
	GIVENNAME_STD VARCHAR2(40),
	GIVENNAME_PHON VARCHAR2(8),
	OTHERGIVENNAME VARCHAR2(32),
	TITLE VARCHAR2(32),
	SUFFIX VARCHAR2(32))
		;

ALTER TABLE SBYN_NAMESBR ADD CONSTRAINT FK_NAMESBR_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENTSBR(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_NAMESBR ADD CONSTRAINT PK_NAMESBR PRIMARY KEY (NAMEID);
ALTER TABLE SBYN_NAMESBR ADD CONSTRAINT U_NAMESBR UNIQUE (UKPATIENTID, FAMILYNAME, GIVENNAME, OTHERGIVENNAME, SUFFIX) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_ADDRESS (
	UKPATIENTID VARCHAR2(20),
	ADDRESSID VARCHAR2(20),
	ADDRESSTYPE VARCHAR2(32),
	ADDRESSLINE1 VARCHAR2(32),
	ADDRESSLINE2 VARCHAR2(32),
	ADDRESSLINE3 VARCHAR2(32),
	ADDRESSLINE4 VARCHAR2(32),
	ADDRESSLINE5 VARCHAR2(32),
	ADDRESSHOUSENO VARCHAR2(32),
	PROPNAME VARCHAR2(32),
	PROPNAME_HOUSENO VARCHAR2(10),
	PROPNAME_STDIR VARCHAR2(5),
	PROPNAME_STNAME VARCHAR2(40),
	PROPNAME_STPHON VARCHAR2(8),
	PROPNAME_STTYPE VARCHAR2(5),
	STREET VARCHAR2(32),
	STREET_HOUSENO VARCHAR2(10),
	STREET_STDIR VARCHAR2(5),
	STREET_STNAME VARCHAR2(40),
	STREET_STPHON VARCHAR2(8),
	STREET_STTYPE VARCHAR2(5),
	POSTCODE VARCHAR2(32),
	PAFKEY VARCHAR2(32),
	ADDRESSDESCRIPTION VARCHAR2(32),
	EFFECTIVEFROM DATE,
	EFFECTIVETO DATE)
		;

ALTER TABLE SBYN_ADDRESS ADD CONSTRAINT FK_ADDRESS_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENT(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_ADDRESS ADD CONSTRAINT PK_ADDRESS PRIMARY KEY (ADDRESSID);
ALTER TABLE SBYN_ADDRESS ADD CONSTRAINT U_ADDRESS UNIQUE (UKPATIENTID, ADDRESSTYPE) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_ADDRESSSBR (
	UKPATIENTID VARCHAR2(20),
	ADDRESSID VARCHAR2(20),
	ADDRESSTYPE VARCHAR2(32),
	ADDRESSLINE1 VARCHAR2(32),
	ADDRESSLINE2 VARCHAR2(32),
	ADDRESSLINE3 VARCHAR2(32),
	ADDRESSLINE4 VARCHAR2(32),
	ADDRESSLINE5 VARCHAR2(32),
	ADDRESSHOUSENO VARCHAR2(32),
	PROPNAME VARCHAR2(32),
	PROPNAME_HOUSENO VARCHAR2(10),
	PROPNAME_STDIR VARCHAR2(5),
	PROPNAME_STNAME VARCHAR2(40),
	PROPNAME_STPHON VARCHAR2(8),
	PROPNAME_STTYPE VARCHAR2(5),
	STREET VARCHAR2(32),
	STREET_HOUSENO VARCHAR2(10),
	STREET_STDIR VARCHAR2(5),
	STREET_STNAME VARCHAR2(40),
	STREET_STPHON VARCHAR2(8),
	STREET_STTYPE VARCHAR2(5),
	POSTCODE VARCHAR2(32),
	PAFKEY VARCHAR2(32),
	ADDRESSDESCRIPTION VARCHAR2(32),
	EFFECTIVEFROM DATE,
	EFFECTIVETO DATE)
		;

ALTER TABLE SBYN_ADDRESSSBR ADD CONSTRAINT FK_ADDRESSSBR_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENTSBR(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_ADDRESSSBR ADD CONSTRAINT PK_ADDRESSSBR PRIMARY KEY (ADDRESSID);
ALTER TABLE SBYN_ADDRESSSBR ADD CONSTRAINT U_ADDRESSSBR UNIQUE (UKPATIENTID, ADDRESSTYPE) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_TELECOM (
	UKPATIENTID VARCHAR2(20),
	TELECOMID VARCHAR2(20),
	TYPE VARCHAR2(32),
	ROOT VARCHAR2(32),
	EXTENSION VARCHAR2(32),
	CODE VARCHAR2(32),
	EFFECTIVEFROM DATE,
	EFFECTIVETO DATE)
		;

ALTER TABLE SBYN_TELECOM ADD CONSTRAINT FK_TELECOM_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENT(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_TELECOM ADD CONSTRAINT PK_TELECOM PRIMARY KEY (TELECOMID);
ALTER TABLE SBYN_TELECOM ADD CONSTRAINT U_TELECOM UNIQUE (UKPATIENTID, TYPE) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_TELECOMSBR (
	UKPATIENTID VARCHAR2(20),
	TELECOMID VARCHAR2(20),
	TYPE VARCHAR2(32),
	ROOT VARCHAR2(32),
	EXTENSION VARCHAR2(32),
	CODE VARCHAR2(32),
	EFFECTIVEFROM DATE,
	EFFECTIVETO DATE)
		;

ALTER TABLE SBYN_TELECOMSBR ADD CONSTRAINT FK_TELECOMSBR_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENTSBR(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_TELECOMSBR ADD CONSTRAINT PK_TELECOMSBR PRIMARY KEY (TELECOMID);
ALTER TABLE SBYN_TELECOMSBR ADD CONSTRAINT U_TELECOMSBR UNIQUE (UKPATIENTID, TYPE) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_CONSENT (
	UKPATIENTID VARCHAR2(20),
	CONSENTID VARCHAR2(20),
	TYPE VARCHAR2(32),
	STATUS VARCHAR2(32),
	DATEMODIFIED DATE,
	COMMENTTXT VARCHAR2(32))
		;

ALTER TABLE SBYN_CONSENT ADD CONSTRAINT FK_CONSENT_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENT(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_CONSENT ADD CONSTRAINT PK_CONSENT PRIMARY KEY (CONSENTID);
ALTER TABLE SBYN_CONSENT ADD CONSTRAINT U_CONSENT UNIQUE (UKPATIENTID, CONSENTID) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_CONSENTSBR (
	UKPATIENTID VARCHAR2(20),
	CONSENTID VARCHAR2(20),
	TYPE VARCHAR2(32),
	STATUS VARCHAR2(32),
	DATEMODIFIED DATE,
	COMMENTTXT VARCHAR2(32))
		;

ALTER TABLE SBYN_CONSENTSBR ADD CONSTRAINT FK_CONSENTSBR_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENTSBR(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_CONSENTSBR ADD CONSTRAINT PK_CONSENTSBR PRIMARY KEY (CONSENTID);
ALTER TABLE SBYN_CONSENTSBR ADD CONSTRAINT U_CONSENTSBR UNIQUE (UKPATIENTID, CONSENTID) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_ALTCONTACT (
	UKPATIENTID VARCHAR2(20),
	ALTCONTACTID VARCHAR2(20),
	NI VARCHAR2(32),
	RELATIONSHIP VARCHAR2(32),
	ROLE VARCHAR2(32),
	FAMILYNAME VARCHAR2(32),
	FAMILYNAME_STD VARCHAR2(40),
	FAMILYNAME_PHON VARCHAR2(8),
	GIVENNAME VARCHAR2(32),
	GIVENNAME_STD VARCHAR2(40),
	GIVENNAME_PHON VARCHAR2(8),
	OTHERGIVENNAME VARCHAR2(32),
	TITLE VARCHAR2(32),
	SUFFIX VARCHAR2(32),
	LANGUAGE VARCHAR2(32),
	PRECEDENCE INTEGER,
	EFFECTIVEFROM DATE,
	EFFECTIVETO DATE,
	COMMONCONTACTMETH VARCHAR2(32),
	CONTACTPREF01STIME VARCHAR2(32),
	CONTACTPREF01ETIME VARCHAR2(32),
	CONTACTPREF02STIME VARCHAR2(32),
	CONTACTPREF02ETIME VARCHAR2(32),
	CONTACTPREF03STIME VARCHAR2(32),
	CONTACTPREF03ETIME VARCHAR2(32),
	ADDRESSTYPE VARCHAR2(32),
	ADDRESSLINE1 VARCHAR2(32),
	ADDRESSLINE2 VARCHAR2(32),
	ADDRESSLINE3 VARCHAR2(32),
	ADDRESSLINE4 VARCHAR2(32),
	ADDRESSLINE5 VARCHAR2(32),
	POSTCODE VARCHAR2(32),
	PAFKEY VARCHAR2(32),
	ADDRESSEFFECTFROM DATE,
	ADDRESSEFFECTIVETO DATE)
		;

ALTER TABLE SBYN_ALTCONTACT ADD CONSTRAINT FK_ALTCONTACT_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENT(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_ALTCONTACT ADD CONSTRAINT PK_ALTCONTACT PRIMARY KEY (ALTCONTACTID);
ALTER TABLE SBYN_ALTCONTACT ADD CONSTRAINT U_ALTCONTACT UNIQUE (UKPATIENTID, FAMILYNAME, GIVENNAME, OTHERGIVENNAME, SUFFIX) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_ALTCONTACTSBR (
	UKPATIENTID VARCHAR2(20),
	ALTCONTACTID VARCHAR2(20),
	NI VARCHAR2(32),
	RELATIONSHIP VARCHAR2(32),
	ROLE VARCHAR2(32),
	FAMILYNAME VARCHAR2(32),
	FAMILYNAME_STD VARCHAR2(40),
	FAMILYNAME_PHON VARCHAR2(8),
	GIVENNAME VARCHAR2(32),
	GIVENNAME_STD VARCHAR2(40),
	GIVENNAME_PHON VARCHAR2(8),
	OTHERGIVENNAME VARCHAR2(32),
	TITLE VARCHAR2(32),
	SUFFIX VARCHAR2(32),
	LANGUAGE VARCHAR2(32),
	PRECEDENCE INTEGER,
	EFFECTIVEFROM DATE,
	EFFECTIVETO DATE,
	COMMONCONTACTMETH VARCHAR2(32),
	CONTACTPREF01STIME VARCHAR2(32),
	CONTACTPREF01ETIME VARCHAR2(32),
	CONTACTPREF02STIME VARCHAR2(32),
	CONTACTPREF02ETIME VARCHAR2(32),
	CONTACTPREF03STIME VARCHAR2(32),
	CONTACTPREF03ETIME VARCHAR2(32),
	ADDRESSTYPE VARCHAR2(32),
	ADDRESSLINE1 VARCHAR2(32),
	ADDRESSLINE2 VARCHAR2(32),
	ADDRESSLINE3 VARCHAR2(32),
	ADDRESSLINE4 VARCHAR2(32),
	ADDRESSLINE5 VARCHAR2(32),
	POSTCODE VARCHAR2(32),
	PAFKEY VARCHAR2(32),
	ADDRESSEFFECTFROM DATE,
	ADDRESSEFFECTIVETO DATE)
		;

ALTER TABLE SBYN_ALTCONTACTSBR ADD CONSTRAINT FK_ALTCONTACTSBR_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENTSBR(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_ALTCONTACTSBR ADD CONSTRAINT PK_ALTCONTACTSBR PRIMARY KEY (ALTCONTACTID);
ALTER TABLE SBYN_ALTCONTACTSBR ADD CONSTRAINT U_ALTCONTACTSBR UNIQUE (UKPATIENTID, FAMILYNAME, GIVENNAME, OTHERGIVENNAME, SUFFIX) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_ALTCONTELE (
	UKPATIENTID VARCHAR2(20),
	ALTCONTELEID VARCHAR2(20),
	TYPE VARCHAR2(32),
	ROOT VARCHAR2(32),
	EXTENSION VARCHAR2(32),
	CODE VARCHAR2(32),
	EFFECTIVEFROM DATE,
	EFFECTIVETO DATE)
		;

ALTER TABLE SBYN_ALTCONTELE ADD CONSTRAINT FK_ALTCONTELE_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENT(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_ALTCONTELE ADD CONSTRAINT PK_ALTCONTELE PRIMARY KEY (ALTCONTELEID);
ALTER TABLE SBYN_ALTCONTELE ADD CONSTRAINT U_ALTCONTELE UNIQUE (UKPATIENTID, TYPE) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_ALTCONTELESBR (
	UKPATIENTID VARCHAR2(20),
	ALTCONTELEID VARCHAR2(20),
	TYPE VARCHAR2(32),
	ROOT VARCHAR2(32),
	EXTENSION VARCHAR2(32),
	CODE VARCHAR2(32),
	EFFECTIVEFROM DATE,
	EFFECTIVETO DATE)
		;

ALTER TABLE SBYN_ALTCONTELESBR ADD CONSTRAINT FK_ALTCONTELESBR_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENTSBR(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_ALTCONTELESBR ADD CONSTRAINT PK_ALTCONTELESBR PRIMARY KEY (ALTCONTELEID);
ALTER TABLE SBYN_ALTCONTELESBR ADD CONSTRAINT U_ALTCONTELESBR UNIQUE (UKPATIENTID, TYPE) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_AUXID (
	UKPATIENTID VARCHAR2(20),
	AUXIDID VARCHAR2(20),
	AUXIDDEF VARCHAR2(10),
	AUXID VARCHAR2(40))
		;

ALTER TABLE SBYN_AUXID ADD CONSTRAINT FK_AUXID_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENT(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_AUXID ADD CONSTRAINT PK_AUXID PRIMARY KEY (AUXIDID);
ALTER TABLE SBYN_AUXID ADD CONSTRAINT U_AUXID UNIQUE (UKPATIENTID, AUXIDDEF, AUXID) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_AUXIDSBR (
	UKPATIENTID VARCHAR2(20),
	AUXIDID VARCHAR2(20),
	AUXIDDEF VARCHAR2(10),
	AUXID VARCHAR2(40))
		;

ALTER TABLE SBYN_AUXIDSBR ADD CONSTRAINT FK_AUXIDSBR_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENTSBR(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_AUXIDSBR ADD CONSTRAINT PK_AUXIDSBR PRIMARY KEY (AUXIDID);
ALTER TABLE SBYN_AUXIDSBR ADD CONSTRAINT U_AUXIDSBR UNIQUE (UKPATIENTID, AUXIDDEF, AUXID) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_HEALTHID (
	UKPATIENTID VARCHAR2(20),
	HEALTHIDID VARCHAR2(20),
	IDNUMTYPE VARCHAR2(32),
	IDNUM VARCHAR2(32))
		;

ALTER TABLE SBYN_HEALTHID ADD CONSTRAINT FK_HEALTHID_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENT(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_HEALTHID ADD CONSTRAINT PK_HEALTHID PRIMARY KEY (HEALTHIDID);
ALTER TABLE SBYN_HEALTHID ADD CONSTRAINT U_HEALTHID UNIQUE (UKPATIENTID, IDNUM) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_HEALTHIDSBR (
	UKPATIENTID VARCHAR2(20),
	HEALTHIDID VARCHAR2(20),
	IDNUMTYPE VARCHAR2(32),
	IDNUM VARCHAR2(32))
		;

ALTER TABLE SBYN_HEALTHIDSBR ADD CONSTRAINT FK_HEALTHIDSBR_UKPATIENTID FOREIGN KEY (UKPATIENTID) REFERENCES SBYN_UKPATIENTSBR(UKPATIENTID) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE SBYN_HEALTHIDSBR ADD CONSTRAINT PK_HEALTHIDSBR PRIMARY KEY (HEALTHIDID);
ALTER TABLE SBYN_HEALTHIDSBR ADD CONSTRAINT U_HEALTHIDSBR UNIQUE (UKPATIENTID, IDNUM) DEFERRABLE INITIALLY DEFERRED;

CREATE TABLE SBYN_SEQ_TABLE (
	SEQ_NAME VARCHAR2(20),
	SEQ_COUNT NUMBER(38));

ALTER TABLE SBYN_SEQ_TABLE ADD CONSTRAINT U_SEQ_NAME UNIQUE (SEQ_NAME);

CREATE OR REPLACE FUNCTION SEQMGR (Seq_Name_In IN VARCHAR2, Chunk_Size_In IN INTEGER) RETURN INTEGER
IS
PRAGMA AUTONOMOUS_TRANSACTION;

   Count_out INTEGER := 0;
BEGIN
   Count_out := 0;

   UPDATE SBYN_SEQ_TABLE SET seq_count = seq_count+Chunk_Size_In WHERE seq_name = Seq_Name_In;
   SELECT seq_count-Chunk_Size_In  INTO Count_out FROM SBYN_SEQ_TABLE WHERE seq_name = Seq_Name_In;
   COMMIT;

   RETURN Count_out;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END SEQMGR;
/

INSERT INTO SBYN_SEQ_TABLE VALUES ('EUID', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('POTENTIALDUPLICATE', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('TRANSACTIONNUMBER', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ASSUMEDMATCH', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('AUDIT', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('MERGE', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('SBYN_APPL', 2);
INSERT INTO SBYN_SEQ_TABLE VALUES ('SBYN_COMMON_HEADER', 1);
INSERT INTO SBYN_SEQ_TABLE VALUES ('SBYN_COMMON_DETAIL', 1);
INSERT INTO SBYN_SEQ_TABLE VALUES ('NAME', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('NAMESBR', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ADDRESS', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ADDRESSSBR', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('TELECOM', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('TELECOMSBR', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('CONSENT', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('CONSENTSBR', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ALTCONTACT', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ALTCONTACTSBR', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ALTCONTELE', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('ALTCONTELESBR', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('AUXID', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('AUXIDSBR', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('HEALTHID', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('HEALTHIDSBR', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('UKPATIENT', 0);
INSERT INTO SBYN_SEQ_TABLE VALUES ('UKPATIENTSBR', 0);

INSERT INTO SBYN_APPL (APPL_ID, CODE, DESCR, READ_ONLY, CREATE_DATE, CREATE_USERID) 
VALUES (1, 'EV', 'eView5.1', 'Y', sysdate, user);

COMMIT;
