package ru.msu.prak_2022.DAO_implementations;

import lombok.NonNull;
import lombok.val;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.DAO_interfaces.Company_DAO;
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
import java.util.function.Predicate;

@Repository
public class Company_DAO_impl extends simple_hub_DAO_impl<Company> implements Company_DAO {

    @Override
    public AbstractMap.SimpleEntry<status, List<Company>> get(String pattern) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Company> query = cb.createQuery(entity_class);
        Root<Company> companyRoot = query.from(entity_class);
        query.select(companyRoot).where(cb.like(companyRoot.<String>get("company_name"), "%" + pattern + "%"));
        AbstractMap.SimpleEntry<status, List<Company>> res = new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        session.getTransaction().commit();
        return res;
    }

    @Override
    public AbstractMap.SimpleEntry<status, List<Company>> get_all() {
        Session session = gl_session.sessionFactory.getCurrentSession();
        gl_session.beginTransaction();
        CriteriaQuery<Company> query = session.getCriteriaBuilder().createQuery(this.entity_class);
        query.from(entity_class);
        AbstractMap.SimpleEntry<status, List<Company>> res = new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        gl_session.commit();
        return res;
    }

    public AbstractMap.SimpleEntry<status, List<Course>> courses(@NonNull Company company) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(company);
        Hibernate.initialize(company.getCourses());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, (List<Course>) company.getCourses().stream().map(CompanyCourse::getCourse).toList());
    }

    public AbstractMap.SimpleEntry<status, List<Teacher>> teachers(@NonNull Company company) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(company);
        Hibernate.initialize(company.getTeachers());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, (List<Teacher>) company.getTeachers().stream().filter(CompanyTeacher::is_approved).map(CompanyTeacher::getTeacher).toList());
    }

    public AbstractMap.SimpleEntry<status, List<Teacher>> invites(@NonNull Company company) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(company);
        Hibernate.initialize(company.getTeachers());
        session.getTransaction().commit();
        return new AbstractMap.SimpleEntry<>(status.OK, (List<Teacher>) company.getTeachers().stream().filter(ct -> !ct.is_approved()).map(CompanyTeacher::getTeacher).toList());
    }
}
