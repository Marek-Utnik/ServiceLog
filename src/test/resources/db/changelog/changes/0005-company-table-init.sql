CREATE TABLE company (
	company_id BIGINT NOT NULL, 
	company_address VARCHAR(255), 
	company_name VARCHAR(255), 
	CONSTRAINT company_pkey PRIMARY KEY (company_id)
	);
CREATE SEQUENCE  IF NOT EXISTS company_seq AS bigint START WITH 1 INCREMENT BY 50 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;
