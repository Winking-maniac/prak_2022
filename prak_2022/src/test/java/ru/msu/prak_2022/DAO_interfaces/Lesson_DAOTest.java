package ru.msu.prak_2022.DAO_interfaces;

import lombok.val;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
class Lesson_DAOTest {

    @Autowired
    private Course_DAO course_dao;
    @Autowired
    private Lesson_DAO lesson_dao;
    @Autowired
    private Teacher_DAO teacher_dao;

    @BeforeEach
    void setUp() throws Exception {

        gl_session.open();
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE lesson_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE teacher_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE course_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.getTransaction().commit();
        teacher_dao.save(new Teacher("admin", "Ivanov", "Ivan"));
        session.close();

        gl_session.open();
        session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(new Course(1L, "Web prak", new Date(2022, 01, 01), new Date(2022, 03, 01), 1, 1, ""));
        session.getTransaction().commit();
        session.close();

        Teacher teacher = teacher_dao.get(1L).getValue();
        Course course = course_dao.get(1L).getValue();

        gl_session.open();
        session=  gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(new Lesson(1L, course, teacher));
        session.save(new Lesson(2L, course, teacher));
        session.getTransaction().commit();
        gl_session.close();
    }

    @AfterEach
    void tearDown() {
        gl_session.open();
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE course_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE teacher_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE lesson_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.getTransaction().commit();
        gl_session.close();
    }

    @Test
    void get_course() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, Course> res = lesson_dao.get_course(lesson_dao.get(1L).getValue());
        AbstractMap.SimpleEntry<status, Course> exp = course_dao.get(1L);
        assertEquals(exp, res);
        gl_session.close();
    }

    @Test
    void get_teacher() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, Teacher> res = lesson_dao.get_teacher(lesson_dao.get(1L).getValue());
        assertEquals(teacher_dao.get(1L), res);
        gl_session.close();
    }

    @Test
    void scores() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, Map<Long, Long>> res = lesson_dao.scores(lesson_dao.get(1L).getValue());
        assertEquals(status.OK, res.getKey());
        assertEquals(0, res.getValue().size());
        gl_session.close();
    }

    @Test
    void get() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, Lesson> s = lesson_dao.get(1L);
        assertEquals(status.OK, s.getKey());
        assertEquals(1L, s.getValue().getLesson_id());

        s = lesson_dao.get(4L);
        assertEquals(status.NOT_FOUND, s.getKey());

        gl_session.close();
    }
}