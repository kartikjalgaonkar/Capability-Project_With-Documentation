DROP TABLE IF EXISTS feedback_schema.feedback;
DROP SCHEMA IF EXISTS feedback_schema;
DROP SEQUENCE IF EXISTS feedback_id_seq;
CREATE SEQUENCE feedback_id_seq START 1;
CREATE SCHEMA feedback_schema AUTHORIZATION root;
CREATE TABLE feedback_schema.feedback(id integer DEFAULT nextval('feedback_id_seq'), source varchar(255), rating integer, comment varchar(255), username varchar(255), created_time date,  PRIMARY KEY (id));
