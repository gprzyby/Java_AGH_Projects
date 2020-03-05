CREATE TABLE answer (
    answer_id int NOT NULL AUTO_INCREMENT,
    survey_id int NOT NULL,
    user_id int NOT NULL,
    rating int,
    CONSTRAINT answer_pk PRIMARY KEY(answer_id),
    CONSTRAINT answer_user_fk FOREIGN KEY(user_id) REFERENCES user(user_id),
    CONSTRAINT answer_survey_fk FOREIGN KEY(survey_id) REFERENCES survey(survey_id)
);

CREATE TABLE survey(
                       survey_id INT NOT NULL AUTO_INCREMENT,
                       survey_title VARCHAR(511),
                       question VARCHAR(511),
                       user_id INT NOT NULL,
                       CONSTRAINT survey_pk PRIMARY KEY(survey_id),
                       CONSTRAINT survey_user_fk FOREIGN KEY(user_id) REFERENCES user(user_id)
);

CREATE TABLE user(
    user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    CONSTRAINT user_pk PRIMARY KEY(user_id)
);

INSERT INTO user(username) VALUES ("Admin");
INSERT INTO survey(question, survey_title, user_id) VALUES("Jak Pan/Pani ocenia wyroby firmy COS", "Badanie opinii publicznej na temat wyrobow firmy COS", 1);
INSERT INTO answer(rating, survey_id, user_id) VALUES(5,1,1);
