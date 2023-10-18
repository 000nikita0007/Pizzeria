package com.example.pizza_world.dao;

import com.example.pizza_world.bean.Feedback;
import com.example.pizza_world.bean.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.pizza_world.config.FactoryManager.getSessionFactory;

@Repository
public class FeedbackDao extends AbstractBeanDao {

    public List<Feedback> findAll() {
        List<Feedback> feedbackList;
        Session session = getSessionFactory().openSession();
        feedbackList = session.createQuery("FROM Feedback ").list();
        session.close();
        return feedbackList;
    }
}
