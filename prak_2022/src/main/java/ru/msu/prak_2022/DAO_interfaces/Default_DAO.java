package ru.msu.prak_2022.DAO_interfaces;

import ru.msu.prak_2022.status;

import java.io.Serializable;
import java.util.AbstractMap;

public interface Default_DAO<T, ID extends Serializable> {
    // Read
    AbstractMap.SimpleEntry<status, T> get(ID id);
}
