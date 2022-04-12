package ru.msu.prak_2022.DAO_implementations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.DAO_interfaces.simple_hub_DAO;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.status;

import java.lang.reflect.ParameterizedType;

@Repository
public abstract class simple_hub_DAO_impl<T> extends Default_DAO_impl<T, Long> implements simple_hub_DAO<T> {
    protected Class<T> entity_class;

    public simple_hub_DAO_impl() {
        super();
        this.entity_class = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public status save(T obj) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(obj);
        session.getTransaction().commit();
        return status.OK;
    }

    @Override
    public status delete(T obj) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.delete(obj);
        session.getTransaction().commit();
        return status.OK;
    }

    @Override
    public status update(T obj) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.update(obj);
        session.getTransaction().commit();
        return status.OK;
    }
}
