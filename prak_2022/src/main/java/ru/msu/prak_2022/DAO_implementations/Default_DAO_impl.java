package ru.msu.prak_2022.DAO_implementations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.DAO_interfaces.Default_DAO;
import ru.msu.prak_2022.status;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.AbstractMap;

@Repository
public abstract class Default_DAO_impl<T, ID extends Serializable> implements Default_DAO<T, ID> {
    protected SessionFactory sessionFactory;

    protected Class<T> entity_class;

    public Default_DAO_impl() {
//        this.entity_class = entity_class;
        this.entity_class = ((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @Autowired
    public void setSessionFactory(LocalSessionFactoryBean session_factory) {
        this.sessionFactory = session_factory.getObject();
    }

    @Override
    public AbstractMap.SimpleEntry<status, T> get(ID id) {
        try (Session session = sessionFactory.openSession()) {
            T obj = session.get(entity_class, id);
            if (obj == null) {
                return new AbstractMap.SimpleEntry<>(status.NOT_FOUND, null);
            } else {
                return new AbstractMap.SimpleEntry<>(status.OK, obj);
            }
        }
    }
}
