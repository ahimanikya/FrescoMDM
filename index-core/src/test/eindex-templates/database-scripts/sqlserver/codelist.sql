

-- declare table variable to hold 
-- user specified code list

declare @codelist table(
    objtype varchar(1) not null, 
    code varchar(8) not null,
    descr varchar(50) not null
)

-- declare cursor to loop
-- through the code list

declare c1 cursor
for
select * from @codelist

-- temporary variables

declare @v_objtype varchar(1),
              @v_code varchar(8),
              @v_descr varchar(50),
              @v_appl_id int,
              @v_header_seq int,
              @v_detail_seq int,
              @v_date datetime

begin

        -- ****     ADDRESS TYPE   ****
        insert into @codelist values('L', 'ADDRTYPE', 'ADDRESS TYPE')
        insert into @codelist values('V', 'O', 'OFFICE')
        insert into @codelist values('V', 'H', 'HOME')
        insert into @codelist values('V', 'V', 'VACATION')
        insert into @codelist values('V', 'H2', 'HOME 2')
        insert into @codelist values('V', 'M', 'MAILING')
        insert into @codelist values('V', 'W', 'WORK')
        insert into @codelist values('V', 'W2', 'WORK 2')
        insert into @codelist values('V', 'T', 'TEMPORARY')
        
        -- ****     CITIZENSHIP   ****        
        insert into @codelist values('L', 'CITIZEN', 'CITIZENSHIP')
        insert into @codelist values('V', 'USA', 'UNITED STATES')
        insert into @codelist values('V', 'CAN', 'CANADA')
        insert into @codelist values('V', 'MEX', 'MEXICO')
        
        -- ****     COUNTRY CODE   ****        
        insert into @codelist values('L', 'COUNTRY', 'COUNTRY CODE')
        insert into @codelist values('V', 'AFGH', 'AFGHANISTAN')
        insert into @codelist values('V', 'ALBA', 'ALBANIA')
        insert into @codelist values('V', 'ALGE', 'ALGERIA')
        insert into @codelist values('V', 'AMSA', 'AMERICAN SAMOA')
        insert into @codelist values('V', 'ANDO', 'ANDORRA')
        insert into @codelist values('V', 'ANGO', 'ANGOLA')
        insert into @codelist values('V', 'ANGU', 'ANGUILLA')
        insert into @codelist values('V', 'ANTA', 'ANTARCTICA')
        insert into @codelist values('V', 'ANTI', 'ANTIGUA AND BARBUDA')
        insert into @codelist values('V', 'ARGN', 'ARGENTINA')
        insert into @codelist values('V', 'ARMN', 'ARMENIA')
        insert into @codelist values('V', 'ARUB', 'ARUBA')
        insert into @codelist values('V', 'AUST', 'AUSTRALIA')
        insert into @codelist values('V', 'AUSA', 'AUSTRIA')
        insert into @codelist values('V', 'AZER', 'AZERBAIJAN')
        insert into @codelist values('V', 'AZOR', 'AZORES')
        insert into @codelist values('V', 'BAHA', 'BAHAMAS')
        insert into @codelist values('V', 'BAHR', 'BAHRAIN')
        insert into @codelist values('V', 'BANG', 'BANGLADESH')
        insert into @codelist values('V', 'BARB', 'BARBADOS')
        insert into @codelist values('V', 'BELA', 'BELARUS')
        insert into @codelist values('V', 'BELG', 'BELGIUM')
        insert into @codelist values('V', 'BELI', 'BELIZE')
        insert into @codelist values('V', 'BENE', 'BENIN')
        insert into @codelist values('V', 'BERM', 'BERMUDA')
        insert into @codelist values('V', 'BEQU', 'BEQUIA')
        insert into @codelist values('V', 'BHUT', 'BHUTAN')
        insert into @codelist values('V', 'BOLI', 'BOLIVIA')
        insert into @codelist values('V', 'BOSN', 'BOSNIA')
        insert into @codelist values('V', 'BOTS', 'BOTSWANA')
        insert into @codelist values('V', 'BRAZ', 'BRAZIL')
        insert into @codelist values('V', 'BRIO', 'BRITISH INDIAN OCEAN')
        insert into @codelist values('V', 'BRWI', 'BRITISH WEST INDIES')
        insert into @codelist values('V', 'BRUN', 'BRUNEI DARUSSALAM')
        insert into @codelist values('V', 'BULG', 'BULGARIA')
        insert into @codelist values('V', 'BURK', 'BURKINA FASO')
        insert into @codelist values('V', 'BURM', 'BURMA')
        insert into @codelist values('V', 'BURN', 'BURUNDI')
        insert into @codelist values('V', 'CAMB', 'CAMBODIA')
        insert into @codelist values('V', 'CAME', 'CAMEROON')
        insert into @codelist values('V', 'CANA', 'CANADA')
        insert into @codelist values('V', 'CAPE', 'CAPE VERDE')
        insert into @codelist values('V', 'CAYM', 'CAYMAN ISLANDS')
        insert into @codelist values('V', 'CENT', 'CENTRAL AFRICAN REPUBLIC')
        insert into @codelist values('V', 'CEYL', 'CEYLON')
        insert into @codelist values('V', 'CHAD', 'CHAD')
        insert into @codelist values('V', 'CHIL', 'CHILE')
        insert into @codelist values('V', 'CHIN', 'CHINA')
        insert into @codelist values('V', 'CRIS', 'CHRISTMAS ISLAND')
        insert into @codelist values('V', 'CCIS', 'COCOS (KEELING) ISLAND')
        insert into @codelist values('V', 'COLU', 'COLOMBIA')
        insert into @codelist values('V', 'COMO', 'COMOROS')
        insert into @codelist values('V', 'CONG', 'CONGO')
        insert into @codelist values('V', 'COOK', 'COOK ISLANDS')
        insert into @codelist values('V', 'COST', 'COSTA RICA')
        insert into @codelist values('V', 'COEL', 'COTE ELVOIRE')
        insert into @codelist values('V', 'CROA', 'CROATIA')
        insert into @codelist values('V', 'CUBA', 'CUBA')
        insert into @codelist values('V', 'CYPR', 'CYPRUS')
        insert into @codelist values('V', 'CZER', 'CZECH REPUBLIC')
        insert into @codelist values('V', 'CZEC', 'CZECHOSLOVAKIA')
        insert into @codelist values('V', 'DENM', 'DENMARK')
        insert into @codelist values('V', 'DJIB', 'DJIBOUTI')
        insert into @codelist values('V', 'DOMI', 'DOMINICA')
        insert into @codelist values('V', 'DOMR', 'DOMINICAN REPUBLIC')
        insert into @codelist values('V', 'ESTM', 'EAST TIMOR')
        insert into @codelist values('V', 'ECUA', 'ECUADOR')
        insert into @codelist values('V', 'EGYP', 'EGYPT')
        insert into @codelist values('V', 'ELSA', 'EL SALVADOR')
        insert into @codelist values('V', 'ENGL', 'ENGLAND')
        insert into @codelist values('V', 'EQUA', 'EQUATORIAL GUINEA')
        insert into @codelist values('V', 'ENTR', 'ERITREA')
        insert into @codelist values('V', 'ESTO', 'ESTONIA')
        insert into @codelist values('V', 'ETHI', 'ETHIOPIA')
        insert into @codelist values('V', 'FKIS', 'FALKLAND ISLANDS')
        insert into @codelist values('V', 'FRIS', 'FAROE ISLANDS')
        insert into @codelist values('V', 'FIJI', 'FIJI')
        insert into @codelist values('V', 'FINL', 'FINLAND')
        insert into @codelist values('V', 'FRAN', 'FRANCE')
        insert into @codelist values('V', 'FANT', 'FRENCH ANTILLES')
        insert into @codelist values('V', 'FRGU', 'FRENCH GUIANA')
        insert into @codelist values('V', 'FRPO', 'FRENCH POLYNESIA')
        insert into @codelist values('V', 'GABO', 'GABON')
        insert into @codelist values('V', 'GAMB', 'GAMBIA')
        insert into @codelist values('V', 'GEOR', 'GEORGIA')
        insert into @codelist values('V', 'GERM', 'GERMANY')
        insert into @codelist values('V', 'GHAN', 'GHANA')
        insert into @codelist values('V', 'GIBR', 'GIBRALTOR')
        insert into @codelist values('V', 'GRAN', 'GRANADA')
        insert into @codelist values('V', 'GRBR', 'GREAT BRITAIN')
        insert into @codelist values('V', 'GREE', 'GREECE')
        insert into @codelist values('V', 'GRNL', 'GREENLAND')
        insert into @codelist values('V', 'GREN', 'GRENADA')
        insert into @codelist values('V', 'GUAD', 'GUADELOUPE')
        insert into @codelist values('V', 'GUAM', 'GUAM')
        insert into @codelist values('V', 'GUAT', 'GUATEMALA')
        insert into @codelist values('V', 'GUIN', 'GUINEA')
        insert into @codelist values('V', 'GUBI', 'GUINEA-BISSAU')
        insert into @codelist values('V', 'GUYA', 'GUYANA')
        insert into @codelist values('V', 'HAIT', 'HAITI')
        insert into @codelist values('V', 'HRMD', 'HEARD AND MCDONALD')
        insert into @codelist values('V', 'HOLL', 'HOLLAND')
        insert into @codelist values('V', 'HOND', 'HONDURAS')
        insert into @codelist values('V', 'HONG', 'HONG KONG')
        insert into @codelist values('V', 'HUNG', 'HUNGARY')
        insert into @codelist values('V', 'ICEL', 'ICELAND')
        insert into @codelist values('V', 'INDI', 'INDIA')
        insert into @codelist values('V', 'INDO', 'INDONESIA')
        insert into @codelist values('V', 'IRAN', 'IRAN')
        insert into @codelist values('V', 'IRAQ', 'IRAQ')
        insert into @codelist values('V', 'IREL', 'IRELAND')
        insert into @codelist values('V', 'ISRA', 'ISRAEL')
        insert into @codelist values('V', 'ITAL', 'ITALY')
        insert into @codelist values('V', 'IVOR', 'IVORY COAST')
        insert into @codelist values('V', 'JAMA', 'JAMAICA')
        insert into @codelist values('V', 'JAPA', 'JAPAN')
        insert into @codelist values('V', 'JORD', 'JORDAN')
        insert into @codelist values('V', 'KAZK', 'KAZAKHSTAN')
        insert into @codelist values('V', 'KENY', 'KENYA')
        insert into @codelist values('V', 'KIRI', 'KIRIBATI')
        insert into @codelist values('V', 'KORE', 'KOREA')
        insert into @codelist values('V', 'KORR', 'KOREA, REPUBLIC OF')
        insert into @codelist values('V', 'KUWA', 'KUWAIT')
        insert into @codelist values('V', 'KYRG', 'KYRGYZSTAN')
        insert into @codelist values('V', 'LAOP', 'LAO PEOPLES DEMOCR')
        insert into @codelist values('V', 'LAOS', 'LAOS')
        insert into @codelist values('V', 'LATV', 'LATVIA')
        insert into @codelist values('V', 'LEBA', 'LEBANON')
        insert into @codelist values('V', 'LESO', 'LESOTHO')
        insert into @codelist values('V', 'LIBE', 'LIBERIA')
        insert into @codelist values('V', 'LIBY', 'LIBYA')
        insert into @codelist values('V', 'LICH', 'LIECHTENSTEIN')
        insert into @codelist values('V', 'LITH', 'LITHUANIA')
        insert into @codelist values('V', 'LUXE', 'LUXEMBOURG')
        insert into @codelist values('V', 'MACU', 'MACAU')
        insert into @codelist values('V', 'MACE', 'MACEDONIA')
        insert into @codelist values('V', 'MADA', 'MADAGASCAR')
        insert into @codelist values('V', 'MALW', 'MALAWI')
        insert into @codelist values('V', 'MALA', 'MALAYSIA')
        insert into @codelist values('V', 'MALD', 'MALDIVES')
        insert into @codelist values('V', 'MALI', 'MALI')
        insert into @codelist values('V', 'MALT', 'MALTA')
        insert into @codelist values('V', 'MALV', 'MALAVIVA')
        insert into @codelist values('V', 'MARS', 'MARSHALL ISLANDS')
        insert into @codelist values('V', 'MART', 'MARTINIQUE')
        insert into @codelist values('V', 'MAUN', 'MAURITANIA')
        insert into @codelist values('V', 'MAUT', 'MAURITIUS')
        insert into @codelist values('V', 'MAYT', 'MAYOTTE')
        insert into @codelist values('V', 'MEXI', 'MEXICO')
        insert into @codelist values('V', 'MICR', 'MICRONISIA, FEDERATION')
        insert into @codelist values('V', 'MOLD', 'MOLDOVA, REPUBLIC OF')
        insert into @codelist values('V', 'MONA', 'MONACO')
        insert into @codelist values('V', 'MONG', 'MONGOLIA')
        insert into @codelist values('V', 'MONT', 'MONTSERRAT')
        insert into @codelist values('V', 'MORO', 'MOROCCO')
        insert into @codelist values('V', 'MOZA', 'MOZAMBIQUE')
        insert into @codelist values('V', 'MUST', 'MUSTIQUE')
        insert into @codelist values('V', 'MYAN', 'MYANMAR')
        insert into @codelist values('V', 'NAMI', 'NAMIBIA')
        insert into @codelist values('V', 'NAUR', 'NAURU')
        insert into @codelist values('V', 'NEPA', 'NEPAL')
        insert into @codelist values('V', 'NETH', 'NETHERLANDS')
        insert into @codelist values('V', 'NEAN', 'NETHERLANDS ANTILLES')
        insert into @codelist values('V', 'NCAL', 'NEW CALEDONIA')
        insert into @codelist values('V', 'NEWG', 'NEW GUINEA')
        insert into @codelist values('V', 'NEWZ', 'NEW ZEALAND')
        insert into @codelist values('V', 'NICA', 'NICARAGUA')
        insert into @codelist values('V', 'NIGR', 'NIGER')
        insert into @codelist values('V', 'NIGE', 'NIGERIA')
        insert into @codelist values('V', 'NIUE', 'NIUE')
        insert into @codelist values('V', 'NORF', 'NORFOLK ISLAND')
        insert into @codelist values('V', 'NKOR', 'NORTH KOREA')
        insert into @codelist values('V', 'NMAR', 'NORTHERN MARIANA ISLANDS')
        insert into @codelist values('V', 'NORW', 'NORWAY')
        insert into @codelist values('V', 'OMAN', 'OMAN')
        insert into @codelist values('V', 'PAKI', 'PAKISTAN')
        insert into @codelist values('V', 'PALU', 'PALAU')
        insert into @codelist values('V', 'PANA', 'PANAMA')
        insert into @codelist values('V', 'PAPU', 'PAPUA NEW GUINEA')
        insert into @codelist values('V', 'PARA', 'PARAGUAY')
        insert into @codelist values('V', 'PERU', 'PERU')
        insert into @codelist values('V', 'PHIL', 'PHILIPPINES')
        insert into @codelist values('V', 'PITN', 'PITCAIRN')
        insert into @codelist values('V', 'POLA', 'POLAND')
        insert into @codelist values('V', 'PORT', 'PORTUGAL')
        insert into @codelist values('V', 'PURC', 'PUERTO RICO')
        insert into @codelist values('V', 'QATA', 'QATAR')
        insert into @codelist values('V', 'REUN', 'REUNION')
        insert into @codelist values('V', 'ROMA', 'ROMANIA')
        insert into @codelist values('V', 'RUMN', 'RUMANIA')
        insert into @codelist values('V', 'RUSS', 'RUSSIA')
        insert into @codelist values('V', 'RWAN', 'RWANDA')
        insert into @codelist values('V', 'SVCT', 'SAINT VINCENT AND T')
        insert into @codelist values('V', 'SAMO', 'SAMOA')
        insert into @codelist values('V', 'SANM', 'SAN MARINO')
        insert into @codelist values('V', 'SAOT', 'SAO TOME AND PRINCIPE')
        insert into @codelist values('V', 'SDOM', 'SANTO DOMINGO')
        insert into @codelist values('V', 'SAUD', 'SAUDI ARABIA')
        insert into @codelist values('V', 'SCOT', 'SCOTLAND')
        insert into @codelist values('V', 'SENE', 'SENEGAL')
        insert into @codelist values('V', 'SEYC', 'SEYCHELLES')
        insert into @codelist values('V', 'SIER', 'SIERRA LEONE')
        insert into @codelist values('V', 'SING', 'SINGAPORE')
        insert into @codelist values('V', 'SLOK', 'SLOVAKIA')
        insert into @codelist values('V', 'SLOV', 'SLOVENIA')
        insert into @codelist values('V', 'SOLO', 'SOLOMON ISLANDS')
        insert into @codelist values('V', 'SOMA', 'SOMALIA')
        insert into @codelist values('V', 'SAFR', 'SOUTH AFRICA')
        insert into @codelist values('V', 'SGEG', 'SOUTH GEORGIA AND T')
        insert into @codelist values('V', 'SKOR', 'SOUTH KOREA')
        insert into @codelist values('V', 'SOVT', 'SOVIET UNION')
        insert into @codelist values('V', 'SPAI', 'SPAIN')
        insert into @codelist values('V', 'STKI', 'ST. KITTS AND NEVIS')
        insert into @codelist values('V', 'STLU', 'SAINT LUCIA')
        insert into @codelist values('V', 'SRIL', 'SRI LANKA')
        insert into @codelist values('V', 'SHEL', 'ST HELENA')
        insert into @codelist values('V', 'SPIR', 'ST PIERRE AND MIQU')
        insert into @codelist values('V', 'STVI', 'ST VINCENT AND GRENADINES')
        insert into @codelist values('V', 'SUDA', 'SUDAN')
        insert into @codelist values('V', 'SURI', 'SURINAME')
        insert into @codelist values('V', 'SVAL', 'SVALBARD AND JAN MA')
        insert into @codelist values('V', 'SWAZ', 'SWAZILAND')
        insert into @codelist values('V', 'SWED', 'SWEDEN')
        insert into @codelist values('V', 'SWIT', 'SWITZERLAND')
        insert into @codelist values('V', 'SYRI', 'SYRIA')
        insert into @codelist values('V', 'SYRA', 'SYRIAN ARAB REPUBLIC')
        insert into @codelist values('V', 'TAIW', 'TAIWAN')
        insert into @codelist values('V', 'TAJI', 'TAJIKISTAN')
        insert into @codelist values('V', 'TANZ', 'TANZANIA')
        insert into @codelist values('V', 'THAI', 'THAILAND')
        insert into @codelist values('V', 'TIBE', 'TIBET')
        insert into @codelist values('V', 'TOBA', 'TOBAGO')
        insert into @codelist values('V', 'TOGO', 'TOGO')
        insert into @codelist values('V', 'TOKL', 'TOKELAU')
        insert into @codelist values('V', 'TONG', 'TONGA')
        insert into @codelist values('V', 'TRIN', 'TRINIDAD')
        insert into @codelist values('V', 'TRTB', 'TRINIDAD AND TOBAGO')
        insert into @codelist values('V', 'TUNI', 'TUNISIA')
        insert into @codelist values('V', 'TURK', 'TURKEY')
        insert into @codelist values('V', 'TURM', 'TURKMENISTAN')
        insert into @codelist values('V', 'TURC', 'TURKS AND CAICOS IS')
        insert into @codelist values('V', 'TUVA', 'TUVALU')
        insert into @codelist values('V', 'UGAN', 'UGANDA')
        insert into @codelist values('V', 'UKRA', 'UKRAINE')
        insert into @codelist values('V', 'UAEM', 'UNITED ARAB EMIRATES')
        insert into @codelist values('V', 'UNIS', 'UNION ISLANDS')
        insert into @codelist values('V', 'UNIT', 'UNITED KINGDOM')
        insert into @codelist values('V', 'UNST', 'UNITED STATES')
        insert into @codelist values('V', 'URUG', 'URUGUAY')
        insert into @codelist values('V', 'USBE', 'UZBEKISTAN')
        insert into @codelist values('V', 'VANU', 'VANUATU')
        insert into @codelist values('V', 'VATI', 'VATICAN CITY')
        insert into @codelist values('V', 'VENE', 'VENEZUELA')
        insert into @codelist values('V', 'VTNM', 'VIET NAM')
        insert into @codelist values('V', 'VIET', 'VIETNAM')
        insert into @codelist values('V', 'VIRG', 'VIRGIN ISLANDS')
        insert into @codelist values('V', 'BRVI', 'BRITISH VIRGIN ISLAND')
        insert into @codelist values('V', 'VIRU', 'US VIRGIN ISLANDS')
        insert into @codelist values('V', 'WALE', 'WALES')
        insert into @codelist values('V', 'WALF', 'WALLIS AND FUTUN')
        insert into @codelist values('V', 'WSTS', 'WESTERN SAHARA')
        insert into @codelist values('V', 'WSAM', 'WESTERN SAMOA')
        insert into @codelist values('V', 'YEME', 'YEMEN')
        insert into @codelist values('V', 'YEMR', 'YEMEN ARAB REBUPLIC')
        insert into @codelist values('V', 'YUGO', 'YUGOSLAVIA')
        insert into @codelist values('V', 'ZAIR', 'ZAIRE')
        insert into @codelist values('V', 'ZAMB', 'ZAMBIA')
        insert into @codelist values('V', 'ZIMB', 'ZIMBABWE')
        insert into @codelist values('V', 'UNKN', 'UNKNOWN')
        
        -- ****     DEPARTMENT   ****        
        insert into @codelist values('L', 'DEPT', 'DEPARTMENT')
        insert into @codelist values('V', 'ER', 'EMERGENCY ROOM')
        insert into @codelist values('V', 'LAB', 'LABORATORY')
        insert into @codelist values('V', 'PHAR', 'PHARMACY')
        
        -- ****     DISTRICT OF RESIDENCE   ****        
        insert into @codelist values('L', 'DISTRICT', 'DISTRICT OF RESIDENCE')
        insert into @codelist values('V', 'ESSX', 'ESSEX')
        insert into @codelist values('V', 'SHEF', 'SHEFFIELD')
        insert into @codelist values('V', 'HTZ', 'HANTZ')
        
        -- ****     DRIVER LICENSE ISSUER   ****        
        insert into @codelist values('L', 'DLISSUER', 'DRIVER LICENSE ISSUER')
        insert into @codelist values('V', 'CA', 'CALIFORNIA')
        insert into @codelist values('V', 'NV', 'NEVADA')
        insert into @codelist values('V', 'MEX', 'MEXICO')
        
        -- ****     ETHNIC   ****        
        insert into @codelist values('L', 'ETHNIC', 'ETHNIC')
        insert into @codelist values('V', '1', 'PUERTO RICAN')
        insert into @codelist values('V', '10', 'FILIPINO')
        insert into @codelist values('V', '11', 'KOREAN')
        insert into @codelist values('V', '12', 'VIETNAMESE')
        insert into @codelist values('V', '13', 'CAMBODIAN')
        insert into @codelist values('V', '14', 'LAOTIAN')
        insert into @codelist values('V', '15', 'THAI')
        insert into @codelist values('V', '16', 'HAWAIIAN')
        insert into @codelist values('V', '17', 'OTHER ASIAN/PACIFIC')
        insert into @codelist values('V', '18', 'ASIAN INDIAN')
        insert into @codelist values('V', '19', 'PAKISTANI')
        insert into @codelist values('V', '2', 'CUBAN')
        insert into @codelist values('V', '20', 'CAPE VERDEAN')
        insert into @codelist values('V', '21', 'OTHER PORTUGUESE')
        insert into @codelist values('V', '22', 'HAITIAN')
        insert into @codelist values('V', '23', 'JAMAICAN')
        insert into @codelist values('V', '24', 'BARBADIAN')
        insert into @codelist values('V', '25', 'OTHER W INDIAN')
        insert into @codelist values('V', '26', 'ARMENIAN')
        insert into @codelist values('V', '27', 'AUSTRIAN')
        insert into @codelist values('V', '28', 'ENGLISH')
        insert into @codelist values('V', '29', 'EGYPTIAN')
        insert into @codelist values('V', '3', 'DOMINICAN')
        insert into @codelist values('V', '30', 'FRENCH')
        insert into @codelist values('V', '31', 'FRENCH CANADIAN')
        insert into @codelist values('V', '32', 'GERMAN')
        insert into @codelist values('V', '33', 'GREEK')
        insert into @codelist values('V', '34', 'IRANIAN')
        insert into @codelist values('V', '35', 'IRISH')
        insert into @codelist values('V', '36', 'LEBANESE')
        insert into @codelist values('V', '37', 'LITHUANIAN')
        insert into @codelist values('V', '38', 'POLISH')
        insert into @codelist values('V', '39', 'RUSSIAN')
        insert into @codelist values('V', '4', 'MEXICAN')
        insert into @codelist values('V', '40', 'SCOTTISH')
        insert into @codelist values('V', '41', 'AMERICAN')
        insert into @codelist values('V', '42', 'OTHER')
        insert into @codelist values('V', '43', 'EUROPEAN')
        insert into @codelist values('V', '44', 'COLOMBIAN')
        insert into @codelist values('V', '45', 'SALVADORAN')
        insert into @codelist values('V', '46', 'AFRICAN AMERICAN')
        insert into @codelist values('V', '47', 'NIGERIAN')
        insert into @codelist values('V', '48', 'OTHER AFRICAN')
        insert into @codelist values('V', '5', 'CENTRAL AMERICAN')
        insert into @codelist values('V', '50', 'ISRAELI')
        insert into @codelist values('V', '51', 'OTHER MIDDLE EASTER')
        insert into @codelist values('V', '52', 'NATIVE AMER INDIAN')
        insert into @codelist values('V', '6', 'OTHER HISPANIC LATIN')
        insert into @codelist values('V', '8', 'JAPANESE')
        insert into @codelist values('V', '9', 'CHINESE')
        
        -- ****     GENDER   ****        
        insert into @codelist values('L', 'GENDER', 'GENDER')
        insert into @codelist values('V', 'M', 'MALE')
        insert into @codelist values('V', 'F', 'FEMALE')
        insert into @codelist values('V', 'O', 'OTHER')
        insert into @codelist values('V', 'U', 'UNKNOWN')
        
        -- ****     LANGUAGE   ****        
        insert into @codelist values('L', 'LANGUAGE', 'LANGUAGE')
        insert into @codelist values('V', 'ARAB', 'ARABIC')
        insert into @codelist values('V', 'ARME', 'ARMENAIN')
        insert into @codelist values('V', 'CAMB', 'CAMBODIAN')
        insert into @codelist values('V', 'CHIN', 'CHINESE')
        insert into @codelist values('V', 'CZEK', 'CZECH')
        insert into @codelist values('V', 'CAPE', 'CAPE VERDEAN')
        insert into @codelist values('V', 'CAPP', 'CAPE VERDEAN PORTUG')
        insert into @codelist values('V', 'CREO', 'CREOLE FRENCH')
        insert into @codelist values('V', 'DANE', 'DANISH')
        insert into @codelist values('V', 'ENGL', 'ENGLISH')
        insert into @codelist values('V', 'FRN', 'FRENCH')
        insert into @codelist values('V', 'GERM', 'GERMAN')
        insert into @codelist values('V', 'GREK', 'GREEK')
        insert into @codelist values('V', 'HATI', 'HATIAN')
        insert into @codelist values('V', 'HEBR', 'HEBREW')
        insert into @codelist values('V', 'HNDI', 'HINDI')
        insert into @codelist values('V', 'HUNG', 'HUNGARIAN')
        insert into @codelist values('V', 'ISLE', 'ISLANDIC')
        insert into @codelist values('V', 'ITAL', 'ITALIAN')
        insert into @codelist values('V', 'JAPA', 'JAPANESE')
        insert into @codelist values('V', 'KORE', 'KOREAN')
        insert into @codelist values('V', 'LAOT', 'LAOTIAN')
        insert into @codelist values('V', 'LATV', 'LATVIAN')
        insert into @codelist values('V', 'LITH', 'LITHUANIAN')
        insert into @codelist values('V', 'MONG', 'HMONG')
        insert into @codelist values('V', 'NORW', 'NORWEGIAN')
        insert into @codelist values('V', 'OTHE', 'OTHER')
        insert into @codelist values('V', 'PORT', 'PORTUGUESSE')
        insert into @codelist values('V', 'POPO', 'PORTUGESE-PORTUGUESE')
        insert into @codelist values('V', 'PRCI', 'PERSIAN FARSI')
        insert into @codelist values('V', 'RUSS', 'RUSSIAN')
        insert into @codelist values('V', 'SIGN', 'SIGN LANGUAGE')
        insert into @codelist values('V', 'SWED', 'SWEDISH')
        insert into @codelist values('V', 'THAI', 'THAI')
        insert into @codelist values('V', 'TURK', 'TURKISH')
        insert into @codelist values('V', 'UKRA', 'UKRANIAN')
        insert into @codelist values('V', 'UNKN', 'UNKNOWN')
        insert into @codelist values('V', 'VIET', 'VIETNAMESE')
        insert into @codelist values('V', 'YDSH', 'YIDDISH')
        insert into @codelist values('V', 'YUGO', 'YOGOSLAVIAN')
        
        -- ****     MARITAL STATUS   ****        
        insert into @codelist values('L', 'MSTATUS', 'MARITAL STATUS')
        insert into @codelist values('V', 'D', 'DIVORCED')
        insert into @codelist values('V', 'L', 'LEGALLY SEPARATED')
        insert into @codelist values('V', 'M', 'MARRIED')
        insert into @codelist values('V', 'P', 'PARTNER')
        insert into @codelist values('V', 'S', 'SINGLE')
        insert into @codelist values('V', 'U', 'UNKNOWN')
        insert into @codelist values('V', 'W', 'WIDOWED')
        
        -- ****     NATIONALITY   ****        
        insert into @codelist values('L', 'NATIONAL', 'NATIONALITY')
        insert into @codelist values('V', 'USA', 'AMERICAN')
        insert into @codelist values('V', 'CAN', 'CANADIAN')
        insert into @codelist values('V', 'MEX', 'MEXICAN')
        
        -- ****     PERSON CATEGORY   ****        
        insert into @codelist values('L', 'PERCAT', 'PERSON CATEGORY')
        insert into @codelist values('V', 'P', 'PERSON')
      
        -- ****     PHONE TYPE   ****        
        insert into @codelist values('L', 'PHONTYPE', 'PHONE TYPE')
        insert into @codelist values('V', 'CH', 'HOME')
        insert into @codelist values('V', 'CO', 'OFFICE')
        insert into @codelist values('V', 'CF', 'FAX')
        insert into @codelist values('V', 'CB', 'BUSINESS')
        insert into @codelist values('V', 'CBD', 'BUSINESS DIRECT')
        insert into @codelist values('V', 'CBA', 'BUSINESS ALTERNATE')
        insert into @codelist values('V', 'CP', 'PAGER')
        insert into @codelist values('V', 'CC', 'CELLULAR')
        
        -- ****     RACE   ****        
        insert into @codelist values('L', 'RACE', 'RACE')
        insert into @codelist values('V', 'I', 'AMERICAN INDIAN')
        insert into @codelist values('V', 'A', 'ASIAN')
        insert into @codelist values('V', 'B', 'BLACK')
        insert into @codelist values('V', 'H', 'HISPANIC')
        insert into @codelist values('V', 'O', 'OTHER')
        insert into @codelist values('V', 'R', 'REFUSED')
        insert into @codelist values('V', 'U', 'UNKNOWN')
        insert into @codelist values('V', 'W', 'WHITE')
        
        -- ****     RELIGION   ****        
        insert into @codelist values('L', 'RELIGION', 'RELIGION')
        insert into @codelist values('V', 'AC', 'ADVENT CHRISTIAN')
        insert into @codelist values('V', 'AG', 'AGNOSTIC')
        insert into @codelist values('V', 'AN', 'ANGLICAN')
        insert into @codelist values('V', 'AT', 'ATHEIST')
        insert into @codelist values('V', 'BA', 'BAHAI')
        insert into @codelist values('V', 'BP', 'BAPTIST')
        insert into @codelist values('V', 'BU', 'BUDHIST')
        insert into @codelist values('V', 'CH', 'CHRISTIAN')
        insert into @codelist values('V', 'CO', 'CONGREGATIONAL')
        insert into @codelist values('V', 'CS', 'CHRISTIAN SCIENTIST')
        insert into @codelist values('V', 'DC', 'DISCIPLES OF CHRIST')
        insert into @codelist values('V', 'DE', 'DEFERRED')
        insert into @codelist values('V', 'EC', 'EVANGELICAL CHRISTI')
        insert into @codelist values('V', 'EP', 'EPISCOPAL')
        insert into @codelist values('V', 'GO', 'GREEK ORTHODOX')
        insert into @codelist values('V', 'HI', 'HINDU')
        insert into @codelist values('V', 'JV', 'JEHOVAHS WITNESS')
        insert into @codelist values('V', 'JH', 'JEWISH')
        insert into @codelist values('V', 'LU', 'LUTHERAN')
        insert into @codelist values('V', 'ME', 'METHODIST')
        insert into @codelist values('V', 'MO', 'MORMON')
        insert into @codelist values('V', 'MS', 'MOSLEM')
        insert into @codelist values('V', 'MU', 'MUSLIM')
        insert into @codelist values('V', 'NO', 'NONE')
        insert into @codelist values('V', 'NP', 'NO PREFERENCE')
        insert into @codelist values('V', 'OR', 'ORTHODOX')
        insert into @codelist values('V', 'OT', 'OTHER')
        insert into @codelist values('V', 'PE', 'PENTECOSTAL HOLINES')
        insert into @codelist values('V', 'PR', 'PROTESTANT')
        insert into @codelist values('V', 'PS', 'PRESBYTERIAN')
        insert into @codelist values('V', 'QU', 'QUAKER')
        insert into @codelist values('V', 'RC', 'ROMAN CATHOLIC')
        insert into @codelist values('V', 'RO', 'RUSSIAN ORTHODOX')
        insert into @codelist values('V', 'SB', 'SOUTHERN BAPTIST')
        insert into @codelist values('V', 'SD', 'SEVENTH DAY ADVENTI')
        insert into @codelist values('V', 'UA', 'UNAFFILIATED')
        insert into @codelist values('V', 'UN', 'UNKNOWN')
        insert into @codelist values('V', 'UU', 'UNITARIAN')
        
        -- ****     STATE CODE   ****        
        insert into @codelist values('L', 'STATE', 'STATE CODE')
        insert into @codelist values('V', 'AL', 'ALABAMA')
        insert into @codelist values('V', 'AK', 'ALASKA')
        insert into @codelist values('V', 'AZ', 'ARIZONA')
        insert into @codelist values('V', 'AR', 'ARKANSAS')
        insert into @codelist values('V', 'CA', 'CALIFORNIA')
        insert into @codelist values('V', 'CO', 'COLORADO')
        insert into @codelist values('V', 'CT', 'CONNECTICUT')
        insert into @codelist values('V', 'DE', 'DELAWARE')
        insert into @codelist values('V', 'DC', 'DISTRICT OF COLUMBIA')
        insert into @codelist values('V', 'FL', 'FLORIDA')
        insert into @codelist values('V', 'GA', 'GEORGIA')
        insert into @codelist values('V', 'HI', 'HAWAII')
        insert into @codelist values('V', 'ID', 'IDAHO')
        insert into @codelist values('V', 'IL', 'ILLINOIS')
        insert into @codelist values('V', 'IN', 'INDIANA')
        insert into @codelist values('V', 'IA', 'IOWA')
        insert into @codelist values('V', 'KS', 'KANSAS')
        insert into @codelist values('V', 'KY', 'KENTUCKY')
        insert into @codelist values('V', 'LA', 'LOUISIANA')
        insert into @codelist values('V', 'ME', 'MAINE')
        insert into @codelist values('V', 'MD', 'MARYLAND')
        insert into @codelist values('V', 'MA', 'MASSACHUSETTS')
        insert into @codelist values('V', 'MI', 'MICHIGAN')
        insert into @codelist values('V', 'MN', 'MINNESOTA')
        insert into @codelist values('V', 'MS', 'MISSISSIPPI')
        insert into @codelist values('V', 'MO', 'MISSOURI')
        insert into @codelist values('V', 'MT', 'MONTANA')
        insert into @codelist values('V', 'NE', 'NEBRASKA')
        insert into @codelist values('V', 'NV', 'NEVADA')
        insert into @codelist values('V', 'NH', 'NEW HAMPSHIRE')
        insert into @codelist values('V', 'NJ', 'NEW JERSEY')
        insert into @codelist values('V', 'NM', 'NEW MEXICO')
        insert into @codelist values('V', 'NY', 'NEW YORK')
        insert into @codelist values('V', 'NC', 'NORTH CAROLINA')
        insert into @codelist values('V', 'ND', 'NORTH DAKOTA')
        insert into @codelist values('V', 'OH', 'OHIO')
        insert into @codelist values('V', 'OK', 'OKLAHOMA')
        insert into @codelist values('V', 'OR', 'OREGON')
        insert into @codelist values('V', 'PA', 'PENNSYLVANIA')
        insert into @codelist values('V', 'PR', 'PUERTO RICO')
        insert into @codelist values('V', 'RI', 'RHODE ISLAND')
        insert into @codelist values('V', 'SC', 'SOUTH CAROLINA')
        insert into @codelist values('V', 'SD', 'SOUTH DAKOTA')
        insert into @codelist values('V', 'TN', 'TENNESSEE')
        insert into @codelist values('V', 'TX', 'TEXAS')
        insert into @codelist values('V', 'UT', 'UTAH')
        insert into @codelist values('V', 'VT', 'VERMONT')
        insert into @codelist values('V', 'VI', 'VIRGIN ISLANDS')
        insert into @codelist values('V', 'VA', 'VIRGINIA')
        insert into @codelist values('V', 'WA', 'WASHINGTON')
        insert into @codelist values('V', 'WI', 'WISCONSIN')
        insert into @codelist values('V', 'WV', 'WEST VIRGINIA')
        insert into @codelist values('V', 'WY', 'WYOMING')
        insert into @codelist values('V', 'BC', 'BRITISH COLUMBIA')
        insert into @codelist values('V', 'YK', 'YUKON')
        insert into @codelist values('V', 'AB', 'ALBERTA')
        insert into @codelist values('V', 'SK', 'SASKATCHEWAN')
        insert into @codelist values('V', 'MB', 'MANITOBA')
        insert into @codelist values('V', 'ON', 'ONTARIO')
        insert into @codelist values('V', 'QU', 'QUEBEC')
        insert into @codelist values('V', 'NT', 'NORTHWEST TERRITORIES')
        insert into @codelist values('V', 'NF', 'NEWFOUNDLAND')
        insert into @codelist values('V', 'PE', 'PRINCE EDWARD ISLAND')
        insert into @codelist values('V', 'NB', 'NEW BRUNSWICH')
        insert into @codelist values('V', 'NS', 'NOVA SCOTIA')
        
        -- ****     REGION CODE   ****        
        insert into @codelist values('L', 'REGION', 'REGION CODE')
        insert into @codelist values('V', 'ABC', 'ABC REGION')
        insert into @codelist values('V', 'LMN', 'LMN REGION')
        
        -- ****     SUFFIX   ****        
        insert into @codelist values('L', 'SUFFIX', 'SUFFIX')
        insert into @codelist values('V', '2ND', '2ND')
        insert into @codelist values('V', '3RD', '3RD')
        insert into @codelist values('V', 'DDS', 'DDS')
        insert into @codelist values('V', 'DMD', 'DMD')
        insert into @codelist values('V', 'DVM', 'DVM')
        insert into @codelist values('V', 'ESQ', 'ESQ')
        insert into @codelist values('V', 'II', 'II')
        insert into @codelist values('V', 'III', 'III')
        insert into @codelist values('V', 'IV', 'IV')
        insert into @codelist values('V', 'JR', 'JR')
        insert into @codelist values('V', 'JRDDS', 'JRDDS')
        insert into @codelist values('V', 'JRESQ', 'JRESQ')
        insert into @codelist values('V', 'JRMD', 'JRMD')
        insert into @codelist values('V', 'JRPHD', 'JRPHD')
        insert into @codelist values('V', 'MD', 'MD')
        insert into @codelist values('V', 'MDESQ', 'MDESQ')
        insert into @codelist values('V', 'MDPHD', 'MDPHD')
        insert into @codelist values('V', 'RN', 'RN')
        insert into @codelist values('V', 'SR', 'SR')
        insert into @codelist values('V', 'SRDDS', 'SRDDS')
        insert into @codelist values('V', 'SRESQ', 'SRESQ')
        insert into @codelist values('V', 'SRMD', 'SRMD')
        insert into @codelist values('V', 'SRPHD', 'SRPHD')
        
        -- ****     SOURCE   ****        
        insert into @codelist values('L', 'SYSTEM', 'SOURCE')
        insert into @codelist values('V', 'HDQ', 'HEADQUARTERS')
        insert into @codelist values('V', 'FAC', 'FACILITY')
        
        -- ****     TITLE   ****        
        insert into @codelist values('L', 'TITLE', 'TITLE')
        insert into @codelist values('V', 'MD', 'MEDICAL DOCTOR')
        insert into @codelist values('V', 'PHD', 'PHD')
        insert into @codelist values('V', 'RN', 'REGISTERED NURSE')
        insert into @codelist values('V', 'CNA', 'CERTIFIED NURSES AID')
        insert into @codelist values('V', 'LVN', 'LVN')
        
        -- ****     VETERAN STATUS   ****        
        insert into @codelist values('L', 'VETSTS', 'VETERAN STATUS')
        insert into @codelist values('V', 'N', 'NONE')
        insert into @codelist values('V', 'Y', 'YES')
        insert into @codelist values('V', 'U', 'UNKNOWN')
        
        -- ****     VIP FLAG   ****        
        insert into @codelist values('L', 'VIPFLAG', 'VIP FLAG')
        insert into @codelist values('V', 'V', 'VIP')
        insert into @codelist values('V', 'E', 'EMPLOYEE')
        insert into @codelist values('V', 'N', 'NONE')


    select @v_date = getdate()
    select @v_appl_id = APPL_ID from SBYN_APPL where code = 'EV';
    if @v_appl_id is null or @@error <> 0
    begin
        print 'ERROR: APPL_ID not found for EV.'
        goto abort
    end

    -- remove values of the codelist that matches the application id
    delete from SBYN_COMMON_DETAIL where COMMON_HEADER_ID in 
        (select  COMMON_HEADER_ID from SBYN_COMMON_HEADER
        where APPL_ID = @v_appl_id)

    -- remove header info that matches the application id
    delete from SBYN_COMMON_HEADER 
    where APPL_ID = @v_appl_id    

    update SBYN_SEQ_TABLE set SEQ_COUNT = SEQ_COUNT + 1 
    where SEQ_NAME = 'SBYN_COMMON_HEADER'

    select @v_header_seq = SEQ_COUNT - 1 from SBYN_SEQ_TABLE 
    where SEQ_NAME = 'SBYN_COMMON_HEADER'

    update SBYN_SEQ_TABLE set SEQ_COUNT = SEQ_COUNT + 1 
    where SEQ_NAME = 'SBYN_COMMON_DETAIL'

    select @v_detail_seq  = SEQ_COUNT - 1 from SBYN_SEQ_TABLE 
    where SEQ_NAME = 'SBYN_COMMON_DETAIL'

    -- loop through the codelist and populate 
    -- SBYN_COMMON_DETAIL and SBYN_COMMON_DETAIL

    open c1

    fetch next from c1 into @v_objtype, @v_code, @v_descr

    while @@FETCH_STATUS = 0
    begin

        if @v_objtype = 'L'
        begin

            insert into SBYN_COMMON_HEADER (COMMON_HEADER_ID, 
                                            APPL_ID, 
                                            CODE, 
                                            DESCR, 
                                            READ_ONLY, 
                                            MAX_INPUT_LEN, 
                                            TYP_TABLE_CODE, 
                                            CREATE_DATE,
                                            CREATE_USERID)
            values (@v_header_seq, 
                    @v_appl_id, 
                    @v_code, 
                    @v_descr, 
                    'Y', 
                    8, 
                    'XX', 
                    @v_date, 
                    system_user );

            select @v_header_seq = @v_header_seq + 1;

        end
        else
        begin

            insert into SBYN_COMMON_DETAIL(common_detail_id, 
                                            COMMON_HEADER_ID, 
                                            CODE, 
                                            DESCR, 
                                            READ_ONLY, 
                                            CREATE_DATE, 
                                            CREATE_USERID)
            values (@v_detail_seq, 
                    @v_header_seq-1, 
                    @v_code , 
                    @v_descr, 
                    'N', 
                    @v_date, 
                    system_user )

            select @v_detail_seq = @v_detail_seq + 1

        end

        fetch next from c1 into @v_objtype, @v_code, @v_descr
    end

    close c1
    deallocate c1

    update SBYN_SEQ_TABLE set SEQ_COUNT = @v_header_seq
    where SEQ_NAME = 'SBYN_COMMON_HEADER'

    update SBYN_SEQ_TABLE set SEQ_COUNT = @v_detail_seq
    where SEQ_NAME = 'SBYN_COMMON_DETAIL'

commit
abort:
end
go
