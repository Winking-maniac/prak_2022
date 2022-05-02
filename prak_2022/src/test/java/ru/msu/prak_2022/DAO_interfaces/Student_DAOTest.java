package ru.msu.prak_2022.DAO_interfaces;

import lombok.val;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.Course;
import ru.msu.prak_2022.models.Student;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
class Student_DAOTest {

    @Autowired
    private Student_DAO student_dao;

    @BeforeEach
    void setUp() {
        gl_session.open();
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE student_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.getTransaction().commit();

        student_dao.save(new Student(1L, "admin", "Ivanov", "Ivan"));
        student_dao.save(new Student(2L, "admin", "Petrov", "Petr"));
        gl_session.close();
    }

    @AfterEach
    void tearDown() {
        gl_session.open();
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE student_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.getTransaction().commit();

        gl_session.close();
    }

    @Test
    void courses() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Course>> s = student_dao.courses(student_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void get() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, Student> s = student_dao.get(1L);
        assertEquals(status.OK, s.getKey());
        assertEquals(1L, s.getValue().getStudent_id());
        gl_session.close();
    }

    @Test
    void get_all() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Student>> s = student_dao.get_all();
        assertEquals(status.OK, s.getKey());
        assertEquals(2L, s.getValue().size());
        gl_session.close();
    }

    @Test
    void get_pattern() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Student>> s = student_dao.get("Ivanov");
        AbstractMap.SimpleEntry<status, Student> exp = student_dao.get(1L);
        assertEquals(1, s.getValue().size());
        assertEquals(exp.getKey(), s.getKey());
        assertEquals(exp.getValue(), s.getValue().get(0));
        gl_session.close();
    }

    @Test
    void save() {
        gl_session.open();
        status st = student_dao.save(new Student(3L, "admin", "No", "name"));
        assertEquals(status.OK, st);
        AbstractMap.SimpleEntry<status, Student> res = student_dao.get(3L);
        assertEquals(status.OK, res.getKey());
        assertEquals(3L, res.getValue().getStudent_id());
        gl_session.close();
    }

    @Test
    void delete() {
        gl_session.open();
        status st = student_dao.delete(new Student(1L, "admin", "Ivanov", "Ivan"));
        assertEquals(status.OK, st);
        AbstractMap.SimpleEntry<status, Student> res = student_dao.get(1L);
        assertEquals(status.NOT_FOUND, res.getKey());
        gl_session.close();
    }

    @Test
    void update() {
        gl_session.open();

        AbstractMap.SimpleEntry<status, Student> s = student_dao.get(1L);
        assertEquals(status.OK, s.getKey());

        s.getValue().setSurname("Sidorov");
        status res = student_dao.update(s.getValue());
        assertEquals(status.OK, res);

        gl_session.close();
    }
}