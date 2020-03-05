package edu.gprzyb.lab11_hibernate_final.dao.impl;

import edu.gprzyb.lab11_hibernate_final.dao.UserDAO;
import edu.gprzyb.lab11_hibernate_final.entites.Survey;
import edu.gprzyb.lab11_hibernate_final.entites.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Set;

@Component
public class UserDAOImpl implements UserDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDAOImpl(EntityManagerFactory sessionFactory) {
        this.sessionFactory = sessionFactory.createEntityManager().unwrap(Session.class).getSessionFactory();
    }

    @Override
    public List<User> getAllUsers() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            NativeQuery query = session.createSQLQuery("SELECT * FROM user");
            query.addEntity(User.class);
            List<User> toRet = query.list();
            transaction.commit();
            return toRet;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        } finally {
            if(session != null) session.close();
        }
        return null;
    }

    @Override
    public User getUser(Integer id) {
        Session session = null;
        Transaction transaction = null;
        User user = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
            transaction.commit();
        } catch(Exception e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        } finally {
            if(session != null) session.close();
        }
        return user;
    }

    @Override
    public boolean checkIfUserExists(Integer id) {
        Session session = null;
        Transaction transaction = null;
        boolean toRet = false;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);

            if(user != null) {
                toRet = true;
            } else {
                toRet = false;
            }
            transaction.commit();
        } catch(Exception e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        } finally {
            if(session != null) session.close();
        }
        return toRet;

    }

    @Override
    public User createUser(String name) {
        Session session = null;
        Transaction transaction = null;
        User toRet = null;
        try {
            toRet = new User(name);
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Integer userID = (Integer) session.save(toRet);
            transaction.commit();
            toRet.setId(userID);
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) transaction.rollback();
        } finally {
            if(session != null) session.close();
        }

        return toRet;
    }

    @Override
    public Set<Survey> getUserSurveys(Integer id) {
        User user = getUser(id);
        if(user == null) return null;
        else {
            return user.getSurveys();
        }
    }

}
