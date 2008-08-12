
create or replace type tCode as object (
  type varchar2(1),
  code  varchar2(30),
  descr varchar2(50) 
);
/  
declare
    v_appl_id		integer;
    v_header_seq	integer;
    v_detail_seq	integer;
    v_date              varchar2(10);
    v_user          varchar2(20);
    v_idx           integer;

    TYPE tCodeList IS TABLE OF tCode;
    
    codes tCodeList := tCodeList(

        -- ****     SUFFIX   ****
        tCode('L', 'SUFFIX', 'Suffix'),
        tCode('V', '2ND', '2ND'),
        tCode('V', '3RD', '3RD'),
        tCode('V', 'DDS', 'DDS'),
        tCode('V', 'ESQ', 'ESQ'),
        tCode('V', 'II', 'II'),
        tCode('V', 'III', 'III'),
        tCode('V', 'IV', 'IV'),
        tCode('V', 'JR', 'JR'),
        tCode('V', 'MD', 'MD'),
        tCode('V', 'RN', 'RN'),
        tCode('V', 'SR', 'SR'),
        tCode('V', 'PHD', 'PHD'),
        tCode('V', 'MA', 'MA'),

        -- ****     TITLE   ****
        tCode('L', 'TITLE', 'Title'),
        tCode('V', 'MD', 'MEDICAL DOCTOR'),
        tCode('V', 'PHD', 'DOCTOR'),
        tCode('V', 'RN', 'REGISTERED NURSE'),
        tCode('V', 'CNA', 'CERTIFIED NURSES AID'),
        tCode('V', 'LVN', 'LICENSED VOCATIONAL NURSE'),
        tCode('V', 'REV', 'REVERAND'),
        tCode('V', 'MR', 'MR'),
	tCode('V', 'MRS', 'MRS'),
        tCode('V', 'MISS', 'MISS'),

        -- ****     GENDER   ****
        --  MALE / Female from HIPAA Standard
        --  UNKNOWN from CMS FISS Standard
        tCode('L', 'GENDER', 'Gender'),
        tCode('V', 'M', 'MALE'),
        tCode('V', 'F', 'FEMALE'),
        tCode('V', 'U', 'UNKNOWN'),


        -- ****     MSTATUS   ****
        tCode('L', 'MSTATUS', 'Marital Status'),
        tCode('V', 'D', 'DIVORCED'),
        tCode('V', 'L', 'LEGALLY SEPARATED'),
        tCode('V', 'M', 'MARRIED'),
        tCode('V', 'P', 'PARTNER'),
        tCode('V', 'S', 'SINGLE'),
        tCode('V', 'U', 'UNKNOWN'),
        tCode('V', 'W', 'WIDOWED'),

        -- ****     RACE   ****
        tCode('L', 'RACE', 'Race'),
        tCode('V', 'I', 'AMERICAN INDIAN'),
        tCode('V', 'A', 'ASIAN'),
        tCode('V', 'B', 'BLACK'),
        tCode('V', 'H', 'HISPANIC'),
        tCode('V', 'O', 'OTHER'),
        tCode('V', 'R', 'REFUSED'),
        tCode('V', 'U', 'UNKNOWN'),
        tCode('V', 'W', 'WHITE'),

        -- ****     ETHNIC   ****
        tCode('L', 'ETHNIC', 'Ethnicity'),
        tCode('V', '1', 'PUERTO RICAN'),
        tCode('V', '10', 'FILIPINO'),
        tCode('V', '11', 'KOREAN'),
        tCode('V', '12', 'VIETNAMESE'),
        tCode('V', '13', 'CAMBODIAN'),
        tCode('V', '14', 'LAOTIAN'),
        tCode('V', '15', 'THAI'),
        tCode('V', '16', 'HAWAIIAN'),
        tCode('V', '17', 'OTHER ASIAN/PACIFIC'),
        tCode('V', '19', 'PAKISTANI'),
        tCode('V', '2', 'CUBAN'),
        tCode('V', '22', 'HAITIAN'),
        tCode('V', '23', 'JAMAICAN'),
        tCode('V', '24', 'BARBADIAN'),
        tCode('V', '25', 'WEST INDIAN'),
        tCode('V', '26', 'ARMENIAN'),
        tCode('V', '27', 'AUSTRIAN'),
        tCode('V', '28', 'ENGLISH'),
        tCode('V', '29', 'EGYPTIAN'),
        tCode('V', '3', 'DOMINICAN'),
        tCode('V', '30', 'FRENCH'),
        tCode('V', '31', 'CANADIAN'),
        tCode('V', '32', 'GERMAN'),
        tCode('V', '33', 'GREEK'),
        tCode('V', '34', 'IRANIAN'),
        tCode('V', '35', 'IRISH'),
        tCode('V', '36', 'LEBANESE'),
        tCode('V', '37', 'LITHUANIAN'),
        tCode('V', '38', 'POLISH'),
        tCode('V', '39', 'RUSSIAN'),
        tCode('V', '4',  'MEXICAN'),
        tCode('V', '40', 'SCOTTISH'),
        tCode('V', '42', 'OTHER'),
        tCode('V', '43', 'EUROPEAN'),
        tCode('V', '44', 'COLOMBIAN'),
        tCode('V', '45', 'SALVADORAN'),
        tCode('V', '46', 'AFRICAN AMERICAN'),
        tCode('V', '47', 'NIGERIAN'),
        tCode('V', '48', 'OTHER AFRICAN'),
        tCode('V', '50', 'ISRAELI'),
        tCode('V', '51', 'OTHER MIDDLE EASTERN'),
        tCode('V', '52', 'NATIVE AMERICAN'),
        tCode('V', '6', 'OTHER HISPANIC'),
        tCode('V', '7', 'AUSTRALIAN'),
        tCode('V', '8', 'JAPANESE'),
        tCode('V', '9', 'CHINESE'),

        -- ****     RELIGION   ****
        tCode('L', 'RELIGION', 'Religion'),
        tCode('V', 'AG', 'AGNOSTIC'),
        tCode('V', 'AN', 'ANGLICAN'),
        tCode('V', 'AT', 'ATHEIST'),
        tCode('V', 'BP', 'BAPTIST'),
        tCode('V', 'BU', 'BUDHIST'),
        tCode('V', 'CH', 'CHRISTIAN'),
        tCode('V', 'CO', 'CONGREGATIONAL'),
        tCode('V', 'CS', 'CHRISTIAN SCIENTIST'),
        tCode('V', 'DE', 'DEFERRED'),
        tCode('V', 'EP', 'EPISCOPAL'),
        tCode('V', 'GO', 'GREEK ORTHODOX'),
        tCode('V', 'HI', 'HINDU'),
        tCode('V', 'JW', 'JEHOVAHS WITNESS'),
        tCode('V', 'JH', 'JEWISH'),
        tCode('V', 'LU', 'LUTHERAN'),
        tCode('V', 'ME', 'METHODIST'),
        tCode('V', 'MO', 'MORMON'),
        tCode('V', 'MU', 'MUSLIM'),
        tCode('V', 'NO', 'NONE'),
        tCode('V', 'OR', 'ORTHODOX'),
        tCode('V', 'OT', 'OTHER'),
        tCode('V', 'PS', 'PRESBYTERIAN'),
        tCode('V', 'QU', 'QUAKER'),
        tCode('V', 'RC', 'ROMAN CATHOLIC'),
        tCode('V', 'SD', 'SEVENTH DAY ADVENTI'),
        tCode('V', 'UA', 'UNAFFILIATED'),
        tCode('V', 'UN', 'UNKNOWN'),
        tCode('V', 'UU', 'UNITARIAN'),

        -- ****     LANGUAGE   ****
        tCode('L', 'LANGUAGE', 'Language'),
        tCode('V', 'ARAB', 'ARABIC'),
        tCode('V', 'CHIN', 'CHINESE'),
        tCode('V', 'CZEK', 'CZECH'),
        tCode('V', 'CREO', 'CREOLE FRENCH'),
        tCode('V', 'DANE', 'DANISH'),
        tCode('V', 'ENGL', 'ENGLISH'),
        tCode('V', 'FRN', 'FRENCH'),
        tCode('V', 'GERM', 'GERMAN'),
        tCode('V', 'GREK', 'GREEK'),
        tCode('V', 'HATI', 'HATIAN'),
        tCode('V', 'HEBR', 'HEBREW'),
        tCode('V', 'HNDI', 'HINDI'),
        tCode('V', 'ITAL', 'ITALIAN'),
        tCode('V', 'JAPA', 'JAPANESE'),
        tCode('V', 'KORE', 'KOREAN'),
        tCode('V', 'OTHE', 'OTHER'),
        tCode('V', 'PORT', 'PORTUGUESE'),
        tCode('V', 'PRCI', 'PERSIAN/FARSI'),
        tCode('V', 'RUSS', 'RUSSIAN'),
        tCode('V', 'SIGN', 'SIGN LANGUAGE'),
        tCode('V', 'SPAN', 'SPANISH'),
        tCode('V', 'SWED', 'SWEDISH'),
        tCode('V', 'THAI', 'THAI'),
        tCode('V', 'TURK', 'TURKISH'),
        tCode('V', 'UNKN', 'UNKNOWN'),
        tCode('V', 'YDSH', 'YIDDISH'),


        -- ****     NATIONAL   ****
        tCode('L', 'NATIONAL', 'Nationality'),
        tCode('V', 'USA', 'AMERICAN'),
        tCode('V', 'CAN', 'CANADIAN'),
        tCode('V', 'MEX', 'MEXICAN'),


        -- ****     CITIZEN   ****
       tCode('L', 'CITIZEN', 'Citizenship'),
       tCode('V', 'USA', 'UNITED STATES'),
       tCode('V', 'CAN', 'CANADA'),
       tCode('V', 'MEX', 'MEXICO'),

        -- ****     ADDRTYPE   ****
        tCode('L', 'ADDRTYPE', 'Address Type'),
        tCode('V', 'O', 'OFFICE'),
        tCode('V', 'H', 'HOME'),
        tCode('V', 'V', 'VACATION'),
        tCode('V', 'H2','HOME 2'),
        tCode('V', 'M', 'MAILING'),
        tCode('V', 'W', 'WORK'),
        tCode('V', 'W2', 'WORK 2'),
        tCode('V', 'T', 'TEMPORARY'),

        -- ****     PHONTYPE   ****
        tCode('L', 'PHONTYPE', 'Phone Type'),
        tCode('V', 'CH', 'HOME'),
        tCode('V', 'CO', 'OFFICE'),
        tCode('V', 'CF', 'FAX'),
        tCode('V', 'CB', 'BUSINESS'),
        tCode('V', 'CBD', 'BUSINESS DIRECT'),
        tCode('V', 'CBA', 'BUSINESS ALTERNATE'),
        tCode('V', 'CP', 'PAGER'),
        tCode('V', 'CC', 'CELLULAR'),

        -- ****     RELATIONSHIP   ****
        tCode('L', 'RELATION', 'Relationship'),
        tCode('V', 'PARENT', 'PARENT'),
        tCode('V', 'CHILD', 'CHILD'),
        tCode('V', 'SPOUSE', 'SPOUSE'),
        tCode('V', 'FAMILY_MEMBER', 'FAMILY_MEMBER'),
        tCode('V', 'OTHER', 'OTHER'),
        tCode('V', 'UNKNOWN', 'UNKNOWN'),

        -- ****     POLICYTYPE   ****
        tCode('L', 'POLTYPE', 'Policy Type'),
        tCode('V', 'HMO', 'HMO'),
        tCode('V', 'PPO', 'PPO'),
        tCode('V', 'MED', 'MEDICARE'),
        tCode('V', 'OTHER', 'OTHER'),
        tCode('V', 'UNKNOWN', 'UNKNOWN'),

        -- ****     STATE   ****
        tCode('L', 'STATE', 'State'),
        tCode('V', 'AL', 'ALABAMA'),
        tCode('V', 'AK', 'ALASKA'),
        tCode('V', 'AZ', 'ARIZONA'),
        tCode('V', 'AR', 'ARKANSAS'),
        tCode('V', 'CA', 'CALIFORNIA'),
        tCode('V', 'CO', 'COLORADO'),
        tCode('V', 'CT', 'CONNECTICUT'),
        tCode('V', 'DE', 'DELAWARE'),
        tCode('V', 'DC', 'DISTRICT OF COLUMBIA'),
        tCode('V', 'FL', 'FLORIDA'),
        tCode('V', 'GA', 'GEORGIA'),
        tCode('V', 'HI', 'HAWAII'),
        tCode('V', 'ID', 'IDAHO'),
        tCode('V', 'IL', 'ILLINOIS'),
        tCode('V', 'IN', 'INDIANA'),
        tCode('V', 'IA', 'IOWA'),
        tCode('V', 'KS', 'KANSAS'),
        tCode('V', 'KY', 'KENTUCKY'),
        tCode('V', 'LA', 'LOUISIANA'),
        tCode('V', 'ME', 'MAINE'),
        tCode('V', 'MD', 'MARYLAND'),
        tCode('V', 'MA', 'MASSACHUSETTS'),
        tCode('V', 'MI', 'MICHIGAN'),
        tCode('V', 'MN', 'MINNESOTA'),
        tCode('V', 'MS', 'MISSISSIPPI'),
        tCode('V', 'MO', 'MISSOURI'),
        tCode('V', 'MT', 'MONTANA'),
        tCode('V', 'NE', 'NEBRASKA'),
        tCode('V', 'NV', 'NEVADA'),
        tCode('V', 'NH', 'NEW HAMPSHIRE'),
        tCode('V', 'NJ', 'NEW JERSEY'),
        tCode('V', 'NM', 'NEW MEXICO'),
        tCode('V', 'NY', 'NEW YORK'),
        tCode('V', 'NC', 'NORTH CAROLINA'),
        tCode('V', 'ND', 'NORTH DAKOTA'),
        tCode('V', 'OH', 'OHIA'),
        tCode('V', 'OK', 'OKLAHOMA'),
        tCode('V', 'OR', 'OREGON'),
        tCode('V', 'PA', 'PENNSYLVANIA'),
        tCode('V', 'PR', 'PUERTO RICO'),
        tCode('V', 'RI', 'RHODE ISLAND'),
        tCode('V', 'SC', 'SOUTH CAROLINA'),
        tCode('V', 'SD', 'SOUTH DAKOTA'),
        tCode('V', 'TN', 'TENNESSEE'),
        tCode('V', 'TX', 'TEXAS'),
        tCode('V', 'UT', 'UTAH'),
        tCode('V', 'VT', 'VERMONT'),
        tCode('V', 'VI', 'VIRGIN ISLANDS'),
        tCode('V', 'VA', 'VIRGINIA'),
        tCode('V', 'WA', 'WASHINGTON'),
        tCode('V', 'WI', 'WISCONSIN'),
        tCode('V', 'WV', 'WEST VIRGINIA'),
        tCode('V', 'WY', 'WYOMING'),

        -- ****     COUNTRY   ****
        tCode('L', 'COUNTRY', 'Country'),
        tCode('V', 'AUST', 'AUSTRALIA'),
        tCode('V', 'BAHA', 'BAHAMAS'),
        tCode('V', 'BERM', 'BERMUDA'),
        tCode('V', 'CANA', 'CANADA'),
        tCode('V', 'ENGL', 'ENGLAND'),
        tCode('V', 'FRAN', 'FRANCE'),
        tCode('V', 'GRBR', 'GREAT BRITAIN'),
        tCode('V', 'IREL', 'IRELAND'),
        tCode('V', 'MEXI', 'MEXICO'),
        tCode('V', 'UNST', 'UNITED STATES'),
        tCode('V', 'VIRU', 'US VIRGIN ISLANDS'),
        tCode('V', 'UNKNOWN', 'UNKNOWN'),

        -- ****     CONTACTROLE  ****
        tCode('L', 'CONTROLE', 'Contact Role'),
        tCode('V', 'EMERGENCY', 'EMERGENCY'),
        tCode('V', 'GUARANTOR', 'GUARANTOR'),
        tCode('V', 'NEXT_OF_KIN', 'NEXT OF KIN'),
        tCode('V', 'OTHER', 'OTHER'),
        tCode('V', 'UNKNOWN', 'UNKNOWN'),


        -- **** REL ****
        --   CMS FISS 
        tCode('L', 'REL', 'Patients Relationship to Insured'),
        tCode('V', '01' , 'Spouse'),
        tCode('V', '04' , 'Grandfather or Grandmother'),
        tCode('V', '05' , 'Grandson or Granddaughter'),
        tCode('V', '07' , 'Nephew or Niece'),
        tCode('V', '10' , 'Foster Child'),
        tCode('V', '15' , 'Ward of the Court'),
        tCode('V', '17' , 'Stepson or Stepdaughter'),
        tCode('V', '18' , 'Self'),
        tCode('V', '19' , 'Child'),
        tCode('V', '20' , 'Employee'),
        tCode('V', '21' , 'Unknown'),
        tCode('V', '22' , 'Handicapped Dependent'),
        tCode('V', '23' , 'Sponsored Dependent'),
        tCode('V', '24' , 'Dependent of Minor Dependent'),
        tCode('V', '29' , 'Significant Other'),
        tCode('V', '32' , 'Mother'),
        tCode('V', '33' , 'Father'),
        tCode('V', '36' , 'Emancipated Minor'),
        tCode('V', '39' , 'Organ Donor'),
        tCode('V', '40' , 'Cadaver Donor'),
        tCode('V', '41' , 'Injured Plaintiff'),
        tCode('V', '43' , 'Child No Financial Resp.'),
        tCode('V', '53' , 'Life Partner'),
        tCode('V', 'G8' , 'Other'),

        -- **** PATREL ****
        --    CMS FISS
        tCode('L', 'PATREL', 'Subscriber relationship to patient'),
        tCode('V', '01' , 'Patient is insured'),
        tCode('V', '02' , 'Spouse'),
        tCode('V', '03' , 'Natural child'),
        tCode('V', '04' , 'Natural child not responsible'),
        tCode('V', '05' , 'Step-child'),
        tCode('V', '06' , 'Foster child'),
        tCode('V', '07' , 'Ward of the court'),
        tCode('V', '08' , 'Employee'),
        tCode('V', '09' , 'Unknown'),
        tCode('V', '10' , 'Handicapped dependent'),
        tCode('V', '11' , 'Organ donor'),
        tCode('V', '12' , 'Cadaver donor'),
        tCode('V', '13' , 'Grandchild'),
        tCode('V', '14' , 'Niece/nephew'),
        tCode('V', '15' , 'Injured plaintiff'),
        tCode('V', '16' , 'Dependent sponsored'),
        tCode('V', '17' , 'Minor dependent of a minor dependent'),
        tCode('V', '18' , 'Parent'),
        tCode('V', '19' , 'Grandparent'),

        -- **** MEDREL ****
        --   UB-04 CMS Pub 100-04 Medicare Claims Processing
        tCode('L', 'MEDREL', 'Patients Relationship to Insured'),
        tCode('V', '01' , 'Spouse'),
        tCode('V', '18' , 'Self'),
        tCode('V', '19' , 'Child'),
        tCode('V', '20' , 'Employee'),
        tCode('V', '21' , 'Unknown'),
        tCode('V', '39' , 'Organ Donor'),
        tCode('V', '40' , 'Cadaver Donor'),
        tCode('V', '53' , 'Life Partner'),
        tCode('V', 'G8' , 'Other')

    );

begin

    select TO_CHAR(sysdate, 'MM/DD/YYYY') into v_date from dual;
    select user into v_user from dual;
    
    select appl_id into v_appl_id from sbyn_appl where code = 'EV';
    if SQL%NOTFOUND then
        dbms_output.put_line('ERROR: appl_id not found for EV.');
        goto abort;
    end if;

    delete from sbyn_common_detail where common_header_id in 
        (select common_header_id from sbyn_common_header
        where appl_id = v_appl_id);

    delete from sbyn_common_header 
    where appl_id = v_appl_id;

    update sbyn_seq_table set seq_count = seq_count + 1 
    where seq_name = 'SBYN_COMMON_HEADER';

    select seq_count - 1 into v_header_seq from sbyn_seq_table 
    where seq_name = 'SBYN_COMMON_HEADER';

    update sbyn_seq_table set seq_count = seq_count + 1 
    where seq_name = 'SBYN_COMMON_DETAIL';

    select seq_count - 1 into v_detail_seq from sbyn_seq_table 
    where seq_name = 'SBYN_COMMON_DETAIL';

    v_idx := codes.FIRST;
    WHILE v_idx <= codes.LAST LOOP
        IF codes(v_idx).type = 'L' THEN
            insert into sbyn_common_header (common_header_id, appl_id, code, descr, read_only, max_input_len, typ_table_code, create_date, create_userid)
            values (v_header_seq, v_appl_id, codes(v_idx).code, codes(v_idx).descr , 'Y', 12, 'XX', TO_DATE(v_date, 'MM/DD/YYYY'), v_user );
            v_header_seq := v_header_seq + 1;
        ELSE
            insert into sbyn_common_detail (common_detail_id, common_header_id, code, descr, read_only, create_date, create_userid)
            values (v_detail_seq, v_header_seq-1, codes(v_idx).code , codes(v_idx).descr, 'N', TO_DATE(v_date, 'MM/DD/YYYY'), v_user );
            v_detail_seq := v_detail_seq + 1;
        END IF;
        v_idx := codes.NEXT(v_idx);
    END LOOP;

    update sbyn_seq_table set seq_count =  v_header_seq
    where seq_name = 'SBYN_COMMON_HEADER';

    update sbyn_seq_table set seq_count =  v_detail_seq
    where seq_name = 'SBYN_COMMON_DETAIL';

    commit;

<<abort>>
    rollback;
end;
/
