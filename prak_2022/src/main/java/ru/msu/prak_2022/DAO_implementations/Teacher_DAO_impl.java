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

@Repository
public class Teacher_DAO_impl extends simple_hub_DAO_impl<Teacher> implements Default_DAO<Teacher, Long>, simple_hub_DAO<Teacher>, Searchable<Teacher> {

    @Override
    public AbstractMap.SimpleEntry<status, Collection<Teacher>> get(String pattern) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Teacher> query = cb.createQuery(entity_class);
            Root<Teacher> teacherRoot = query.from(entity_class);
            query.select(teacherRoot).where(cb.like(teacherRoot.<String>get("fullName"), "%" + pattern + "%"));
            return new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        }
    }

    @Override
    public AbstractMap.SimpleEntry<status, Collection<Teacher>> get_all() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Teacher> query = session.getCriteriaBuilder().createQuery(this.entity_class);
            query.from(entity_class);
            return new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        }
    }

    public AbstractMap.SimpleEntry<status, List<Course>> courses(@NonNull Teacher teacher) {
        return new AbstractMap.SimpleEntry<>(status.OK, teacher.getCourses().stream().map(TeacherCourse::getCourse).toList());
    }

    public AbstractMap.SimpleEntry<status, List<Company>> companies(@NonNull Teacher teacher) {
        return new AbstractMap.SimpleEntry<>(status.OK, teacher.getCompanies().stream().filter(CompanyTeacher::is_approved).map(CompanyTeacher::getCompany).toList());
    }

    public AbstractMap.SimpleEntry<status, List<Company>> invites(@NonNull Teacher teacher) {
        return new AbstractMap.SimpleEntry<>(status.OK, teacher.getCompanies().stream().filter(ct -> !ct.is_approved()).map(CompanyTeacher::getCompany).toList());
    }

    public AbstractMap.SimpleEntry<status, List<Lesson>> lessons(@NonNull Teacher teacher) {
        return new AbstractMap.SimpleEntry<>(status.OK, teacher.getLessons());
    }
}
