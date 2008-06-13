
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
    v_date		    varchar2(10);
    v_user          varchar2(20);
    v_idx           integer;

    TYPE tCodeList IS TABLE OF tCode;
    
    codes tCodeList := tCodeList(

        -- ****     CATEGORY   ****
        tCode('L', 'CATEGORY', 'Category'),
        tCode('V', 'PERSON', 'PERSON'),

        -- ****     GENDER   ****
        tCode('L', 'GENDER', 'Gender'),
        tCode('V', 'M', 'MALE'),
        tCode('V', 'F', 'FEMALE'),
        tCode('V', 'O', 'OTHER'),
        tCode('V', 'U', 'UNKNOWN'),

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

        -- ****     CONTACT_METHOD   ****
        tCode('L', 'CONMETH', 'Contact Method'),
        tCode('V', 'CH', 'HOME TELECOM'),
        tCode('V', 'CO', 'WORK TELECOM'),
        tCode('V', 'CC', 'CELLULAR TELECOM'),
        tCode('V', 'MA', 'MAIL'),
        tCode('V', 'EM','EMAIL'),
        tCode('V', 'CP', 'PAGER'),
        tCode('V', 'O', 'OTHER'),
        tCode('V', 'U', 'UNKNOWN'),

        -- ****     RELATIONSHIP   ****
	tCode('L', 'RELATION', 'Relationship'),
	tCode('V', 'PARENT', 'PARENT'),
	tCode('V', 'CHILD', 'CHILD'),
	tCode('V', 'SPOUSE', 'SPOUSE'),
	tCode('V', 'FAMILY_MEMBER', 'FAMILY_MEMBER'),
	tCode('V', 'OTHER', 'OTHER'),
        tCode('V', 'UNKNOWN', 'UNKNOWN'),

        -- ****     TITLE   ****
        tCode('L', 'TITLE', 'Title'),
        tCode('V', 'MD',   'MEDICAL DOCTOR'),
        tCode('V', 'PHD',  'DOCTOR'),
        tCode('V', 'RN',   'REGISTERED NURSE'),
        tCode('V', 'CNA',  'CERTIFIED NURSES AID'),
        tCode('V', 'LVN',  'LICENSED VOCATIONAL NURSE'),
        tCode('V', 'REV',  'REVERAND'),
        tCode('V', 'LORD', 'LORD'),

        -- ****     SUFFIX   ****
        tCode('L', 'SUFFIX', 'Suffix'),
        tCode('V', '2ND', '2ND'),
        tCode('V', '3RD', '3RD'),
        tCode('V', 'DDS', 'DDS'),
        tCode('V', 'ESQ', 'ESQ'),
        tCode('V', 'II',  'II'),
        tCode('V', 'III', 'III'),
        tCode('V', 'IV', 'IV'),
        tCode('V', 'JR', 'JR'),
        tCode('V', 'MD', 'MD'),
        tCode('V', 'RN', 'RN'),
        tCode('V', 'SR', 'SR'),
        tCode('V', 'PHD', 'PHD'),
        tCode('V', 'MA', 'MA'),

        -- ****     ADDRTYPE   ****
        tCode('L', 'ADDRTYPE', 'Address Type'),
        tCode('V', 'O',  'OFFICE'),
        tCode('V', 'H',  'HOME'),
        tCode('V', 'V',  'VACATION'),
        tCode('V', 'H2', 'HOME 2'),
        tCode('V', 'M',  'MAILING'),
        tCode('V', 'W',  'WORK'),
        tCode('V', 'W2', 'WORK 2'),
        tCode('V', 'T',  'TEMPORARY'),

        -- ****     TELECOMTYPE   ****
        tCode('L', 'TELETYPE', 'Telecom Type'),
        tCode('V', 'CH', 'HOME'),
        tCode('V', 'CO', 'OFFICE'),
        tCode('V', 'CF', 'FAX'),
        tCode('V', 'CB', 'BUSINESS'),
        tCode('V', 'CBD', 'BUSINESS DIRECT'),
        tCode('V', 'CBA', 'BUSINESS ALTERNATE'),
        tCode('V', 'CP', 'PAGER'),
        tCode('V', 'CC', 'CELLULAR'),


        -- ****    ETHNIC_CODE   ****
        tCode('L', 'ETHNIC', 'Ethnic Category Code'),
        tCode('V', 'A', 'British'), 
        tCode('V', 'B', 'Irish'), 
        tCode('V', 'C', 'Any other White background'), 
        tCode('V', 'D', 'White and Black Caribbean'),
        tCode('V', 'E', 'White and Black African'),
        tCode('V', 'F', 'White and Asian'), 
        tCode('V', 'G', 'Any other mixed background'), 
        tCode('V', 'H', 'Indian'), 
        tCode('V', 'J', 'Pakistani'),
        tCode('V', 'K', 'Bangladeshi'), 
        tCode('V', 'L', 'Any other Asian background'), 
        tCode('V', 'M', 'Caribbean'), 
        tCode('V', 'N', 'African'), 
        tCode('V', 'P', 'Any other Black background'),  
        tCode('V', 'R', 'Chinese'), 
        tCode('V', 'S', 'Any other ethnic group'), 
        tCode('V', 'Z', 'Not stated'), 

        
        -- ****     MSTATUS   ****
        tCode('L', 'MSTATUS', 'Marital Status'),
        tCode('V', 'D', 'DIVORCED'),
        tCode('V', 'L', 'LEGALLY SEPARATED'),
        tCode('V', 'M', 'MARRIED'),
        tCode('V', 'P', 'PARTNERED'),
        tCode('V', 'S', 'SINGLE'),
        tCode('V', 'U', 'UNKNOWN'),
        tCode('V', 'W', 'WIDOWED'),
        
       
        
        -- ****     CONTACTROLE  ****
	tCode('L', 'CONROLE', 'Contact Role'),
	tCode('V', 'EMERGENCY', 'EMERGENCY'),
	tCode('V', 'GUARANTOR', 'GUARANTOR'),
	tCode('V', 'NEXT_OF_KIN', 'NEXT OF KIN'),
	tCode('V', 'OTHER', 'OTHER'),
        tCode('V', 'UNKNOWN', 'UNKNOWN'),

        -- ****     IDNUM_TYPE   ****
        tCode('L', 'IDTYPE', 'Id Number Type'),
        tCode('V', 'NHS', 'NHS NUMBER'),
        tCode('V', 'CHI', 'CHI NUMBER'),
        tCode('V', 'HC', 'HEATH AND CARE NUMBER'),
        tCode('V', 'O', 'OTHER'),
        tCode('V', 'U', 'UNKNOWN')

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
            values (v_header_seq, v_appl_id, codes(v_idx).code, codes(v_idx).descr , 'Y', 8, 'XX', TO_DATE(v_date, 'MM/DD/YYYY'), v_user );
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
