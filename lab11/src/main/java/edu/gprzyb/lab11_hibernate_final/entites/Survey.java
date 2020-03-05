package edu.gprzyb.lab11_hibernate_final.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "survey")
public class Survey {

    @Id
    @Column(name = "survey_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "survey_title")
    private String title;

    @Column(name = "question")
    private String question;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = true, updatable = true, nullable = false)
    private User user;

    @OneToMany(targetEntity = Answer.class, mappedBy = "survey", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Answer> answers;

    public Survey() {
    }

    public Survey(String title, String question, User user) {
        this.title = title;
        this.question = question;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}
