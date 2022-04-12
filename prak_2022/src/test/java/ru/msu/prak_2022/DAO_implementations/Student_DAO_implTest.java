package ru.msu.prak_2022.DAO_implementations;

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
class Student_DAO_implTest {

    @Autowired
    private Student_DAO_impl student_dao;

//    @Autowired
//    private SessionFactory sessionFactory;

//    @BeforeAll
//    void before_all() {
//
//    }
    @BeforeEach
    void before() {
        gl_session.open();
        try (Session session = gl_session.sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE student_hub RESTART IDENTITY CASCADE;").executeUpdate();
//            session.createSQLQuery("ALTER SEQUENCE person_person_id_seq RESTART WITH 1;").executeUpdate();
            session.getTransaction().commit();
        }
        student_dao.save(new Student(1L, "Ivanov", "Ivan"));
        student_dao.save(new Student(2L, "Petrov", "Petr"));
        gl_session.close();
    }

    @AfterEach
    void after() {
        student_dao.delete(new Student(1L, "Ivanov", "Ivan"));
        student_dao.delete(new Student(2L, "Petrov", "Petr"));
    }

    @Test
    void get() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, Student> s = student_dao.get(1L);
        assertEquals(status.OK, s.getKey());
        assertEquals(new Student(1L, "Ivanov", "Ivan"), s.getValue());
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
    void get_all() {
    }

    @Test
    void courses() {
//        student_dao.save(new Student(1L, "Ivanov", "Ivan"));
//        try(Session session = gl_session.sessionFactory.openSession()) {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Course>> s = student_dao.courses(student_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }
}