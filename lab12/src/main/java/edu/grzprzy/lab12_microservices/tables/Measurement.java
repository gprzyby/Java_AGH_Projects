package edu.grzprzy.lab12_microservices.tables;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "measurement")
public class Measurement {

    @Id
    @Column(name = "measurement_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "temp")
    private Float temperature;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date measure_date;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    public Measurement() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Date getMeasure_date() {
        return measure_date;
    }

    public void setMeasure_date(Date measure_date) {
        this.measure_date = measure_date;
    }

    public Measurement(Float temperature, Date measure_date, Location location) {
        this.temperature = temperature;
        this.measure_date = measure_date;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", temperature=" + temperature +
                ", measure_date=" + measure_date +
                ", location=" + location +
                '}';
    }
}
