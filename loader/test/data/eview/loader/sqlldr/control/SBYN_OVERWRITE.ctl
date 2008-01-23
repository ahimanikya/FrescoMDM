load data 
	 infile '../masterindex/SBYN_OVERWRITE.data' "str '$$$'"
	 APPEND into table SBYN_OVERWRITE
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( EUID, PATH, TYPE, INTEGERDATA, BOOLEANDATA, STRINGDATA, BYTEDATA, LONGDATA, DATEDATA, FLOATDATA, TIMESTAMPDATA ) 
