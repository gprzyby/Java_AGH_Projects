package edu.gprzyb.lab11_hibernate_final.dao;

import edu.gprzyb.lab11_hibernate_final.entites.Survey;
import edu.gprzyb.lab11_hibernate_final.entites.User;

import java.util.List;
import java.util.Set;

public interface UserDAO {
    List<User> getAllUsers();
    User getUser(Integer id);
    boolean checkIfUserExists(Integer id);
    User createUser(String name);
    Set<Survey> getUserSurveys(Integer id);


}
