package ru.msu.prak_2022.DAO_interfaces;

import lombok.NonNull;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.time.LocalTime;
import java.util.AbstractMap;

public interface Relations_DAO {
    status enroll(@NonNull Student student, @NonNull Course course);
    status unenroll(@NonNull Student student, @NonNull Course course);
    AbstractMap.SimpleEntry<status, Boolean> is_student(@NonNull Student student, @NonNull Course course);
    AbstractMap.SimpleEntry<status, Boolean> is_member(@NonNull Teacher teacher, @NonNull Company company);
    AbstractMap.SimpleEntry<status, Boolean> is_invited(@NonNull Teacher teacher, @NonNull Company company);
    status approve(@NonNull Teacher teacher, @NonNull Company company);
    status reject(@NonNull Teacher teacher, @NonNull Company company);
    status retire(@NonNull Teacher teacher, @NonNull Company company);
    status invite(@NonNull Teacher teacher, @NonNull Company company);
    status fire(@NonNull Teacher teacher, @NonNull Company company);
    AbstractMap.SimpleEntry<status, Boolean> is_admin(@NonNull Teacher teacher, @NonNull Course course);
    status create_course(@NonNull Company company, @NonNull Course course);
    AbstractMap.SimpleEntry<status, Boolean> is_author(@NonNull Company company, @NonNull Course course);
    status update_course(@NonNull Company company, @NonNull Course course);
    status delete_course(@NonNull Company company, @NonNull Course course);
    status grant_admin(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher);
    status revoke_admin(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher);
    status add_teacher(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher);
    status delete_teacher(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher);
    status create_lesson(@NonNull Teacher teacher,
                         @NonNull Course course,
                         @NonNull java.sql.Timestamp time_from,
                         @NonNull java.sql.Timestamp time_till,
                         String description);
    status update_lesson(@NonNull Teacher teacher,
                                @NonNull Course course,
                                @NonNull Lesson lesson);
    status delete_lesson(@NonNull Teacher teacher,
                         @NonNull Course course,
                         Long lesson_id);
    public status rate(Teacher teacher,
                       Lesson lesson,
                       Student student,
                       Long score);
}
