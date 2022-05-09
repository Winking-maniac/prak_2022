package ru.msu.prak_2022.DAO_interfaces;

import lombok.NonNull;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;


public interface Lesson_DAO extends Default_DAO<Lesson, Long> {
    AbstractMap.SimpleEntry<status, Course> get_course(@NonNull Lesson lesson);
    AbstractMap.SimpleEntry<status, Teacher> get_teacher(@NonNull Lesson lesson);
    AbstractMap.SimpleEntry<status, Map<Long, Long>> scores(@NonNull Lesson lesson);
}
