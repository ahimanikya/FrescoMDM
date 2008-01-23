load data 
	 infile '../masterindex/SBYN_TRANSACTION.data' "str '$$$'"
	 APPEND into table SBYN_TRANSACTION
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( TRANSACTIONNUMBER, LID1, LID2, EUID1, EUID2, FUNCTION, SYSTEMUSER, TIMESTAMP, SYSTEMCODE, LID, EUID ) 
