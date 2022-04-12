package ru.msu.prak_2022.DAO_interfaces;

import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;

public interface Searchable<T> {
    AbstractMap.SimpleEntry<status, List<T>> get(String pattern);
    AbstractMap.SimpleEntry<status, List<T>> get_all();
}
