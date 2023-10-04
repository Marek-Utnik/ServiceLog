CREATE TABLE person (
	system_user_id BIGINT NOT NULL,
	name VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	surname VARCHAR(255) NOT NULL,
	username VARCHAR(255) NOT NULL,
	CONSTRAINT system_user_pkey PRIMARY KEY (system_user_id));
ALTER TABLE person ADD CONSTRAINT uk_74y7xiqrvp39wycn0ron4xq4h UNIQUE (username);
