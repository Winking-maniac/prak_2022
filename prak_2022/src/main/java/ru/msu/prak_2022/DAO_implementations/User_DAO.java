package ru.msu.prak_2022.DAO_implementations;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.Role;
import ru.msu.prak_2022.models.User;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

@Service
//@Repository
public class User_DAO implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean save_user(Long user_type, String username, String passwd, Object obj) {
        gl_session.beginTransaction();
//        gl_session.sessionFactory.getCurrentSession().get(User.class, )
        Session session = gl_session.sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root).where(cb.equal(root.get("username"), username));  //here you pass a class field, not a table column (in this example they are called the same)

        Query query = session.createQuery(cr);
        List users = query.getResultList();
        if (users.size() > 0) {
            return false;
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(passwd));
        user.setUsername(username);
        user.setRoles(Collections.singleton(new Role(user_type)));
        session.save(user);
        session.save(obj);
        gl_session.commit();
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        gl_session.beginTransaction();
//        gl_session.sessionFactory.getCurrentSession().get(User.class, )
        Session session = gl_session.sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root).where(cb.equal(root.get("username"), username));  //here you pass a class field, not a table column (in this example they are called the same)

        Query query = session.createQuery(cr);
        List users = query.getResultList();
        if (users.size() != 1) throw new UsernameNotFoundException("User not found");
        return (UserDetails) users.get(0);
    }

    public User by_username(String username){
        gl_session.beginTransaction();
//        gl_session.sessionFactory.getCurrentSession().get(User.class, )
        Session session = gl_session.sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> cr = cb.createQuery(User.class);
        Root<User> root = cr.from(User.class);
        cr.select(root).where(cb.equal(root.get("username"), username));  //here you pass a class field, not a table column (in this example they are called the same)

        Query query = session.createQuery(cr);
        List<User> users = query.getResultList();
        if (users.size() != 1) return null;
        return users.get(0);
    }
}
