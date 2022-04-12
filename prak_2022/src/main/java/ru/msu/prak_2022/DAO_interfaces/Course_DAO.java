package ru.msu.prak_2022.DAO_interfaces;


import lombok.NonNull;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.List;


public interface Course_DAO extends Default_DAO<Course, Long>, Searchable<Course> {
    AbstractMap.SimpleEntry<status, List<Teacher>> teachers(@NonNull Course course);
    AbstractMap.SimpleEntry<status, List<Company>> companies(@NonNull Course course);
    AbstractMap.SimpleEntry<status, List<Student>> students(@NonNull Course course);
    AbstractMap.SimpleEntry<status, List<Lesson>> lessons(@NonNull Course course);
}
