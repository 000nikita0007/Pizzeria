package com.example.pizza_world.dao;

import com.example.pizza_world.bean.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.pizza_world.config.FactoryManager.getSessionFactory;

@Repository
public class UserDao extends AbstractBeanDao{

    public List<User> findAll() {
        List<User> userList;
        Session session = getSessionFactory().openSession();
        userList = session.createQuery("FROM User ").list();
        session.close();
        return userList;
    }
    public User findByLogin(String login) {
        Session session = getSessionFactory().openSession();
        String queryString = "FROM User WHERE login = :login";
        Query<User> query = session.createQuery(queryString, User.class);
        query.setParameter("login", login);
        User user = query.uniqueResult();
        session.close();
        return user;
    }
}
