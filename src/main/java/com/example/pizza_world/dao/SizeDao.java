package com.example.pizza_world.dao;

import com.example.pizza_world.bean.Size;
import com.example.pizza_world.bean.User;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.pizza_world.config.FactoryManager.getSessionFactory;

@Repository
public class SizeDao extends AbstractBeanDao {

    public List<Size> findAll() {
        List<Size> sizeList;
        Session session = getSessionFactory().openSession();
        sizeList = session.createQuery("FROM Size ").list();
        session.close();
        return sizeList;
    }
}
