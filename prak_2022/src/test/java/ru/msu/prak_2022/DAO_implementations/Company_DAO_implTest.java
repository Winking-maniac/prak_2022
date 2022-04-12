package ru.msu.prak_2022.DAO_implementations;

import lombok.val;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.msu.prak_2022.models.Company;
import ru.msu.prak_2022.models.Course;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
class Company_DAO_implTest {

    @Autowired
    private Company_DAO_impl company_dao;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE company_hub RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
        company_dao.save(new Company(1L, "First company", "Baker street, 221b", null));
        company_dao.save(new Company(2L, "Second company", "Wall street, 11", null));
    }

    @AfterEach
    void tearDown() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createSQLQuery("TRUNCATE company_hub RESTART IDENTITY CASCADE;").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void get() {
        AbstractMap.SimpleEntry<status, Company> s = company_dao.get(1L);
        assertEquals(status.OK, s.getKey());
        assertEquals(new Company(1L, "First company", "Baker street, 221b", null), s.getValue());
    }

    @Test
    void get_all() {
    }

    @Test
    void courses() {
    }

    @Test
    void teachers() {
    }
}