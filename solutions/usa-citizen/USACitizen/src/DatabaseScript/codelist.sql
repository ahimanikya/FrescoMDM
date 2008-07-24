
create or replace type tCode as object (
  type varchar2(1),
  code  varchar2(8),
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

        -- ****     SUFFIX   ****
        tCode('L', 'SUFFIX', 'module description'),
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
        tCode('L', 'TITLE', 'module description'),
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
        tCode('L', 'GENDER', 'module description'),
        tCode('V', 'M', 'MALE'),
        tCode('V', 'F', 'FEMALE'),
        tCode('V', 'O', 'OTHER'),
        tCode('V', 'U', 'UNKNOWN'),

        -- ****     MSTATUS   ****
        tCode('L', 'MSTATUS', 'module description'),
        tCode('V', 'D', 'DIVORCED'),
        tCode('V', 'L', 'LEGALLY SEPARATED'),
        tCode('V', 'M', 'MARRIED'),
        tCode('V', 'P', 'PARTNER'),
        tCode('V', 'S', 'SINGLE'),
        tCode('V', 'U', 'UNKNOWN'),
        tCode('V', 'W', 'WIDOWED'),

        -- ****     RACE   ****
        tCode('L', 'RACE', 'module description'),
        tCode('V', 'I', 'AMERICAN INDIAN'),
        tCode('V', 'A', 'ASIAN'),
        tCode('V', 'B', 'BLACK'),
        tCode('V', 'H', 'HISPANIC'),
        tCode('V', 'O', 'OTHER'),
        tCode('V', 'R', 'REFUSED'),
        tCode('V', 'U', 'UNKNOWN'),
        tCode('V', 'W', 'WHITE'),

        -- ****     ETHNIC   ****
        tCode('L', 'ETHNIC', 'module description'),
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
        tCode('L', 'RELIGION', 'module description'),
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
        tCode('L', 'LANGUAGE', 'module description'),
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

        -- ****     HAIRCOLR   ****
        tCode('L', 'HAIRCOLR', 'module description'),
        tCode('V', 'BLACK', 'BLACK'),
        tCode('V', 'BLONDE', 'BLONDE'),
        tCode('V', 'BROWN', 'BROWN'),
        tCode('V', 'RED', 'RED'),
        tCode('V', 'GRAY', 'GRAY'),

        -- ****     EYECOLOR   ****
        tCode('L', 'EYECOLOR', 'module description'),
        tCode('V', 'AMBER', 'AMBER'),
        tCode('V', 'BLACK', 'BLACK'),
        tCode('V', 'BLUE', 'BLUE'),
        tCode('V', 'BROWN', 'BROWN'),
        tCode('V', 'GRAY', 'GRAY'),
        tCode('V', 'GREEN', 'GREEN'),
        tCode('V', 'HAZEL', 'HAZEL'),

        -- ****     ADDRTYPE   ****
        tCode('L', 'ADDRTYPE', 'module description'),
        tCode('V', 'O', 'OFFICE'),
        tCode('V', 'H', 'HOME'),
        tCode('V', 'V', 'VACATION'),
        tCode('V', 'H2','HOME 2'),
        tCode('V', 'M', 'MAILING'),
        tCode('V', 'W', 'WORK'),
        tCode('V', 'W2', 'WORK 2'),
        tCode('V', 'T', 'TEMPORARY'),

        -- ****     PHONTYPE   ****
        tCode('L', 'PHONTYPE', 'module description'),
        tCode('V', 'CH', 'HOME'),
        tCode('V', 'CO', 'OFFICE'),
        tCode('V', 'CF', 'FAX'),
        tCode('V', 'CB', 'BUSINESS'),
        tCode('V', 'CBD', 'BUSINESS DIRECT'),
        tCode('V', 'CBA', 'BUSINESS ALTERNATE'),
        tCode('V', 'CP', 'PAGER'),
        tCode('V', 'CC', 'CELLULAR'),

        -- ****     MEMRTYPE   ****
        tCode('L', 'MEMRTYPE', 'module description'),
        tCode('V', 'FA', 'FATHER'),
        tCode('V', 'MO', 'MOTHER'),

        -- ****     IDTYPE   ****
        tCode('L', 'IDTYPE', 'module description'),
        tCode('V', 'DR', 'DRIVER LICENSE'),
        tCode('V', 'ST', 'STATE ID'),
        tCode('V', 'FD', 'FEDERAL ID'),

        -- ****     YESNO   ****
        tCode('L', 'YESNO', 'module description'),
        tCode('V', 'Y', 'YES'),
        tCode('V', 'N', 'NO'),

        -- ****     STATECD   ****
        tCode('L', 'STATECD', 'module description'),
        tCode('V', 'AL','ALABAMA'),
        tCode('V', 'AK','ALASKA'),
        tCode('V', 'AZ','ARIZONA'),
        tCode('V', 'AR','ARKANSAS'),
        tCode('V', 'CA','CALIFORNIA'),
        tCode('V', 'CO','COLORADO'),
        tCode('V', 'CT','CONNECTICUT'),
        tCode('V', 'DE','DELAWARE'),
        tCode('V', 'DC','DISTRICT OF COLUMBIA'),
        tCode('V', 'FL','FLORIDA'),
        tCode('V', 'GA','GEORGIA'),
        tCode('V', 'HI','HAWAII'),
        tCode('V', 'ID','IDAHO'),
        tCode('V', 'IL','ILLINOIS'),
        tCode('V', 'IN','INDIANA'),
        tCode('V', 'IA','IOWA'),
        tCode('V', 'KS','KANSAS'),
        tCode('V', 'KY','KENTUCKY'),
        tCode('V', 'LA','LOUISIANA'),
        tCode('V', 'ME','MAINE'),
        tCode('V', 'MD','MARYLAND'),
        tCode('V', 'MA','MASSACHUSETTS'),
        tCode('V', 'MI','MICHIGAN'),
        tCode('V', 'MN','MINNESOTA'),
        tCode('V', 'MS','MISSISSIPPI'),
        tCode('V', 'MO','MISSOURI'),
        tCode('V', 'MT','MONTANA'),
        tCode('V', 'NE','NEBRASKA'),
        tCode('V', 'NV','NEVADA'),
        tCode('V', 'NH','NEW HAMPSHIRE'),
        tCode('V', 'NJ','NEW JERSEY'),
        tCode('V', 'NM','NEW MEXICO'),
        tCode('V', 'NY','NEW YORK'),
        tCode('V', 'NC','NORTH CAROLINA'),
        tCode('V', 'ND','NORTH DAKOTA'),
        tCode('V', 'OH','OHIA'),
        tCode('V', 'OK','OKLAHOMA'),
        tCode('V', 'OR','OREGON'),
        tCode('V', 'PA','PENNSYLVANIA'),
        tCode('V', 'PR','PUERTO RICO'),
        tCode('V', 'RI','RHODE ISLAND'),
        tCode('V', 'SC','SOUTH CAROLINA'),
        tCode('V', 'SD','SOUTH DAKOTA'),
        tCode('V', 'TN','TENNESSEE'),
        tCode('V', 'TX','TEXAS'),
        tCode('V', 'UT','UTAH'),
        tCode('V', 'VT','VERMONT'),
        tCode('V', 'VI','VIRGIN ISLANDS'),
        tCode('V', 'VA','VIRGINIA'),
        tCode('V', 'WA','WASHINGTON'),
        tCode('V', 'WI','WISCONSIN'),
        tCode('V', 'WV','WEST VIRGINIA'),
        tCode('V', 'WY','WYOMING'),

        -- ****     CONTTYPE   ****
        tCode('L', 'CONTTYPE', 'module description'),
        tCode('V', 'EMER', 'EMERGENCY'),
        tCode('V', 'GUAR', 'GUARDIAN')
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
