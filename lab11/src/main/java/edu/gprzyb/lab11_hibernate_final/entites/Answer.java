package edu.gprzyb.lab11_hibernate_final.entites;

import javax.persistence.*;

@Entity
@Table(name = "answer")
public class Answer {

    @Id
    @Column(name = "answer_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "survey_id", insertable = true, updatable = true, nullable = false)
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = true, updatable = true, nullable = false)
    private User user;

    @Column(name = "rating")
    private Integer rating;

    public Answer() {
    }

    public Answer(Survey survey, User user, Integer rating) {
        this.survey = survey;
        this.user = user;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
