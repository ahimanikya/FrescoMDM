-- Sample: uncomment to use

insert into sbyn_user_code (code_list, code, descr, format, input_mask, value_mask)
   values ('AUXIDDEF', 'CCID', 'CREDIT CARD ID', '[0-9]{16}', 'DDDD DDDD DDDD DDDD', 'DDDD^DDDD^DDDD^DDDD');

insert into sbyn_user_code (code_list, code, descr, format, input_mask, value_mask)
   values ('AUXIDDEF', 'MEDID', 'Medical License ID', '[A-Z]{3}[0-9]{6}', 'LLL-DDDDDD', 'LLL^DDDDDD');

insert into sbyn_user_code (code_list, code, descr, format, input_mask, value_mask)
   values ('AUXIDDEF', 'HHID', 'Household ID', '[A-Z0-9]{3}[0-9]{4}', 'AAADDDD', 'AAADDDD');

commit;
