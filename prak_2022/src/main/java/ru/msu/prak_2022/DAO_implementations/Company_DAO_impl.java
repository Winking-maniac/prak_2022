package ru.msu.prak_2022.DAO_implementations;

import lombok.NonNull;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.DAO_interfaces.Default_DAO;
import ru.msu.prak_2022.DAO_interfaces.Searchable;
import ru.msu.prak_2022.DAO_interfaces.simple_hub_DAO;
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
public class Company_DAO_impl extends simple_hub_DAO_impl<Company> implements Default_DAO<Company, Long>, simple_hub_DAO<Company>, Searchable<Company> {

    @Override
    public AbstractMap.SimpleEntry<status, Collection<Company>> get(String pattern) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Company> query = cb.createQuery(entity_class);
            Root<Company> companyRoot = query.from(entity_class);
            query.select(companyRoot).where(cb.like(companyRoot.<String>get("company_name"), "%" + pattern + "%"));
            return new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        }
    }

    @Override
    public AbstractMap.SimpleEntry<status, Collection<Company>> get_all() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Company> query = session.getCriteriaBuilder().createQuery(this.entity_class);
            query.from(entity_class);
            return new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        }
    }

    public AbstractMap.SimpleEntry<status, List<Course>> courses(@NonNull Company company) {
        try (Session session = sessionFactory.openSession()) {
            return new AbstractMap.SimpleEntry<>(status.OK, (List<Course>) company.getCourses().stream().map(CompanyCourse::getCourse).toList());
        }
    }

    public AbstractMap.SimpleEntry<status, List<Teacher>> teachers(@NonNull Company company) {
        try (Session session = sessionFactory.openSession()) {
//            Predicate<CompanyTeacher> approved = CompanyTeacher::is_approved;
            return new AbstractMap.SimpleEntry<>(status.OK, (List<Teacher>) company.getTeachers().stream().filter(CompanyTeacher::is_approved).map(CompanyTeacher::getTeacher).toList());
        }
    }
}
