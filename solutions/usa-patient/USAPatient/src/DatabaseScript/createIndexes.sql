CREATE INDEX SBYN_PERSONSBR1 ON SBYN_USAPATIENT (LASTNAME_PHON ASC, FIRSTNAME_PHON ASC);

CREATE INDEX SBYN_PERSONSBR2 ON SBYN_USAPATIENT (SSN ASC);

CREATE INDEX SBYN_PERSONSBR3 ON SBYN_USAPATIENT (FIRSTNAME_PHON ASC, DOB ASC, GENDER ASC);

COMMIT;  