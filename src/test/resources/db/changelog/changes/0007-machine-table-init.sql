CREATE TABLE machine (machine_id BIGINT NOT NULL, machine_type VARCHAR(255), producer_name VARCHAR(255), registration_number INTEGER NOT NULL, serial_number INTEGER NOT NULL, company_id BIGINT, CONSTRAINT machine_pkey PRIMARY KEY (machine_id));
ALTER TABLE machine ADD CONSTRAINT fkt3hmithfsi7986tmskth1hi1j FOREIGN KEY (company_id) REFERENCES company (company_id) ON UPDATE NO ACTION ON DELETE CASCADE;
