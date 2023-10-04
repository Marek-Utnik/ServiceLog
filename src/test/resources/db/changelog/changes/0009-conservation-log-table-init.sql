CREATE TABLE conservation_log (
conservation_log_id BIGINT NOT NULL,
conservation_description VARCHAR(255) NOT NULL,
publication_date date, machine_id BIGINT NOT NULL,
system_user_id BIGINT NOT NULL,
CONSTRAINT conservation_log_pkey PRIMARY KEY (conservation_log_id)
);
ALTER TABLE conservation_log ADD CONSTRAINT FKq69kntqgxfhx7es3beeqoj5al FOREIGN KEY (system_user_id) REFERENCES person (system_user_id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE conservation_log ADD CONSTRAINT fkr6yg194x83u61ryep2awg5u7q FOREIGN KEY (machine_id) REFERENCES machine (machine_id) ON UPDATE NO ACTION ON DELETE CASCADE;
