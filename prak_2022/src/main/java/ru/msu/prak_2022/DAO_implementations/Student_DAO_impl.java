package ru.msu.prak_2022.DAO_implementations;

import lombok.NonNull;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.msu.prak_2022.DAO_interfaces.Default_DAO;
import ru.msu.prak_2022.DAO_interfaces.Searchable;
import ru.msu.prak_2022.DAO_interfaces.Student_DAO;
import ru.msu.prak_2022.DAO_interfaces.simple_hub_DAO;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.Course;
import ru.msu.prak_2022.models.Student;
import ru.msu.prak_2022.models.StudentCourse;
import ru.msu.prak_2022.status;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.AbstractMap;
import java.util.List;

@Repository
//@EnableTransactionManagement
//@Transactional
public class Student_DAO_impl extends simple_hub_DAO_impl<Student> implements Student_DAO {

    @Override
    @Transactional
    public AbstractMap.SimpleEntry<status, List<Student>> get(String pattern) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Student> query = cb.createQuery(entity_class);
        Root<Student> studentRoot = query.from(entity_class);
        query.select(studentRoot).where(cb.like(studentRoot.<String>get("fullName"), "%" + pattern + "%"));
        AbstractMap.SimpleEntry<status, List<Student>> res = new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        session.getTransaction().commit();
        return res;
    }

    @Override
    public AbstractMap.SimpleEntry<status, List<Student>> get_all() {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        CriteriaQuery<Student> query = session.getCriteriaBuilder().createQuery(this.entity_class);
        query.from(entity_class);
        AbstractMap.SimpleEntry<status, List<Student>> res = new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        session.getTransaction().commit();
        return res;

    }

    public AbstractMap.SimpleEntry<status, List<Course>> courses(@NonNull Student student) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(student);
        Hibernate.initialize(student.getCourses());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, (List<Course>) student.getCourses().stream().map(StudentCourse::getCourse).toList());
    }
}