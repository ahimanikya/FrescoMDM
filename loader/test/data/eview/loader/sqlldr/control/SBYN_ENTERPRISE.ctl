load data 
	 infile '../masterindex/SBYN_ENTERPRISE.data' "str '$$$'"
	 APPEND into table SBYN_ENTERPRISE
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( SYSTEMCODE, LID, EUID ) 
