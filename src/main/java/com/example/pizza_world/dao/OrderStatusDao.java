package com.example.pizza_world.dao;

import com.example.pizza_world.bean.OrderStatus;
import com.example.pizza_world.bean.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.pizza_world.config.FactoryManager.getSessionFactory;

@Repository
public class OrderStatusDao extends AbstractBeanDao {

    public List<OrderStatus> findAll() {
        List<OrderStatus> orderStatusList;
        Session session = getSessionFactory().openSession();
        orderStatusList = session.createQuery("FROM OrderStatus ").list();
        session.close();
        return orderStatusList;
    }
}
