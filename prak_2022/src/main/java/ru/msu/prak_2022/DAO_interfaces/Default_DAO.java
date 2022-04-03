package ru.msu.prak_2022.DAO_interfaces;

import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.Collection;

public interface Default_DAO<T, ID extends Serializable> {

    // Read
    T get(ID id);
    Collection<T> get_all();

    // Save
    void save(T obj);
    //void bulk_save(Collection<T> objs);

    // Delete
    void delete(T obj);
    //void bulk_delete(Collection<T> objs);

    // Update
    void update(T obj);
    //void bulk_update(Collection<T> objs);

}
