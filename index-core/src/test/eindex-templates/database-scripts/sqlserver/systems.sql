
declare @v_date  varchar(10)
    
begin

    select @v_date = convert(varchar(10), getdate(), 101)
    


    insert into SBYN_SYSTEMS (SYSTEMCODE, DESCRIPTION, STATUS, ID_LENGTH, FORMAT, INPUT_MASK, VALUE_MASK, CREATE_DATE, CREATE_USERID) 
    values ('SBYN', 'SBYN', 'A', 10, '[0-9]{10}', 'DDD-DDD-DDDD', 'DDD^DDD^DDDD', @v_date, system_user);
    


    commit
end
go
