package com.example.pizza_world.config;

import com.example.pizza_world.bean.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class FactoryManager {

    private static volatile FactoryManager instance;
    private static SessionFactory sessionFactory;

    private FactoryManager() {
    }

    public static SessionFactory getSessionFactory() {
        //Конфигурация хибера
        if (instance != null) {
            return sessionFactory;
        }
        synchronized (FactoryManager.class) {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Feedback.class);
            configuration.addAnnotatedClass(Order.class);
            configuration.addAnnotatedClass(PositionOrderMap.class);
            configuration.addAnnotatedClass(OrderStatus.class);
            configuration.addAnnotatedClass(Position.class);
            configuration.addAnnotatedClass(Role.class);
            configuration.addAnnotatedClass(Size.class);
            configuration.addAnnotatedClass(User.class);
            configuration.configure();
            StandardServiceRegistry registryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(registryBuilder);
            instance = new FactoryManager();
        }
        return sessionFactory;
    }
}
