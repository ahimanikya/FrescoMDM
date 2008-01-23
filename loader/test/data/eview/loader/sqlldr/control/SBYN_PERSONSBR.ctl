load data 
	 infile '../masterindex/SBYN_PERSONSBR.data' "str '$$$'"
	 APPEND into table SBYN_PERSONSBR
	 fields terminated by "|" optionally enclosed by '"' 
	 TRAILING NULLCOLS 
	 ( euid, Personid, PersonCatCode, FirstName, FirstName_Std, FirstName_Phon, MiddleName, LastName, LastName_Std, LastName_Phon, Suffix, Title, SSN, DOB date "MM/dd/yyyy HH24:MI:SS", Death, Gender, MStatus, Race, Ethnic, Religion, Language, SpouseName, MotherName, MotherMN, FatherName, Maiden, PobCity, PobState, PobCountry, VIPFlag, VetStatus, Status, DriverLicense, DriverLicenseSt, Dod date "MM/dd/yyyy HH24:MI:SS", DeathCertificate, Nationality, Citizenship ) 
