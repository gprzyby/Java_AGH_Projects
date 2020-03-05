package edu.gprzyb.lab11_hibernate_final.dao;

import edu.gprzyb.lab11_hibernate_final.entites.Survey;

import java.util.List;
import java.util.Map;

public interface SurveyDAO {
    Survey addSurveyToUser(Integer userID, String title, String question);
    List<Survey> getAllSurveys();
    Survey getSurvey(Integer id);
    void deleteSurvey(Survey toDelete);
    void deleteSurvey(Integer id);
    List<Object> getSurveyRankByCreatedAmount();
    List<Object> getSurveyRankByAnswerAmount();
    Double getAvgSurveyByUser();
    Double getAvgSurveyByAnswer();
}
