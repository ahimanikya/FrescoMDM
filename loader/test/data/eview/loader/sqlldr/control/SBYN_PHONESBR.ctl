load data 
	 infile '../masterindex/SBYN_PHONESBR.data' "str '$$$'"
	 APPEND into table SBYN_PHONESBR
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( Personid, Phoneid, PhoneType, Phone, PhoneExt ) 
