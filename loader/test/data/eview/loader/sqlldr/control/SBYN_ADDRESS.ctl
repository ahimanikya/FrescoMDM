load data 
	 infile '../masterindex/SBYN_ADDRESS.data' "str '$$$'"
	 APPEND into table SBYN_ADDRESS
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( Personid, Addressid, AddressType, AddressLine1, AddressLine1_HouseNo, AddressLine1_StDir, AddressLine1_StName, AddressLine1_StPhon, AddressLine1_StType, AddressLine2, City, StateCode, PostalCode, PostalCodeExt, County, CountryCode ) 
