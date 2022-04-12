package ru.msu.prak_2022.DAO_implementations;

import lombok.NonNull;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.DAO_interfaces.Default_DAO;
import ru.msu.prak_2022.DAO_interfaces.Lesson_DAO;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

@Repository
public class Lesson_DAO_impl extends Default_DAO_impl<Lesson, Long> implements Lesson_DAO {
    public SimpleEntry<status, Course> get_course(@NonNull Lesson lesson) {
        return new SimpleEntry<>(status.OK, lesson.getCourse());
    }

    public SimpleEntry<status, Teacher> get_teacher(@NonNull Lesson lesson) {
        return new SimpleEntry<>(status.OK, lesson.getTeacher());
    }

    public SimpleEntry<status, List<SimpleEntry<Long, Long>>> scores(@NonNull Lesson lesson) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.refresh(lesson);
        Hibernate.initialize(lesson.getStudents());
        session.getTransaction().commit();
        return new SimpleEntry<>(status.OK, lesson.getStudents().stream().map(sl -> new SimpleEntry<>(sl.getStudent_id(), sl.getScore())).toList());
    }
}
