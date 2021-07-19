drop table jtc_bill;
CREATE TABLE jtc_bill(
	billing_id varchar(36) primary key not null,
	customer_id varchar(36) not null,
	package_id varchar(36) not null,
	phone_used INT,
	sms_used INT,
	phone_pay INT,
	sms_pay INT,
	first boolean,
	subscript_time timestamp with time zone ,
    last_update_time  timestamp with time zone
);
drop table jtc_package;
CREATE TABLE jtc_package(
	package_id varchar(36) not null,
	name varchar(20) not null,
	phone_limit INT,
	sms_limit INT,
	extra_phone_fee decimal,
	extra_sms_fee decimal,
	subscription_fee decimal
);
delete  from jtc_package;
insert into jtc_package values('39a62e4e-07d4-4940-a60b-c44aafe00dad','Starter',10,10,1,0.5,38);
insert into jtc_package values('221b3291-5376-45a8-85a1-2dcee83c5820','Standard',30,40,1,0.5,58);
insert into jtc_package values('b85e170e-fc55-4311-bb81-88c1f92c7bb4','Premier',188,300,1,0.5,200);

drop table jtc_invoice;
CREATE TABLE jtc_invoice(
	invoice_id varchar(36) not null,
	customer_id varchar(36) not null,
	pay decimal not null,
	status VARCHAR(10)  not null,
	create_time timestamp with time zone not null,
	last_update_time timestamp with time zone not null
);