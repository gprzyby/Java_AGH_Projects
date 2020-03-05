package edu.gprzyb.lab11_hibernate_final.dao.impl;

import edu.gprzyb.lab11_hibernate_final.dao.SurveyDAO;
import edu.gprzyb.lab11_hibernate_final.entites.Survey;
import edu.gprzyb.lab11_hibernate_final.entites.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SurveyDAOImpl implements SurveyDAO {
    private final SessionFactory sessionFactory;

    @Autowired
    public SurveyDAOImpl(EntityManagerFactory factory) {
        this.sessionFactory = factory.createEntityManager().unwrap(Session.class).getSessionFactory();
    }

    @Override
    public Survey addSurveyToUser(Integer userID, String title, String question) {
        Session session = null;
        Transaction transaction = null;
        Survey toRet = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            //firstly check if that user exists
            User user = session.get(User.class, userID);
            if(user == null) throw new EntityNotFoundException("No user at specified id");
            toRet = new Survey(title,question, user);
            session.save(toRet);
            transaction.commit();
        } catch (HibernateException exc) {
            if(transaction != null) transaction.rollback();
            throw exc;
        } finally {
            if(session != null) session.close();
        }
        return toRet;
    }

    @Override
    public List<Survey> getAllSurveys() {
        Session session = null;
        Transaction transaction = null;
        List<Survey> toRet;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            //firstly check if that user exists
            NativeQuery tmp = session.createSQLQuery("SELECT * FROM survey");
            tmp.addEntity(Survey.class);
            toRet = tmp.list();
            transaction.commit();
        } catch (HibernateException exc) {
            if(transaction != null) transaction.rollback();
            throw exc;
        } finally {
            if(session != null) session.close();
        }
        return toRet;
    }

    @Override
    public Survey getSurvey(Integer id) {
        Session session = null;
        Transaction transaction = null;
        Survey toRet = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            //firstly check if that user exists
            toRet = session.get(Survey.class, id);
            transaction.commit();
        } catch (HibernateException exc) {
            if(transaction != null) transaction.rollback();
            throw exc;
        } finally {
            if(session != null) session.close();
        }
        return toRet;
    }

    @Override
    public void deleteSurvey(Survey toDelete) {
        Session session = null;
        Transaction transaction = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.delete(toDelete);
            transaction.commit();
        } catch (HibernateException exc) {
            if(transaction != null) transaction.rollback();
            throw exc;
        } finally {
            if(session != null) session.close();
        }

    }

    @Override
    public void deleteSurvey(Integer id) {
        Session session = null;
        Transaction transaction = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            //firstly check if survey at specified id exist
            Survey survey = session.get(Survey.class, id);
            if(survey == null)
            {
                transaction.rollback();
                throw new EntityNotFoundException("No survey at specified id!");
            }
            session.createSQLQuery("DELETE FROM answer WHERE survey_id = :surveID").setParameter("surveID", id).executeUpdate();
            session.createSQLQuery("DELETE FROM survey WHERE survey_id = :surveID").setParameter("surveID",id).executeUpdate();
            session.flush();
            session.getTransaction().commit();
        } catch (HibernateException exc) {
            if(transaction != null) transaction.rollback();
            throw exc;
        } finally {
            if(session != null) session.close();
        }
    }

    @Override
    public List<Object> getSurveyRankByCreatedAmount() {
        Session session = null;
        Transaction transaction = null;
        List<Object> toRet = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<Object[]> resoult = session.createNativeQuery(
                    " SELECT u.user_id, u.username, COUNT(s.survey_id) AS amount\n" +
                    "FROM user u\n" +
                    "         LEFT JOIN survey s ON s.user_id = u.user_id\n" +
                    "GROUP BY u.user_id\n" +
                    "ORDER BY amount DESC")
                    .addScalar("user_id", IntegerType.INSTANCE)
                    .addScalar("username", StringType.INSTANCE)
                    .addScalar("amount", IntegerType.INSTANCE).list();
            toRet = new ArrayList<>();
            for(Object[] tab : resoult) {
                Map<String, Object> toAdd = new HashMap<>();
                toAdd.put("user_id", tab[0]);
                toAdd.put("username", tab[1]);
                toAdd.put("survey_amount", tab[2]);
                toRet.add(toAdd);
            }

            transaction.commit();
        } catch (HibernateException exc) {
            if(transaction != null) transaction.rollback();
            throw exc;
        } finally {
            if(session != null) session.close();
        }
        return toRet;
    }

    @Override
    public List<Object> getSurveyRankByAnswerAmount() {
        Session session = null;
        Transaction transaction = null;
        List<Object> toRet = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<Object[]> resoult = session.createNativeQuery(
                    "SELECT s.survey_id, s.survey_title, s.question, COUNT(a.answer_id) AS amount " +
                            "FROM survey s " +
                            "LEFT JOIN answer a ON s.survey_id = a.survey_id " +
                            "GROUP BY s.survey_id " +
                            "ORDER BY amount DESC")
                    .addScalar("survey_id", IntegerType.INSTANCE)
                    .addScalar("survey_title", StringType.INSTANCE)
                    .addScalar("question", StringType.INSTANCE)
                    .addScalar("amount", IntegerType.INSTANCE).list();
            toRet = new ArrayList<>();
            for(Object[] tab : resoult) {
                Map<String, Object> toAdd = new HashMap<>();
                toAdd.put("survey_id", tab[0]);
                toAdd.put("survey_title", tab[1]);
                toAdd.put("question", tab[2]);
                toAdd.put("amount", tab[3]);
                toRet.add(toAdd);
            }

            transaction.commit();
        } catch (HibernateException exc) {
            if(transaction != null) transaction.rollback();
            throw exc;
        } finally {
            if(session != null) session.close();
        }
        return toRet;
    }

    @Override
    public Double getAvgSurveyByUser() {
        Session session = null;
        Transaction transaction = null;
        Double toRet = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<Double> resoult = session.createNativeQuery(
                    "SELECT AVG(a.countVal) AS avgAmount " +
                            "FROM ( " +
                            "         SELECT COUNT(s.survey_id) AS countVal " +
                            "         FROM user u " +
                            "                  LEFT JOIN survey s ON s.user_id = u.user_id " +
                            "         GROUP BY u.user_id " +
                            "     ) a")
                    .addScalar("avgAmount", DoubleType.INSTANCE).list();
            toRet = resoult.get(0);

            transaction.commit();
        } catch (HibernateException exc) {
            if(transaction != null) transaction.rollback();
            throw exc;
        } finally {
            if(session != null) session.close();
        }
        return toRet;
    }

    @Override
    public Double getAvgSurveyByAnswer() {
        Session session = null;
        Transaction transaction = null;
        Double toRet = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            List<Double> resoult = session.createNativeQuery(
                    "SELECT AVG(inn.amount) AS avgAmount " +
                            "FROM( " +
                            "        SELECT COUNT(a.answer_id) AS amount " +
                            "        FROM survey s " +
                            "                 LEFT JOIN answer a ON s.survey_id = a.survey_id " +
                            "        GROUP BY s.survey_id) inn")
                    .addScalar("avgAmount", DoubleType.INSTANCE).list();
            toRet = resoult.get(0);

            transaction.commit();
        } catch (HibernateException exc) {
            if(transaction != null) transaction.rollback();
            throw exc;
        } finally {
            if(session != null) session.close();
        }
        return toRet;
    }
}
