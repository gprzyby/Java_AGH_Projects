package edu.grzprzy.lab12_microservices.tables;

import javax.persistence.*;

@Entity
@Table(name = "location")
public class Location {

    @Id
    @Column(name = "location_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lon")
    private Float lon;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "city_name")
    private String city_name;

    @Column(name = "update_time_sec")
    private Integer update_freq;

    public Location() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public Integer getUpdate_freq() {
        return update_freq;
    }

    public void setUpdate_freq(Integer update_freq) {
        this.update_freq = update_freq;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", city_name='" + city_name + '\'' +
                ", update_freq=" + update_freq +
                '}';
    }
}
