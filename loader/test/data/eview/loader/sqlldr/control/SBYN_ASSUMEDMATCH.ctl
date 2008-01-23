load data 
	 infile '../masterindex/SBYN_ASSUMEDMATCH.data' "str '$$$'"
	 APPEND into table SBYN_ASSUMEDMATCH
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( ASSUMEDMATCHID, EUID, SYSTEMCODE, LID, WEIGHT, TRANSACTIONNUMBER ) 
