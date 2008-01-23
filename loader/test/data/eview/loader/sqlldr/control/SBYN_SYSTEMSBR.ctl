load data 
	 infile '../masterindex/SBYN_SYSTEMSBR.data' "str '$$$'"
	 APPEND into table SBYN_SYSTEMSBR
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( EUID, CHILDTYPE, CREATESYSTEM, CREATEUSER, CREATEFUNCTION, CREATEDATE date "dd-mm-yy HH24:MI:SS", UPDATESYSTEM, UPDATEUSER, UPDATEFUNCTION, UPDATEDATE date "dd-mm-yy HH24:MI:SS", STATUS, REVISIONNUMBER ) 
