DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
SET search_path TO public;

CREATE TABLE roles
(
    role_id serial NOT NULL,
    role_name text,

    PRIMARY KEY (role_id)
);

CREATE TABLE IF NOT EXISTS public.users
(
    user_id serial,
    passwd text,
    username text,
    foreign_id bigint,

	PRIMARY KEY (user_id),
    UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_user_id bigint NOT NULL,
    roles_role_id bigint NOT NULL,

	PRIMARY KEY (user_user_id, roles_role_id),
    FOREIGN KEY (user_user_id)
		REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (roles_role_id)
        REFERENCES public.roles (role_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE student_hub (
	student_id		serial NOT NULL,
	username		text NOT NULL,
	surname			varchar(128) NOT NULL,
	first_name		varchar(128) NOT NULL,
	last_name		varchar(128),
	description		varchar(1024),

	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,

	PRIMARY KEY(student_id),
	FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE teacher_hub (
	teacher_id		serial NOT NULL,
	username		text NOT NULL,
	surname			varchar(128) NOT NULL,
	first_name		varchar(128) NOT NULL,
	last_name		varchar(128),
	description		varchar(1024),

	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,

	PRIMARY KEY(teacher_id),
	FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE company_hub (
	company_id		serial NOT NULL,
	username		text NOT NULL,
	company_name	varchar(128) NOT NULL,
	address			varchar(1024) NOT NULL,
	description		varchar(2048),

	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,

	PRIMARY KEY(company_id),
	FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);


CREATE TABLE course_hub (
	course_id				serial NOT NULL,
	course_name				varchar(128) NOT NULL,
	date_from				date NOT NULL,
	date_till				date NOT NULL,
	lesson_intensivity		real NOT NULL CHECK (lesson_intensivity BETWEEN 0 AND 24),
	self_study_intensivity	real NOT NULL CHECK (self_study_intensivity BETWEEN 0 AND 24),
	description				varchar(2048),

	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,

	PRIMARY KEY(course_id)
);

CREATE TABLE company_teacher_link (
	company_id		int NOT NULL,
	teacher_id		int NOT NULL,
	is_approved		bool NOT NULL,

	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,

	PRIMARY KEY(company_id, teacher_id),
	FOREIGN KEY(company_id) REFERENCES company_hub(company_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(teacher_id) REFERENCES teacher_hub(teacher_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE company_course_link (
	company_id		int NOT NULL,
	course_id		int NOT NULL,
	is_author		bool NOT NULL,

	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,

	PRIMARY KEY(company_id, course_id),
	FOREIGN KEY(company_id) REFERENCES company_hub(company_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(course_id) REFERENCES course_hub(course_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE student_course_link (
	student_id		int NOT NULL,
	course_id		int NOT NULL,

	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,

	PRIMARY KEY(student_id, course_id),
	FOREIGN KEY(student_id) REFERENCES student_hub(student_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(course_id) REFERENCES course_hub(course_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE teacher_course_link (
	teacher_id		int NOT NULL,
	course_id		int NOT NULL,
	is_admin		bool NOT NULL,

	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,

	PRIMARY KEY(teacher_id, course_id),
	FOREIGN KEY(teacher_id) REFERENCES teacher_hub(teacher_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(course_id) REFERENCES course_hub(course_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE lesson_hub (
	lesson_id		serial,
	course_id		int NOT NULL,
	teacher_id		int NOT NULL,
	time_from		timestamp NOT NULL,
	time_till		timestamp NOT NULL,
	description		varchar(1024),

	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,

	PRIMARY KEY(lesson_id),
	UNIQUE(lesson_id, course_id),
	FOREIGN KEY(teacher_id) REFERENCES teacher_hub(teacher_id) ON UPDATE CASCADE ON DELETE RESTRICT,
	FOREIGN KEY(course_id) REFERENCES course_hub(course_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE student_lesson_link (
	student_id		int NOT NULL,
	course_id		int NOT NULL,
	lesson_id		int NOT NULL,
	score 			int NOT NULL CHECK (score BETWEEN 0 AND 100),

	load_timestamp	timestamp NOT NULL DEFAULT current_timestamp,

	PRIMARY KEY(student_id, course_id, lesson_id),
	FOREIGN KEY(student_id, course_id) REFERENCES student_course_link(student_id, course_id) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY(lesson_id, course_id) REFERENCES lesson_hub(lesson_id, course_id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO roles (role_id, role_name)
VALUES	(1, 'ROLE_STUDENT'),
		(2, 'ROLE_TEACHER'),
		(3, 'ROLE_COMPANY'),
		(4, 'ROLE_ADMIN'),
		(5, 'ROLE_GUEST');

INSERT INTO users (passwd, username, foreign_id)
VALUES ('$2a$12$VLkREhBFnYXgPFcnNjUOg.iKEihGS7ZywGqJ8nPy3qhhNggW261ru', 'admin', 1);

INSERT INTO users_roles VALUES (1, 4);
