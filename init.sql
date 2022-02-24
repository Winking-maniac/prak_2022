DROP SCHEMA IF EXISTS course_platform CASCADE;
CREATE SCHEMA course_platform;
SET search_path TO course_platform;

CREATE OR REPLACE FUNCTION course_platform.auto_source(
	)
    RETURNS varchar(128)
    LANGUAGE 'plpgsql'
    COST 100
    STABLE PARALLEL SAFE
AS $BODY$
BEGIN
  RETURN current_setting('course_platform.default_load_source');
END;
$BODY$;

CREATE TABLE sources_dict (
	load_source		varchar(128) NOT NULL,
	
	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,
	
	PRIMARY KEY(load_source)
);

CREATE TABLE student_hub (
	student_id		serial NOT NULL,
	surname			varchar(128) NOT NULL,
	first_name		varchar(128) NOT NULL,
	last_name		varchar(128),
	description		varchar(1024), 
	
	load_source		varchar(128) NOT NULL DEFAULT auto_source(),
	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,
	
	PRIMARY KEY(student_id),
	FOREIGN KEY(load_source) REFERENCES sources_dict(load_source) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE teacher_hub (
	teacher_id		serial NOT NULL,
	surname			varchar(128) NOT NULL,
	first_name		varchar(128) NOT NULL,
	last_name		varchar(128),
	description		varchar(1024), 
	
	load_source		varchar(128) NOT NULL DEFAULT auto_source(),
	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,
	
	PRIMARY KEY(teacher_id),
	FOREIGN KEY(load_source) REFERENCES sources_dict(load_source) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE company_hub (
	company_id		serial NOT NULL,
	company_name	varchar(128) NOT NULL,
	address			varchar(1024) NOT NULL,
	description		varchar(2048),
	
	load_source		varchar(128) NOT NULL DEFAULT auto_source(),
	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,
	
	PRIMARY KEY(company_id),
	FOREIGN KEY(load_source) REFERENCES sources_dict(load_source) ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE course_hub (
	course_id				serial NOT NULL,
	course_name				varchar(128) NOT NULL,
	date_from				date NOT NULL,
	date_till				date NOT NULL,
	lesson_intensivity		real NOT NULL CHECK lesson_intensivity BETWEEN 0 AND 24,
	self_study_intensivity	real NOT NULL CHECK lesson_intensivity BETWEEN 0 AND 24,
	description				varchar(2048), 
	
	load_source		varchar(128) NOT NULL DEFAULT auto_source(),
	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,
	
	PRIMARY KEY(teacher_id),
	FOREIGN KEY(load_source) REFERENCES sources_dict(load_source) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE company_teacher_link (
	company_id		int NOT NULL,
	teacher_id		int NOT NULL,
	is_approved		bool NOT NULL,
	
	load_source		varchar(128) NOT NULL DEFAULT auto_source(),
	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,
	
	PRIMARY KEY(company_id, teacher_id),
	FOREIGN KEY(company_id) REFERENCES company_hub(company_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(teacher_id) REFERENCES teacher_hub(teacher_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(load_source) REFERENCES sources_dict(load_source) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE company_course_link (
	company_id		int NOT NULL,
	course_id		int NOT NULL,
	is_author		bool NOT NULL,
	
	load_source		varchar(128) NOT NULL DEFAULT auto_source(),
	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,
	
	PRIMARY KEY(company_id, course_id),
	FOREIGN KEY(company_id) REFERENCES company_hub(company_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(course_id) REFERENCES course_hub(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(load_source) REFERENCES sources_dict(load_source) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE student_course_link (
	student_id		int NOT NULL,
	course_id		int NOT NULL,
	
	load_source		varchar(128) NOT NULL DEFAULT auto_source(),
	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,
	
	PRIMARY KEY(student_id, course_id),
	FOREIGN KEY(student_id) REFERENCES student_hub(student_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(course_id) REFERENCES course_hub(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(load_source) REFERENCES sources_dict(load_source) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE teacher_course_link (
	teacher_id		int NOT NULL,
	course_id		int NOT NULL,
	is_admin		bool NOT NULL,
	
	load_source		varchar(128) NOT NULL DEFAULT auto_source(),
	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,
	
	PRIMARY KEY(teacher_id, course_id),
	FOREIGN KEY(teacher_id) REFERENCES teacher_hub(teacher_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(course_id) REFERENCES course_hub(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(load_source) REFERENCES sources_dict(load_source) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE lesson_hub (
	lesson_id		int NOT NULL,
	course_id		int NOT NULL,
	teacher_id		int NOT NULL,	
	time_from		timestamp NOT NULL,
	time_till		timestamp NOT NULL,
	description		varchar(1024), 
	
	load_source		varchar(128) NOT NULL DEFAULT auto_source(),
	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,
	
	PRIMARY KEY(lesson_id, course_id),
	FOREIGN KEY(teacher_id) REFERENCES teacher_hub(teacher_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(course_id) REFERENCES course_hub(course_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(load_source) REFERENCES sources_dict(load_source) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE student_lesson_link (
	student_id		int NOT NULL,
	course_id		int NOT NULL,
	lesson_id		int NOT NULL,
	score 			int NOT NULL CHECK score BETWEEN 0 AND 100,
	
	load_source		varchar(128) NOT NULL DEFAULT auto_source(),
	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,
	
	PRIMARY KEY(student_id, course_id, lesson_id),
	FOREIGN KEY(student_id, course_id) REFERENCES student_course_link(student_id, course_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(lesson_id, course_id) REFERENCES lesson_hub(lesson_id, course_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(load_source) REFERENCES sources_dict(load_source) ON UPDATE CASCADE ON DELETE RESTRICT
);



