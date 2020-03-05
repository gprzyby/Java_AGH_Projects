CREATE TABLE location (
                          location_id INT NOT NULL AUTO_INCREMENT,
                          lon FLOAT,
                          lat FLOAT,
                          city_name VARCHAR(255),
                          update_time_sec INT,
                          CONSTRAINT pk_location PRIMARY KEY(location_id)
);

CREATE TABLE measurement (
                             measurement_id INT NOT NULL AUTO_INCREMENT,
                             location_id INT NOT NULL,
                             temp FLOAT,
                             date TIMESTAMP,
                             CONSTRAINT pk_measurement PRIMARY KEY(measurement_id),
                             CONSTRAINT fk_measurement_location FOREIGN KEY(location_id) REFERENCES location(location_id)
);