package ru.msu.prak_2022.DAO_implementations;

import org.hibernate.Criteria;
import org.hibernate.Session;
import ru.msu.prak_2022.DAO_interfaces.Default_DAO;
import ru.msu.prak_2022.DAO_interfaces.simple_hub_DAO;
import ru.msu.prak_2022.models.Student;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

public class Student_DAO_impl extends Default_DAO_impl<Student, Long> implements Default_DAO<Student, Long>, simple_hub_DAO<Student> {

    public Student_DAO_impl() {
        super(Student.class);
    }

    @Override
    public Collection<Student> get(String pattern) {
        try (Session session = factory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Student> query = cb.createQuery(entity_class);
            Root<Student> studentRoot = query.from(entity_class);
            query.select(studentRoot).where(cb.like(studentRoot.<String>get("fullName"), "%" + pattern + "%"));
            return session.createQuery(query).getResultList();
        }
    }
}
