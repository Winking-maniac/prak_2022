package ru.msu.prak_2022.DAO_interfaces;

import org.hibernate.SessionFactory;
import ru.msu.prak_2022.status;

import java.util.AbstractMap;
import java.util.Collection;

public interface simple_hub_DAO<T> {
    // Delete
    status delete(T obj);
    //void bulk_delete(Collection<T> objs);

    // Update
    status update(T obj);
    //void bulk_update(Collection<T> objs);

    // Save
    status save(T obj);
    //void bulk_save(Collection<T> objs);
}
