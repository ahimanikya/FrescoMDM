CREATE TABLE CLUSTER_BUCKET
(
  ID          numeric(38),
  BUCKETNAME  VARCHAR(100)                NOT NULL,
  LOADERNAME  VARCHAR(100),
  TYPE        numeric(38),
  STATE       numeric(38),
  VERSIONNO   numeric(38),
  CONSTRAINT PK_CLUSTER_BUCKET PRIMARY KEY (ID),
  CONSTRAINT CLUSTER_BUCKET_UK UNIQUE (BUCKETNAME, LOADERNAME));
)
GO



CREATE UNIQUE INDEX CLUSTER_BUCKET_LOADER ON  CLUSTER_BUCKET (BUCKETNAME, LOADERNAME))
GO



DROP SEQUENCE BUCKET_ID_SEQ
GO

CREATE SEQUENCE BUCKET_ID_SEQ
  START  1
  MAXVALUE 999999999999999999999
  MINVALUE 1
GO



CREATE TABLE LOADER
(
  NAME         VARCHAR(100)               NOT NULL,
  MACHINENAME  VARCHAR(100),
  RMIPORT      numeric(38),
  ISMASTER     numeric(38),
  STATE        numeric(38),
  WORKINGDIR   VARCHAR(200)               NOT NULL,
  CONSTRAINT PK_LOADER PRIMARY KEY (NAME)
)
GO



CREATE TABLE ANALYSIS_STATE
(
  STATE       numeric(38),
  ISMASTER    numeric(38),
  LOADERNAME  VARCHAR2(100),
  DONE        numeric(38),
  CONSTRAINT PK_ANALYSIS_STATE PRIMARY KEY (LOADERNAME)
)
GO
