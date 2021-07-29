drop table IF EXISTS jtc_bill;
CREATE TABLE IF NOT EXISTS jtc_bill(
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
drop table IF EXISTS jtc_package;
CREATE TABLE IF NOT EXISTS jtc_package(
	package_id varchar(36) not null,
	name varchar(20) not null,
	phone_limit INT,
	sms_limit INT,
	extra_phone_fee decimal,
	extra_sms_fee decimal,
	subscription_fee decimal
);

drop table IF EXISTS jtc_invoice;
CREATE TABLE IF NOT EXISTS jtc_invoice(
	invoice_id varchar(36) not null,
	customer_id varchar(36) not null,
	pay decimal not null,
	status VARCHAR(10)  not null,
	create_time timestamp with time zone not null,
	last_update_time timestamp with time zone not null
);

drop table IF EXISTS jtc_usage;
CREATE TABLE IF NOT EXISTS jtc_usage(
	usage_details_id varchar(36) not null,
	customer_id varchar(36) not null,
	usage NUMERIC not null,
	type VARCHAR(10)  not null,
	in_curred_at timestamp ,
	create_at timestamp
);