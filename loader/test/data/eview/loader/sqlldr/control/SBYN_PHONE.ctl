load data 
	 infile '../masterindex/SBYN_PHONE.data' "str '$$$'"
	 APPEND into table SBYN_PHONE
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( Personid, Phoneid, PhoneType, Phone, PhoneExt ) 
