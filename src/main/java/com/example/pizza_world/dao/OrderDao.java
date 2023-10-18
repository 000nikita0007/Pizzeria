package com.example.pizza_world.dao;

import com.example.pizza_world.bean.Order;
import com.example.pizza_world.bean.PositionOrderMap;
import com.example.pizza_world.bean.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.pizza_world.config.FactoryManager.getSessionFactory;

@Repository
public class OrderDao extends AbstractBeanDao {

    public List<Order> findAll() {
        List<Order> orderList;
        Session session = getSessionFactory().openSession();
        orderList = session.createQuery("FROM Order ").list();
        session.close();
        return orderList;
    }

    public List<Order> findUserOrders(User user) {
        List<Order> orderList = new ArrayList<>();
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Query<Order> query = session.createQuery("FROM Order WHERE user = :user", Order.class);
            query.setParameter("user", user);
            orderList = query.list();

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }
}
