-- V1__Create_task_table.sql
CREATE TYPE task_status AS ENUM ('TO_DO', 'IN_PROGRESS', 'DONE');

CREATE SEQUENCE task_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE task (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR(30) NOT NULL,
  description VARCHAR(300) NOT NULL,
  status task_status NOT NULL,

  CONSTRAINT check_title_not_empty CHECK (LENGTH(TRIM(title)) > 0),
  CONSTRAINT check_title_length CHECK (LENGTH(TRIM(title)) BETWEEN 5 AND 30),
  CONSTRAINT check_description_not_empty CHECK (LENGTH(TRIM(description)) > 0),
  CONSTRAINT check_description_length CHECK (LENGTH(TRIM(description)) BETWEEN 5 AND 300)
);
