package ru.msu.prak_2022.DAO_implementations;

import lombok.NonNull;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.DAO_interfaces.Default_DAO;
import ru.msu.prak_2022.DAO_interfaces.Searchable;
import ru.msu.prak_2022.DAO_interfaces.Teacher_DAO;
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
public class Teacher_DAO_impl extends simple_hub_DAO_impl<Teacher> implements Teacher_DAO {

    @Override
    public AbstractMap.SimpleEntry<status, List<Teacher>> get(String pattern) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        gl_session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Teacher> query = cb.createQuery(entity_class);
        Root<Teacher> teacherRoot = query.from(entity_class);
        query.select(teacherRoot).where(cb.like(teacherRoot.<String>get("fullName"), "%" + pattern + "%"));
        AbstractMap.SimpleEntry<status, List<Teacher>> res = new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        gl_session.commit();
        return res;
    }

    @Override
    public AbstractMap.SimpleEntry<status, List<Teacher>> get_all() {
        Session session = gl_session.sessionFactory.getCurrentSession();
        gl_session.beginTransaction();
        CriteriaQuery<Teacher> query = session.getCriteriaBuilder().createQuery(this.entity_class);
        query.from(entity_class);
        AbstractMap.SimpleEntry<status, List<Teacher>> res = new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        gl_session.commit();
        return res;
    }

    public AbstractMap.SimpleEntry<status, List<Course>> courses(@NonNull Teacher teacher) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(teacher);
        Hibernate.initialize(teacher.getCourses());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, teacher.getCourses().stream().map(TeacherCourse::getCourse).toList());
    }

    public AbstractMap.SimpleEntry<status, List<Company>> companies(@NonNull Teacher teacher) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(teacher);
        Hibernate.initialize(teacher.getCompanies());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, teacher.getCompanies().stream().filter(CompanyTeacher::is_approved).map(CompanyTeacher::getCompany).toList());
    }

    public AbstractMap.SimpleEntry<status, List<Company>> invites(@NonNull Teacher teacher) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(teacher);
        Hibernate.initialize(teacher.getCompanies());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, teacher.getCompanies().stream().filter(ct -> !ct.is_approved()).map(CompanyTeacher::getCompany).toList());
    }

    public AbstractMap.SimpleEntry<status, List<Lesson>> lessons(@NonNull Teacher teacher) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(teacher);
        Hibernate.initialize(teacher.getLessons());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, teacher.getLessons());
    }
}
