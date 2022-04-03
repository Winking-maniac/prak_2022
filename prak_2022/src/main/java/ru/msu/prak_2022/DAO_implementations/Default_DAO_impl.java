package ru.msu.prak_2022.DAO_implementations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.DAO_interfaces.Default_DAO;
import ru.msu.prak_2022.models.Student;

import javax.persistence.criteria.CriteriaQuery;

import java.io.Serializable;
import java.util.Collection;

@Repository
public abstract class Default_DAO_impl<T, ID extends Serializable> implements Default_DAO<T, ID> {
    protected SessionFactory factory;

    protected Class<T> entity_class;

    public Default_DAO_impl(Class<T> entity_class) {
        this.entity_class = entity_class;
    }

    @Autowired
    public void setFactory(LocalSessionFactoryBean session_factory) {
        this.factory = session_factory.getObject();
    }

    @Override
    public Collection<T> get_all() {
        try (Session session = factory.openSession()) {
            CriteriaQuery<T> query = session.getCriteriaBuilder().createQuery(this.entity_class);
            query.from(entity_class);
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public void save(T obj) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.save(obj);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(T obj) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.delete(obj);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(T obj) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.update(obj);
            session.getTransaction().commit();
        }
    }

    @Override
    public T get(ID id) {
        try (Session session = factory.openSession()) {
            return session.get(entity_class, id);
        }
    }
}
