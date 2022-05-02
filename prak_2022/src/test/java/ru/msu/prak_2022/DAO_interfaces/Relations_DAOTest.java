package ru.msu.prak_2022.DAO_interfaces;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
class Relations_DAOTest {

    @Autowired
    private Student_DAO student_dao;

    @Autowired
    private Teacher_DAO teacher_dao;

    @Autowired
    private Company_DAO company_dao;

    @Autowired
    private Course_DAO course_dao;

    @Autowired
    private Lesson_DAO lesson_dao;

    @Autowired
    private Relations_DAO relations_dao;

    @BeforeEach
    void setUp() {
        tearDown();

        gl_session.open();
        student_dao.save(new Student(1L, "admin", "Ivanov", "Ivan"));
        student_dao.save(new Student(2L, "admin", "Petrov", "Petr"));
        gl_session.close();

        gl_session.open();
        teacher_dao.save(new Teacher("admin", "Kuznetsov", "Ivan"));
        teacher_dao.save(new Teacher("admin", "Popov", "Petr"));
        gl_session.close();

        gl_session.open();
        company_dao.save(new Company(1L, "admin", "MSU", "Msk", ""));
        company_dao.save(new Company(2L, "admin", "ISP RAS", "Msk", ""));
        gl_session.close();
    }

    @AfterEach
    void tearDown() {
        gl_session.open();
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE student_lesson_link RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE teacher_course_link RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE student_course_link RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE company_course_link RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE company_teacher_link RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE lesson_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE course_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE teacher_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE student_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.createSQLQuery("TRUNCATE company_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.getTransaction().commit();
        gl_session.close();
    }

    @Test
    void enrollment() {
        Student student1 = student_dao.get(1L).getValue();
        Student student2 = student_dao.get(2L).getValue();

        Company company = company_dao.get(1L).getValue();
        relations_dao.create_course(company, new Course(1L, "Web prak", new Date(2022, 01, 01), new Date(2022,06, 01), 1, 1, ""));

        Course course = course_dao.get(1L).getValue();

        // Test
        AbstractMap.SimpleEntry<status, Boolean> res = relations_dao.is_student(student1, course);
        assertEquals(status.OK, res.getKey());
        assertEquals(FALSE, res.getValue());

        status res1 = relations_dao.enroll(student1, course);
        assertEquals(status.OK, res1);

        res = relations_dao.is_student(student1, course);
        assertEquals(status.OK, res.getKey());
        assertEquals(TRUE, res.getValue());

        res = relations_dao.is_student(student2, course);
        assertEquals(status.OK, res.getKey());
        assertEquals(FALSE, res.getValue());

        res1 = relations_dao.unenroll(student1, course);
        assertEquals(status.OK, res1);

        res = relations_dao.is_student(student1, course);
        assertEquals(status.OK, res.getKey());
        assertEquals(FALSE, res.getValue());

        res1 = relations_dao.unenroll(student2, course);
        assertEquals(status.RELATION_NOT_FOUND, res1);
    }

    @Test
    void invite() {
        gl_session.open();
        Teacher teacher1 = teacher_dao.get(1L).getValue();
        Teacher teacher2 = teacher_dao.get(2L).getValue();

        Company company = company_dao.get(1L).getValue();

        List<Company> res = teacher_dao.invites(teacher1).getValue();
        assertEquals(0, res.size());

        status res1 = relations_dao.invite(teacher1, company);
        assertEquals(status.OK, res1);

        res = teacher_dao.invites(teacher1).getValue();
        assertEquals(1, res.size());
        assertEquals(company, res.get(0));

        relations_dao.invite(teacher2, company);

        res1 = relations_dao.approve(teacher1, company);
        assertEquals(status.OK, res1);
        res1 = relations_dao.reject(teacher2, company);
        assertEquals(status.OK, res1);

        res = teacher_dao.invites(teacher1).getValue();
        assertEquals(0, res.size());

        res = teacher_dao.invites(teacher2).getValue();
        assertEquals(0, res.size());

        res = teacher_dao.companies(teacher1).getValue();
        assertEquals(1, res.size());
        assertEquals(company, res.get(0));

        res = teacher_dao.invites(teacher2).getValue();
        assertEquals(0, res.size());

        res1 = relations_dao.invite(teacher1, company);
        assertEquals(status.ALREADY_MEMBER, res1);

        AbstractMap.SimpleEntry<status, Boolean> res2 = relations_dao.is_member(teacher1, company);
        assertEquals(status.OK, res2.getKey());
        assertEquals(TRUE, res2.getValue());

        res2 = relations_dao.is_member(teacher2, company);
        assertEquals(status.OK, res2.getKey());
        assertEquals(FALSE, res2.getValue());

        res1 = relations_dao.fire(teacher1, company);
        assertEquals(status.OK, res1);
        res1 = relations_dao.fire(teacher2, company);

        assertEquals(status.RELATION_NOT_FOUND, res1);

        relations_dao.invite(teacher1, company);
        relations_dao.approve(teacher1, company);

        res1 = relations_dao.retire(teacher1, company);
        assertEquals(status.OK, res1);

        gl_session.close();
    }

    @Test
    void create_course() {
        gl_session.open();

        Company company = company_dao.get(1L).getValue();
        Company company2 = company_dao.get(2L).getValue();

        status res = relations_dao.create_course(company, new Course(1L, "Old", new Date(2022, 01, 01), new Date(2022, 06, 01), 1, 1, ""));
        assertEquals(status.OK, res);

        Course course = course_dao.get(1L).getValue();
        AbstractMap.SimpleEntry<status, Boolean> res1 = relations_dao.is_author(company, course);
        assertEquals(status.OK, res1.getKey());
        assertEquals(TRUE, res1.getValue());

        res1 = relations_dao.is_author(company2, course);
        assertEquals(status.RELATION_NOT_FOUND, res1.getKey());

        course.setCourse_name("New");
        res = relations_dao.update_course(company, course);
        assertEquals(status.OK, res);

        course = course_dao.get(1L).getValue();
        assertEquals("New", course.getCourse_name());

        res = relations_dao.delete_course(company, course);
        assertEquals(status.OK, res);

        res = course_dao.get(1L).getKey();
        assertEquals(status.NOT_FOUND, res);

        gl_session.close();
    }


    @Test
    void grant_admin() {
        gl_session.open();
        Teacher teacher1 = teacher_dao.get(1L).getValue();
        Teacher teacher2 = teacher_dao.get(2L).getValue();

        Company company = company_dao.get(1L).getValue();
        Company other_company = company_dao.get(2L).getValue();

        relations_dao.create_course(company, new Course(1L, "Old", new Date(2022, 01, 01), new Date(2022, 06, 01), 1, 1, ""));
        relations_dao.create_course(other_company, new Course(2L, "New", new Date(2022, 01, 01), new Date(2022, 06, 01), 1, 1, ""));

        Course course1 = course_dao.get(1L).getValue();
        Course course2 = course_dao.get(2L).getValue();

        relations_dao.invite(teacher1, company);
        relations_dao.approve(teacher1, company);

        // Test
        status res = relations_dao.is_admin(teacher1, course1).getKey();
        assertEquals(status.RELATION_NOT_FOUND, res);

        res = relations_dao.add_teacher(company, course1, teacher1);
        assertEquals(status.OK, res);

        AbstractMap.SimpleEntry<status, Boolean> res1 = relations_dao.is_admin(teacher1, course1);
        assertEquals(status.OK, res1.getKey());
        assertEquals(FALSE, res1.getValue());

        res = relations_dao.grant_admin(company, course1, teacher1);
        assertEquals(status.OK, res);

        res1 = relations_dao.is_admin(teacher1, course1);
        assertEquals(status.OK, res1.getKey());
        assertEquals(TRUE, res1.getValue());

        res = relations_dao.revoke_admin(company, course1, teacher1);
        assertEquals(status.OK, res);

        res1 = relations_dao.is_admin(teacher1, course1);
        assertEquals(status.OK, res1.getKey());
        assertEquals(FALSE, res1.getValue());

        res = relations_dao.delete_teacher(company, course1, teacher1);
        assertEquals(status.OK, res);

        res1 = relations_dao.is_admin(teacher1, course1);
        assertEquals(status.RELATION_NOT_FOUND, res1.getKey());

        res = relations_dao.grant_admin(company, course1, teacher2);
        assertEquals(status.FORBIDDEN, res);

        res = relations_dao.revoke_admin(company, course1, teacher2);
        assertEquals(status.FORBIDDEN, res);

        res = relations_dao.revoke_admin(company, course2, teacher1);
        assertEquals(status.FORBIDDEN, res);

    }


    @Test
    void create_lesson() {
        gl_session.open();
        Teacher teacher1 = teacher_dao.get(1L).getValue();
        Teacher teacher2 = teacher_dao.get(2L).getValue();

        Company company = company_dao.get(1L).getValue();

        relations_dao.create_course(company, new Course(1L, "Old", new Date(2022, 01, 01), new Date(2022, 06, 01), 1, 1, ""));

        Course course = course_dao.get(1L).getValue();

        relations_dao.invite(teacher1, company);
        relations_dao.invite(teacher2, company);

        relations_dao.approve(teacher1, company);
        relations_dao.approve(teacher2, company);

        relations_dao.add_teacher(company, course, teacher1);
        relations_dao.add_teacher(company, course, teacher2);

        relations_dao.grant_admin(company, course, teacher1);

        // Test

        status res = relations_dao.create_lesson(teacher1,
                                                 course,
                                                 new Timestamp(2020, 1, 1, 10, 0, 0, 0),
                                                 new Timestamp(2020, 1, 1, 11, 0, 0, 0),
                                                 "");
        assertEquals(status.OK, res);

        res = relations_dao.create_lesson(teacher2,
                course,
                new Timestamp(2020, 1, 1, 10, 0, 0, 0),
                new Timestamp(2020, 1, 1, 11, 0, 0, 0),
                "");
        assertEquals(status.FORBIDDEN, res);

        Lesson lesson = lesson_dao.get(1L).getValue();
        lesson.setDescription("haha");
        res = relations_dao.update_lesson(teacher1,
                course,
                lesson);
        assertEquals(status.OK, res);

        lesson = lesson_dao.get(1L).getValue();
        assertEquals("haha", lesson.getDescription());

        Student student = student_dao.get(1L).getValue();
        relations_dao.enroll(student, course);

        res = relations_dao.rate(teacher1, lesson, student, 50L);
        assertEquals(status.OK, res);
//        relations_dao.rate(teacher2, lesson, student, 50L);
//        assertEquals(status.FORBIDDEN, res);

        res = relations_dao.delete_lesson(teacher1, course, 1L);
        assertEquals(status.OK, res);

    }

}