package ru.msu.prak_2022.DAO_interfaces;

import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.Collection;

public interface Searchable<T> {
    AbstractMap.SimpleEntry<status, Collection<T>> get(String pattern);
    AbstractMap.SimpleEntry<status, Collection<T>> get_all();
}
