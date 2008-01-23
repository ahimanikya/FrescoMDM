load data 
	 infile '../masterindex/SBYN_SYSTEMOBJECT.data' "str '$$$'"
	 APPEND into table SBYN_SYSTEMOBJECT
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( SYSTEMCODE, LID, CHILDTYPE, CREATEUSER, CREATEFUNCTION, CREATEDATE date "dd-mm-yy HH24:MI:SS", UPDATEUSER, UPDATEFUNCTION, UPDATEDATE date "dd-mm-yy HH24:MI:SS", STATUS ) 
