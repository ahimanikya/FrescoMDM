
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

        -- ****     GENDER   ****
        tCode('L', 'GENDER', 'Gender'),
        tCode('V', '0', 'Not Known'),
        tCode('V', '1', 'Male'),
        tCode('V', '2', 'Female'),
        tCode('V', '9', 'Not Specified'),

        -- ****     LANGUAGE   ****
        tCode('L', 'LANGUAGE', 'Language'),
        tCode('V', '001' , 'Akan/Ashanti'),
        tCode('V', '002' , 'Albanian'),
        tCode('V', '003' , 'Amharic'),
        tCode('V', '004' , 'Arabic'),
        tCode('V', '005' , 'Bengali and Sylheti'),
        tCode('V', '006' , 'Brawa and Somali'),
        tCode('V', '007' , 'British Signing Language'),
        tCode('V', '008' , 'Cantonese'),
        tCode('V', '009' , 'Cantonese and Vietnamese'),
        tCode('V', '010' , 'Creole'),
        tCode('V', '011' , 'Dutch'),
        tCode('V', '012' , 'English'),
        tCode('V', '013' , 'Ethiopian'),
        tCode('V', '014' , 'Farsi/Persian'),
        tCode('V', '015' , 'Finnish'),
        tCode('V', '016' , 'Flemish'),
        tCode('V', '017' , 'French'),
        tCode('V', '018' , 'French creole'),
        tCode('V', '019' , 'Gaelic'),
        tCode('V', '020' , 'German'),
        tCode('V', '021' , 'Greek'),
        tCode('V', '022' , 'Gujarati'),
        tCode('V', '023' , 'Hakka'),
        tCode('V', '024' , 'Hausa'),
        tCode('V', '025' , 'Hebrew'),
        tCode('V', '026' , 'Hindi'),
        tCode('V', '027' , 'Igbo/Ibo'),
        tCode('V', '028' , 'Italian'),
        tCode('V', '029' , 'Japanese'),
        tCode('V', '030' , 'Korean'),
        tCode('V', '031' , 'Kurdish'),
        tCode('V', '032' , 'Lingala'),
        tCode('V', '033' , 'Luganda'),
        tCode('V', '034' , 'Makaton '),
        tCode('V', '035' , 'Malayalam'),
        tCode('V', '036' , 'Mandarin'),
        tCode('V', '037' , 'Norwegian'),
        tCode('V', '038' , 'Pashto/Pushtoo'),
        tCode('V', '039' , 'Patois'),
        tCode('V', '040' , 'Polish'),
        tCode('V', '041' , 'Portuguese'),
        tCode('V', '042' , 'Punjabi'),
        tCode('V', '043' , 'Russian'),
        tCode('V', '044' , 'Serbian/Croatian'),
        tCode('V', '045' , 'Sinhala'),
        tCode('V', '046' , 'Somali'),
        tCode('V', '048' , 'Spanish'),
        tCode('V', '049' , 'Swahili'),
        tCode('V', '050' , 'Swedish'),
        tCode('V', '051' , 'Sylheti'),
        tCode('V', '052' , 'Tagalog Filipino'),
        tCode('V', '053' , 'Tamil'),
        tCode('V', '054' , 'Thai'),
        tCode('V', '055' , 'Tigrinya'),
        tCode('V', '056' , 'Turkish'),
        tCode('V', '057' , 'Urdu'),
        tCode('V', '058' , 'Vietnamese'),
        tCode('V', '059' , 'Welsh'),
        tCode('V', '060' , 'Yoruba'),
        tCode('V', '200' , 'Other'),

        -- ****     CONTACT_METHOD   ****
        tCode('L', 'CONMETH', 'Contact Method'),
        tCode('V', 'a' , 'UK Telephone Number'),
        tCode('V', 'b' , 'Other non UK Telephone Number'),
        tCode('V', 'c' , 'UK Facsimile Number'),
        tCode('V', 'd' , 'Internet e-Mail Address'),
        tCode('V', 'e' , 'Uniform Resource Locator / URL'),
        tCode('V', 'f' , 'Pager'),

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
        tCode('V', 'Mr' , 'Mr'),
        tCode('V', 'Mrs' , 'Mrs'),
        tCode('V', 'Ms' , 'Ms'),
        tCode('V', 'Dr' , 'Dr'),
        tCode('V', 'Rev' , 'Rev'),
        tCode('V', 'Sir' , 'Sir'),
        tCode('V', 'Lady' , 'Lady'),
        tCode('V', 'Lord' , 'Lord'),
        tCode('V', 'Dame' , 'Dame'),
        tCode('V', 'OTHER', 'OTHER'),

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
        tCode('V', 'CC', 'MOBILE'),


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
        tCode('V', 'S ' , 'Single'),
        tCode('V', 'M ' , 'Married/Civil Partner'),
        tCode('V', 'D ' , 'Divorced/Civil Partner dissolved'),
        tCode('V', 'W ' , 'Widowed/Surviving Civil Partner'),
        tCode('V', 'P ' , 'Separated'),
        tCode('V', 'N ' , 'Not disclosed'),       
        
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
