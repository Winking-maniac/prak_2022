package ru.msu.prak_2022.DAO_interfaces;

import lombok.NonNull;
import ru.msu.prak_2022.models.Course;
import ru.msu.prak_2022.models.Student;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.List;

public interface Student_DAO extends Default_DAO<Student, Long>, simple_hub_DAO<Student>, Searchable<Student>{
    AbstractMap.SimpleEntry<status, List<Course>> courses(@NonNull Student student);
}
