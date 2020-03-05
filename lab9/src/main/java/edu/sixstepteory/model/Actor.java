package edu.sixstepteory.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Objects;

public class Actor {
    private String id;
    private String name;

    Actor() {}

    public Actor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int getIDInNumber() {
        return Integer.valueOf(id.substring(2));
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(id, actor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + '(' + id + ')';
    }

    public static Actor deserializeFromJSON(String json) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json,Actor.class);
    }

    public static Actor[] deserializeFromJSONToTable(String json) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json,Actor[].class);
    }
}
