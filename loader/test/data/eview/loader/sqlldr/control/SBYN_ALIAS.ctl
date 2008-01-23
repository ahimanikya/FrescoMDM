load data 
	 infile '../masterindex/SBYN_ALIAS.data' "str '$$$'"
	 APPEND into table SBYN_ALIAS
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( Personid, Aliasid, FirstName, MiddleName, LastName ) 
