load data 
	 infile '../masterindex/SBYN_PERSON.data' "str '$$$'"
	 APPEND into table SBYN_PERSON
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( systemcode, lid, Personid, PersonCatCode, FirstName, FirstName_Std, FirstName_Phon, MiddleName, LastName, LastName_Std, LastName_Phon, Suffix, Title, SSN, DOB date "MM/dd/yyyy HH24:MI:SS", Death, Gender, MStatus, Race, Ethnic, Religion, Language, SpouseName, MotherName, MotherMN, FatherName, Maiden, PobCity, PobState, PobCountry, VIPFlag, VetStatus, Status, DriverLicense, DriverLicenseSt, Dod date "MM/dd/yyyy HH24:MI:SS", DeathCertificate, Nationality, Citizenship ) 
