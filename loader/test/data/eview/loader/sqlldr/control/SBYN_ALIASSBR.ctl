load data 
	 infile '../masterindex/SBYN_ALIASSBR.data' "str '$$$'"
	 APPEND into table SBYN_ALIASSBR
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( Personid, Aliasid, FirstName, MiddleName, LastName ) 
