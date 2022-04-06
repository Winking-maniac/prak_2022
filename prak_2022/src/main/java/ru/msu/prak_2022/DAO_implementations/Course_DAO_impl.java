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
public class Course_DAO_impl extends Default_DAO_impl<Course, CourseId> implements Default_DAO<Course, CourseId>, Searchable<Course> {

    @Override
    public AbstractMap.SimpleEntry<status, Collection<Course>> get(String pattern) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = cb.createQuery(entity_class);
            Root<Course> courseRoot = query.from(entity_class);
            query.select(courseRoot).where(cb.like(courseRoot.<String>get("course_name"), "%" + pattern + "%"));
            return new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        }
    }

    @Override
    public AbstractMap.SimpleEntry<status, Collection<Course>> get_all() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Course> query = session.getCriteriaBuilder().createQuery(this.entity_class);
            query.from(entity_class);
            return new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        }
    }

    public AbstractMap.SimpleEntry<status, List<Teacher>> teachers(@NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            return new AbstractMap.SimpleEntry<>(status.OK, (List<Teacher>) course.getTeachers().stream().map(TeacherCourse::getTeacher).toList());
        }
    }

    public AbstractMap.SimpleEntry<status, List<Company>> companies(@NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            return new AbstractMap.SimpleEntry<>(status.OK, (List<Company>) course.getCompanies().stream().map(CompanyCourse::getCompany).toList());
        }
    }

    public AbstractMap.SimpleEntry<status, List<Student>> students(@NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            return new AbstractMap.SimpleEntry<>(status.OK, (List<Student>) course.getStudents().stream().map(StudentCourse::getStudent).toList());
        }
    }

    public AbstractMap.SimpleEntry<status, List<Lesson>> lessons(@NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            return new AbstractMap.SimpleEntry<>(status.OK, (List<Lesson>) course.getLessons());
        }
    }
}
