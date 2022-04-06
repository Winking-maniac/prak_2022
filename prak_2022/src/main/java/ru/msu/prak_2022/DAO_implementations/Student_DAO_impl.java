package ru.msu.prak_2022.DAO_implementations;

import lombok.NonNull;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.DAO_interfaces.Default_DAO;
import ru.msu.prak_2022.DAO_interfaces.Searchable;
import ru.msu.prak_2022.DAO_interfaces.simple_hub_DAO;
import ru.msu.prak_2022.models.Course;
import ru.msu.prak_2022.models.Student;
import ru.msu.prak_2022.models.StudentCourse;
import ru.msu.prak_2022.status;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;

@Repository
public class Student_DAO_impl extends simple_hub_DAO_impl<Student> implements Default_DAO<Student, Long>, simple_hub_DAO<Student>, Searchable<Student> {


    @Override
    public AbstractMap.SimpleEntry<status, Collection<Student>> get(String pattern) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Student> query = cb.createQuery(entity_class);
            Root<Student> studentRoot = query.from(entity_class);
            query.select(studentRoot).where(cb.like(studentRoot.<String>get("fullName"), "%" + pattern + "%"));
            return new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        }
    }

    @Override
    public AbstractMap.SimpleEntry<status, Collection<Student>> get_all() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaQuery<Student> query = session.getCriteriaBuilder().createQuery(this.entity_class);
            query.from(entity_class);
            return new AbstractMap.SimpleEntry<>(status.OK, session.createQuery(query).getResultList());
        }
    }

    public AbstractMap.SimpleEntry<status, List<Course>> courses(@NonNull Student student) {
        try (Session session = sessionFactory.openSession()) {
            return new AbstractMap.SimpleEntry<>(status.OK, (List<Course>) student.getCourses().stream().map(StudentCourse::getCourse).toList());
        }
    }
}
