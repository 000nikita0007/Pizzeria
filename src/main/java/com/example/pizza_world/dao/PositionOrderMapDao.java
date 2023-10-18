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
public class PositionOrderMapDao extends AbstractBeanDao {

    public List<PositionOrderMap> findOrderPositions(Order order) {
        List<PositionOrderMap> positionList = new ArrayList<>();
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Query<PositionOrderMap> query = session.createQuery("FROM PositionOrderMap WHERE order = :order", PositionOrderMap.class);
            query.setParameter("order", order);
            positionList = query.list();

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return positionList;
    }

    public List<PositionOrderMap> findAll() {
        List<PositionOrderMap> positionOrderMapList;
        Session session = getSessionFactory().openSession();
        positionOrderMapList = session.createQuery("FROM PositionOrderMap ").list();
        session.close();
        return positionOrderMapList;
    }
}
