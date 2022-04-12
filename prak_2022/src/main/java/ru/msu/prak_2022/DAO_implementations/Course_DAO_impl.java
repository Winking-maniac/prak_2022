package ru.msu.prak_2022.DAO_implementations;

import lombok.NonNull;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.DAO_interfaces.Course_DAO;
import ru.msu.prak_2022.DAO_interfaces.Default_DAO;
import ru.msu.prak_2022.DAO_interfaces.Searchable;
import ru.msu.prak_2022.DAO_interfaces.simple_hub_DAO;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;

@Repository
public class Course_DAO_impl extends Default_DAO_impl<Course, Long> implements Course_DAO {

    @Override
    public AbstractMap.SimpleEntry<status, List<Course>> get(String pattern) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        gl_session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Course> query = cb.createQuery(entity_class);
        Root<Course> courseRoot = query.from(entity_class);
        query.select(courseRoot).where(cb.like(courseRoot.<String>get("course_name"), "%" + pattern + "%"));
        AbstractMap.SimpleEntry<status, List<Course>> res = new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        gl_session.commit();
        return res;
    }

    @Override
    public AbstractMap.SimpleEntry<status, List<Course>> get_all() {
        Session session = gl_session.sessionFactory.getCurrentSession();
        gl_session.beginTransaction();
        CriteriaQuery<Course> query = session.getCriteriaBuilder().createQuery(this.entity_class);
        query.from(entity_class);
        AbstractMap.SimpleEntry<status, List<Course>> res = new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        gl_session.commit();
        return res;
    }

    public AbstractMap.SimpleEntry<status, List<Teacher>> teachers(@NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(course);
        Hibernate.initialize(course.getTeachers());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, (List<Teacher>) course.getTeachers().stream().map(TeacherCourse::getTeacher).toList());
    }

    public AbstractMap.SimpleEntry<status, List<Company>> companies(@NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(course);
        Hibernate.initialize(course.getCompanies());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, (List<Company>) course.getCompanies().stream().map(CompanyCourse::getCompany).toList());
    }

    public AbstractMap.SimpleEntry<status, List<Student>> students(@NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(course);
        Hibernate.initialize(course.getStudents());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, (List<Student>) course.getStudents().stream().map(StudentCourse::getStudent).toList());
    }

    public AbstractMap.SimpleEntry<status, List<Lesson>> lessons(@NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(course);
        Hibernate.initialize(course.getLessons());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, (List<Lesson>) course.getLessons());
    }
}
