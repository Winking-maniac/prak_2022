package ru.msu.prak_2022.DAO_interfaces;

import lombok.NonNull;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.List;

public interface Teacher_DAO extends Default_DAO<Teacher, Long>, simple_hub_DAO<Teacher>, Searchable<Teacher>{
    AbstractMap.SimpleEntry<status, List<Course>> courses(@NonNull Teacher teacher);
    AbstractMap.SimpleEntry<status, List<Company>> companies(@NonNull Teacher teacher);
    AbstractMap.SimpleEntry<status, List<Company>> invites(@NonNull Teacher teacher);
    AbstractMap.SimpleEntry<status, List<Lesson>> lessons(@NonNull Teacher teacher);

}
