CREATE SEQUENCE feedback_id_seq;
CREATE TABLE feedback_schema.feedback(id integer DEFAULT nextval('feedback_id_seq'), source varchar(255), rating integer, comment varchar(255), username varchar(255), created_time date,  PRIMARY KEY (id));
