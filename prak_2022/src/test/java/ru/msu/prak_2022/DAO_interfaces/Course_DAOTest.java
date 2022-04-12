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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
class Course_DAOTest {

    @BeforeEach
    void setUp() {
        gl_session.open();
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE course_hub RESTART IDENTITY CASCADE;").executeUpdate();
//        session.createSQLQuery("INSERT INTO course_hub (course_id, course_name, date_from, date_till, lesson_intensivity, self_study_intensivity, description) VALUES (1, 'Web prak', '2022-01-01', '2022-06-01', 1.0, 1.0, '');").executeUpdate();
//        session.createSQLQuery("INSERT INTO course_hub (course_id, course_name, date_from, date_till, lesson_intensivity, self_study_intensivity, description) VALUES (2, 'Web prak', '2021-01-01', '2021-06-01', 1.0, 1.0, '');").executeUpdate();
        session.getTransaction().commit();
        gl_session.close();
        gl_session.open();
        session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(new Course(1L, "Web prak", new Date(2022, 01, 01), new Date(2022,06, 01), 1, 1, ""));
        session.save(new Course(2L, "Web prak", new Date(2021, 01, 01), new Date(2021,06, 01), 1, 1, ""));
        session.getTransaction().commit();
        gl_session.close();
    }

    @AfterEach
    void tearDown() {
        gl_session.open();
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE course_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.getTransaction().commit();
        gl_session.close();
    }

    @Autowired
    private Course_DAO course_dao;

    @Test
    void teachers() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Teacher>> s = course_dao.teachers(course_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void companies() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Company>> s = course_dao.companies(course_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void students() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Student>> s = course_dao.students(course_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void lessons() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Lesson>> s = course_dao.lessons(course_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void get() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, Course> s = course_dao.get(1L);
        assertEquals(status.OK, s.getKey());
        assertEquals(1L, s.getValue().getCourse_id());
        gl_session.close();
    }

    @Test
    void get_all() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Course>> s = course_dao.get_all();
        assertEquals(status.OK, s.getKey());
        assertEquals(2L, s.getValue().size());
        gl_session.close();
    }

    @Test
    void get_pattern() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Course>> s = course_dao.get("Web");
        assertEquals(2, s.getValue().size());
        gl_session.close();
    }

}