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
import ru.msu.prak_2022.models.Company;
import ru.msu.prak_2022.models.Course;
import ru.msu.prak_2022.models.Teacher;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
class Company_DAOTest {

    @Autowired
    private Company_DAO company_dao;

    @BeforeEach
    void setUp() {
        gl_session.open();
        Session session = gl_session.sessionFactory.getCurrentSession();
        assert (session != null);
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE company_hub RESTART IDENTITY CASCADE;").executeUpdate();
//        session.createSQLQuery("INSERT INTO company_hub (company_id, company_name, company_address, description) VALUES (1, \"MSU\", \"Msk\", \"\"), (2, \"ISP RAS\", \"Msk\", \"\");").executeUpdate();
        session.getTransaction().commit();

        company_dao.save(new Company(1L, "admin", "MSU", "Msk", ""));
        company_dao.save(new Company(2L, "admin", "ISP RAS", "Msk", ""));
//        gl_session.close();
    }

    @AfterEach
    void tearDown() {
        gl_session.open();
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.createSQLQuery("TRUNCATE company_hub RESTART IDENTITY CASCADE;").executeUpdate();
        session.getTransaction().commit();
        gl_session.close();
    }

    @Test
    void courses() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Course>> s = company_dao.courses(company_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void teachers() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Teacher>> s = company_dao.teachers(company_dao.get(1L).getValue());
        assertEquals(status.OK, s.getKey());
        assertEquals(0, s.getValue().size());
        gl_session.close();
    }

    @Test
    void get() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, Company> s = company_dao.get(1L);
        assertEquals(status.OK, s.getKey());
        assertEquals(1L, s.getValue().getCompany_id());
        gl_session.close();
    }

    @Test
    void get_all() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Company>> s = company_dao.get_all();
        assertEquals(status.OK, s.getKey());
        assertEquals(2L, s.getValue().size());
        gl_session.close();
    }

    @Test
    void get_pattern() {
        gl_session.open();
        AbstractMap.SimpleEntry<status, List<Company>> s = company_dao.get("ISP");
        AbstractMap.SimpleEntry<status, Company> exp = company_dao.get(2L);
        assertEquals(1, s.getValue().size());
        assertEquals(exp.getKey(), s.getKey());
        assertEquals(exp.getValue(), s.getValue().get(0));
        gl_session.close();
    }

    @Test
    void save() {
        gl_session.open();
        status st = company_dao.save(new Company(3L, "admin", "No", "name", ""));
        assertEquals(status.OK, st);
        AbstractMap.SimpleEntry<status, Company> res = company_dao.get(3L);
        assertEquals(status.OK, res.getKey());
        assertEquals(3L, res.getValue().getCompany_id());
        gl_session.close();
    }

    @Test
    void delete() {
        gl_session.open();
        status st = company_dao.delete(company_dao.get(1L).getValue());
        assertEquals(status.OK, st);
        AbstractMap.SimpleEntry<status, Company> res = company_dao.get(1L);
        assertEquals(status.NOT_FOUND, res.getKey());
        gl_session.close();
    }

    @Test
    void update() {
        gl_session.open();

        AbstractMap.SimpleEntry<status, Company> s = company_dao.get(1L);
        assertEquals(status.OK, s.getKey());

        s.getValue().setCompany_name("Sidorov");
        status res = company_dao.update(s.getValue());
        assertEquals(status.OK, res);

        gl_session.close();
    }
}