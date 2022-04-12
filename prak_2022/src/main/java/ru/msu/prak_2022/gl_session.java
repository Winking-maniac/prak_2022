package ru.msu.prak_2022;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Repository
//@EnableTransactionManagement
//@Transactional
public class gl_session {
    public static SessionFactory sessionFactory;

    public static void open() {
        sessionFactory.openSession();
    }

    public static void close() {
        sessionFactory.getCurrentSession().close();
    }

    public static void beginTransaction() {sessionFactory.getCurrentSession().beginTransaction(); }

    public static void commit() {sessionFactory.getCurrentSession().getTransaction().commit(); }

    @Autowired
    public void setSessionFactory(LocalSessionFactoryBean session_factory) {
        this.sessionFactory = session_factory.getObject();
    }

//    @Autowired
//    public void setSessionFactory() {
//        this.sessionFactory = new HibConf().sessionFactory().getObject();
//    }
}
