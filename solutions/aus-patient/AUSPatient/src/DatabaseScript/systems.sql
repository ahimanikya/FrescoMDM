
declare 
    v_user  varchar2(20);
    v_date  varchar2(10);
begin
    select user into v_user from dual;
    select TO_CHAR(sysdate, 'MM/DD/YYYY') into v_date from dual;


    insert into sbyn_systems (systemcode, description, status, id_length, format, input_mask, value_mask, create_date, create_userid) 
    values ('SUN', 'SUN', 'A', 10, '[0-9]{10}', 'DDD-DDD-DDDD', 'DDD^DDD^DDDD', TO_DATE(v_date, 'MM/DD/YYYY'), v_user);

end;
/
