package ru.msu.prak_2022.DAO_interfaces;

import org.hibernate.SessionFactory;

import java.util.Collection;

public interface simple_hub_DAO<T> {
    Collection<T> get(String pattern);
}
