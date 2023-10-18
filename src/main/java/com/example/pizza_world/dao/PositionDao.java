package com.example.pizza_world.dao;

import com.example.pizza_world.bean.Position;
import com.example.pizza_world.bean.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.pizza_world.config.FactoryManager.getSessionFactory;

@Repository
public class PositionDao extends AbstractBeanDao {

    public List<Position> findAll() {
        List<Position> positionList;
        Session session = getSessionFactory().openSession();
        positionList = session.createQuery("FROM Position ").list();
        session.close();
        return positionList;
    }
}
