package com.example.pizza_world.dao;

import com.example.pizza_world.bean.AbstractBean;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import static com.example.pizza_world.config.FactoryManager.getSessionFactory;

@Slf4j
public abstract class AbstractBeanDao {

    public void save(AbstractBean bean) {
        Session session = null;
        Transaction writeTransaction = null;

        try {
            session = getSessionFactory().openSession();
            writeTransaction = session.beginTransaction();
            session.save(bean);
        } catch (HibernateException exception) {
            log.error(exception.getMessage());
        } finally {
            try {
                if (writeTransaction != null) {
                    writeTransaction.commit();
                }
                if (session != null) {
                    session.close();
                }
            } catch (HibernateException exception) {
                log.error(exception.getMessage());
            }
        }
    }

    public void update(AbstractBean bean) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(bean);
        } catch (HibernateException exception) {
            log.error(exception.getMessage());
        } finally {
            try {
                if (transaction != null) {
                    transaction.commit();
                }
                if (session != null) {
                    session.close();
                }
            } catch (HibernateException exception) {
                log.error(exception.getMessage());
            }
        }
    }

    public void delete(AbstractBean bean) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.delete(bean);
        } catch (HibernateException exception) {
            log.error(exception.getMessage());
        } finally {
            try {
                if (transaction != null) {
                    transaction.commit();
                }
                if (session != null) {
                    session.close();
                }
            } catch (HibernateException exception) {
                log.error(exception.getMessage());
            }
        }
    }

    public <T extends AbstractBean > T findById(int id, Class<T> entityType) {
        Session session = null;
        T entity = null;
        try {
            session = getSessionFactory().openSession();
            entity = session.get(entityType, id);

        } catch (HibernateException exception) {
            log.error(exception.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return entity;
    }
}
