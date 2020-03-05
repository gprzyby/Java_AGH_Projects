package edu.gprzyb.lab11_hibernate_final.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.gprzyb.lab11_hibernate_final.dao.AnswerDAO;
import edu.gprzyb.lab11_hibernate_final.dao.SurveyDAO;
import edu.gprzyb.lab11_hibernate_final.dao.UserDAO;
import edu.gprzyb.lab11_hibernate_final.entites.Answer;
import edu.gprzyb.lab11_hibernate_final.entites.Survey;
import edu.gprzyb.lab11_hibernate_final.entites.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "api")
public class MainController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SurveyDAO surveyDAO;

    @Autowired
    private AnswerDAO answerDAO;

    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        Integer userID = Integer.valueOf(id);
        return new ResponseEntity<>(userDAO.getUser(userID), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody Map<String,String> data) {
        if(!data.containsKey("name")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No name in JSON struct");
        User toRet = userDAO.createUser(data.get("name"));
        return new ResponseEntity<User>(toRet, HttpStatus.OK);
    }

    @GetMapping("/survey/user/{ID}")
    public ResponseEntity<Set<Survey>> getUserSurveys(@PathVariable String ID) {
        int user_id;
        try{
            user_id = Integer.valueOf(ID);
        } catch (NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID wasn't an number!");
        }
        User user = userDAO.getUser(user_id);
        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user at specified ID");
        return new ResponseEntity<>(user.getSurveys(),HttpStatus.OK);
    }

    @PostMapping("/survey")
    public ResponseEntity<Survey> addSurvey(@RequestBody Map<String,String> data, @RequestHeader(value = "user_id") String userId){
        if(!data.containsKey("title") || !data.containsKey("question") ) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No title and question in JSON header");
        if(userId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user_id in http header!");
        int user_id;
        try{
            user_id = Integer.valueOf(userId);
        } catch (NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User_id in header wasn't an number");
        }
        //adding values
        try {
            Survey toRet = surveyDAO.addSurveyToUser(user_id,data.get("title"), data.get("question"));
            return new ResponseEntity<>(toRet,HttpStatus.OK);
        } catch (EntityNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user at specified id!");
        }
    }

    @GetMapping("/survey")
    public ResponseEntity<List<Survey>> getAllSurveys() {
        List<Survey> allSurveys = surveyDAO.getAllSurveys();
        return new ResponseEntity<>(allSurveys, HttpStatus.OK);
    }

    @DeleteMapping("/survey/{ID}")
    public ResponseEntity<?> deleteSurvey(@PathVariable String ID) {
        int survey_id;
        try{
            survey_id = Integer.valueOf(ID);
        } catch (NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID wasn't an number");
        }
        try{
            surveyDAO.deleteSurvey(survey_id);
        } catch (EntityNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No survey at specified id");
        }

        return new ResponseEntity<>(HttpStatus.valueOf(204));
    }

    @GetMapping("/survey/{ID}")
    public ResponseEntity<Survey> getSurveyAtID(@PathVariable String ID) {
        //checking if ID is number
        int survey_id;
        try{
            survey_id = Integer.valueOf(ID);
        } catch (NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID wasn't an number");
        }
        //checking if exists
        Survey toRet = surveyDAO.getSurvey(survey_id);
        if(toRet == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No survey at specified ID");
        }

        return new ResponseEntity<>(toRet, HttpStatus.OK);
    }

    @PostMapping("/answer")
    public ResponseEntity<Answer> addAnswer(@RequestBody Map<String,Integer> requestBody, @RequestHeader(value = "user_id") String userId) {
        if(!requestBody.containsKey("survey_id") || !requestBody.containsKey("rating")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing survey_id and rating in request body!");
        }
        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No userId in header");
        }
        //checking userID
        int user_id;
        try{
            user_id = Integer.valueOf(userId);
        } catch (NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UserID wasn't an number");
        }

        int rating = requestBody.get("rating");
        if(rating < 0 || rating > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be at [0;5]");
        }

       try {
           Answer toRet = answerDAO.createAnswer(user_id, requestBody.get("survey_id"), rating);
           return new ResponseEntity<>(toRet,HttpStatus.OK);
       } catch (EntityNotFoundException exc) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getLocalizedMessage());
       } catch (IllegalArgumentException exc) {
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User has already answered to that survey!");
       }

    }

    @GetMapping("/answer/{ID}")
    public ResponseEntity<Answer> getAnswerAtID(@PathVariable String ID) {
        int answer_id;
        try{
            answer_id = Integer.valueOf(ID);
        } catch (NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID wasn't an number");
        }
        //checking if exists
        Answer answer = answerDAO.getAnswerAtID(answer_id);
        if(answer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No answer at specified ID");
        }

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    @PutMapping("/answer/{ID}")
    public ResponseEntity<Answer> modifyAnswer(@RequestBody Map<String, Integer> responseBody, @PathVariable String ID, @RequestHeader(value = "user_id") String user_id) {
        if(!responseBody.containsKey("rating")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No rating in responseBody");
        }

        if(user_id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user_id in header");
        }
        int userId;
        try {
            userId = Integer.valueOf(user_id);
        } catch(NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User_id wasn't an number!");
        }

        int answerId;
        try {
            answerId = Integer.valueOf(ID);
        } catch(NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Answer ID wasn't an number");
        }

        int rating = responseBody.get("rating");
        if(rating < 0 || rating > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be at [0;5]");
        }

        try {
            Answer answer = answerDAO.modifyAnswer(userId, answerId, rating);
            return new ResponseEntity<>(answer,HttpStatus.OK);
        } catch (EntityNotFoundException exc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getLocalizedMessage());
        }
    }

    @GetMapping("/stats/user/{id}")
    public ResponseEntity<Map<String,Object>> getUserStats(@PathVariable String id) {
        int user_id;
        try{
            user_id = Integer.valueOf(id);
        } catch(NumberFormatException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID wasn't an number");
        }
        User user = userDAO.getUser(user_id);
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user at specified id");
        }
        Map<String, Object> toRet = new HashMap<>();
        toRet.put("Number of created surveys: ", user.getSurveys().size());

        Map<Integer, AnswerStats> answerStatsMap = new HashMap<>();

        for(Survey survey : user.getSurveys()) {
            AnswerStats stats = new AnswerStats(survey.getTitle());
            for(Answer answer : survey.getAnswers()) {
                stats.addRating(answer.getRating());
            }
            answerStatsMap.put(survey.getId(), stats);
        }

        toRet.put("Surveys stats: ", answerStatsMap);

        return new ResponseEntity<>(toRet, HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String,Object>> getStats() {
        Map<String, Object> toRet = new HashMap<>();
        toRet.put("Created surveys amount rank",surveyDAO.getSurveyRankByCreatedAmount());
        toRet.put("Answer amount survey rank", surveyDAO.getSurveyRankByAnswerAmount());
        toRet.put("Average survey amount by user", surveyDAO.getAvgSurveyByUser());
        toRet.put("Average answer amount by survey", surveyDAO.getAvgSurveyByAnswer());
        toRet.put("All surveys amount", surveyDAO.getAllSurveys().size());
        return new ResponseEntity<>(toRet,HttpStatus.OK);
    }

    private static class AnswerStats {
        private String survey_title;
        private int answers_count;
        @JsonIgnore
        private int sumOfRatings;
        private float ratingAvg;

        public AnswerStats(String survey_title) {
            this.survey_title = survey_title;
        }

        public int getAnswers_count() {
            return answers_count;
        }

        public void setAnswers_count(int answers_count) {
            this.answers_count = answers_count;
        }

        public int getSumOfRatings() {
            return sumOfRatings;
        }

        public void setSumOfRatings(int sumOfRatings) {
            this.sumOfRatings = sumOfRatings;
        }

        public float getRatingAvg() {
            return ratingAvg;
        }

        public void setRatingAvg(float ratingAvg) {
            this.ratingAvg = ratingAvg;
        }

        public String getSurvey_title() {
            return survey_title;
        }

        public void setSurvey_title(String survey_title) {
            this.survey_title = survey_title;
        }

        public void addRating(int rating) {
            ++this.answers_count;
            this.sumOfRatings += rating;
            this.ratingAvg = ((float)this.sumOfRatings)/((float)this.answers_count);
        }
    }

}
