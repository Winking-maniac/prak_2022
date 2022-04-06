package ru.msu.prak_2022.DAO_implementations;

import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.DAO_interfaces.Default_DAO;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

@Repository
public class Lesson_DAO_impl extends Default_DAO_impl<Lesson, LessonId> implements Default_DAO<Lesson, LessonId> {
    public SimpleEntry<status, Course> get_course(@NonNull Lesson lesson) {
        return new SimpleEntry<>(status.OK, lesson.getCourse());
    }

    public SimpleEntry<status, Teacher> get_teacher(@NonNull Lesson lesson) {
        return new SimpleEntry<>(status.OK, lesson.getTeacher());
    }

    public SimpleEntry<status, List<SimpleEntry<Long, Long>>> scores(Lesson lesson) {
        return new SimpleEntry<>(status.OK, lesson.getStudents().stream().map(sl -> new SimpleEntry<>(sl.getStudent_id(), sl.getScore())).toList());
    }
}
