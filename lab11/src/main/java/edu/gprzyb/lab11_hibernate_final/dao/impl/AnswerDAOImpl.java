package edu.gprzyb.lab11_hibernate_final.dao.impl;

import edu.gprzyb.lab11_hibernate_final.dao.AnswerDAO;
import edu.gprzyb.lab11_hibernate_final.entites.Answer;
import edu.gprzyb.lab11_hibernate_final.entites.Survey;
import edu.gprzyb.lab11_hibernate_final.entites.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Component
public class AnswerDAOImpl implements AnswerDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public AnswerDAOImpl(EntityManagerFactory factory) {
        this.sessionFactory = factory.createEntityManager().unwrap(Session.class).getSessionFactory();
    }

    @Override
    public Answer getAnswerAtID(Integer id) {
        Session session = null;
        Transaction transaction = null;
        Answer toRet = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            toRet = session.get(Answer.class, id);
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
    public Answer modifyAnswer(Integer userID, Integer answerID, Integer rating) {
        Session session = null;
        Transaction transaction = null;
        Answer toRet = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            //getting answer and checking if exists
            toRet = session.get(Answer.class, answerID);
            if(toRet == null) {
                transaction.rollback();
                throw new EntityNotFoundException("No answer at specified id");
            }

            //checking if userID is the same like in answer from db
            if(!toRet.getUser().getId().equals(userID)) {
                transaction.rollback();
                throw new EntityNotFoundException("Answer creator id mismatch");
            }

            //changing values and updating db
            toRet.setRating(rating);
            session.update(toRet);

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
    public Answer createAnswer(Integer userID, Integer surveyID, Integer rating) {
        Session session = null;
        Transaction transaction = null;
        Answer toRet = null;
        try{
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            //getting survey and user entities
            User user = session.get(User.class, userID);

            if(user == null) {
                transaction.rollback();
                throw new EntityNotFoundException("No user at specified id");
            }

            for(Answer answer : user.getAnswers()) {
                if(answer.getSurvey().getId().equals(surveyID)) {
                    transaction.rollback();
                    throw new IllegalArgumentException("User has answer for this survey!");
                }
            }

            Survey survey = session.get(Survey.class, surveyID);
            if(survey == null) {
                transaction.rollback();
                throw new EntityNotFoundException("No survey at specified id");
            }

            //creating answer
            toRet = new Answer(survey,user,rating);
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
}
