package edu.gprzyb.lab11_hibernate_final.dao;

import edu.gprzyb.lab11_hibernate_final.entites.Answer;

public interface AnswerDAO {
    Answer getAnswerAtID(Integer id);
    Answer modifyAnswer(Integer userID, Integer answerID, Integer rating);
    Answer createAnswer(Integer userID, Integer surveyID, Integer rating);
}
