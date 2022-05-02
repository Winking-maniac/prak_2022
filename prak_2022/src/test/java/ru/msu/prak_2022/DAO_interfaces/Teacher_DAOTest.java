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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
class Teacher_DAOTest {

    @BeforeEach
    void setUp() {
        gl_session.open();
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE teacher_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.getTransaction().commit();

        teacher_dao.save(new Teacher("admin", "Ivanov", "Ivan"));
        teacher_dao.save(new Teacher("admin", "Petrov", "Petr"));
        gl_session.close();
    }

    @Autowired
    private Teacher_DAO teacher_dao;

    @AfterEach
    void tearDown() {
        gl_session.open();
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE teacher_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.getTransaction().commit();
        gl_session.close();
    }

    @Test
    void courses() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Course>> s = teacher_dao.courses(teacher_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void companies() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Company>> s = teacher_dao.companies(teacher_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void invites() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Company>> s = teacher_dao.invites(teacher_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void lessons() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Lesson>> s = teacher_dao.lessons(teacher_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void get() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, Teacher> s = teacher_dao.get(1L);
        assertEquals(status.OK, s.getKey());
        assertEquals(1L, s.getValue().getTeacher_id());
        gl_session.close();
    }

    @Test
    void get_all() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Teacher>> s = teacher_dao.get_all();
        assertEquals(status.OK, s.getKey());
        assertEquals(2L, s.getValue().size());
        gl_session.close();
    }

    @Test
    void get_pattern() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Teacher>> s = teacher_dao.get("Ivanov");
        AbstractMap.SimpleEntry<status, Teacher> exp = teacher_dao.get(1L);
        assertEquals(1, s.getValue().size());
        assertEquals(exp.getKey(), s.getKey());
        assertEquals(exp.getValue(), s.getValue().get(0));
        gl_session.close();
    }

    @Test
    void save() {
        gl_session.open();
        status st = teacher_dao.save(new Teacher("admin", "No", "name"));
        assertEquals(status.OK, st);
        AbstractMap.SimpleEntry<status, Teacher> res = teacher_dao.get(3L);
        assertEquals(status.OK, res.getKey());
        assertEquals(3L, res.getValue().getTeacher_id());
        gl_session.close();
    }

    @Test
    void delete() {
        gl_session.open();
        status st = teacher_dao.delete(teacher_dao.get(1L).getValue());
        assertEquals(status.OK, st);
        AbstractMap.SimpleEntry<status, Teacher> res = teacher_dao.get(1L);
        assertEquals(status.NOT_FOUND, res.getKey());
        gl_session.close();
    }

    @Test
    void update() {
        gl_session.open();

        AbstractMap.SimpleEntry<status, Teacher> s = teacher_dao.get(1L);
        assertEquals(status.OK, s.getKey());

        s.getValue().setSurname("Sidorov");
        status res = teacher_dao.update(s.getValue());
        assertEquals(status.OK, res);

        gl_session.close();
    }
}