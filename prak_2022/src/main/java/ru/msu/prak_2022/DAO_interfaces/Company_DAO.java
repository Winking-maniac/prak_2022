package ru.msu.prak_2022.DAO_interfaces;

import lombok.NonNull;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.List;

public interface Company_DAO extends Default_DAO<Company, Long>, simple_hub_DAO<Company>, Searchable<Company>{
    AbstractMap.SimpleEntry<status, List<Course>> courses(@NonNull Company company);
    AbstractMap.SimpleEntry<status, List<Teacher>> teachers(@NonNull Company company);
}
