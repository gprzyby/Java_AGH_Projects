package edu.sixstepteory.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jgrapht.graph.DefaultEdge;

import java.io.IOException;
import java.util.List;

public class Movie extends DefaultEdge implements Cloneable{
    private String id;
    private String title;
    private List<Actor> actors;

    public Movie() {
    }

    public Movie(String id, String name, List<Actor> actorList) {
        this.id = id;
        this.title = name;
        this.actors = actorList;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public static Movie deserializeFromJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json,Movie.class);
    }

    public static Movie[] deserializeFromJSONToTable(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json,Movie[].class);
    }

    @Override
    public Object clone() {
        if(this.actors == null) {
            return new Movie(this.id, this.title, null);
        }
        return new Movie(this.id,this.title,List.copyOf(this.actors));
    }

    @Override
    public String toString() {
        return title + '(' + id + ')';
    }

    public static void main(String[] args) {
        try {
            Movie mov = Movie.deserializeFromJSON("{\"id\":\"tt0092967\",\"title\":\"End of the Line\",\"actors\":[{\"id\":\"nm0000102\",\"name\":\"Kevin Bacon\"},{\"id\":\"nm0004729\",\"name\":\"Michael Beach\"},{\"id\":\"nm0057363\",\"name\":\"Barbara Barrie\"},{\"id\":\"nm0000837\",\"name\":\"Bob Balaban\"}]}");
            Movie mov2 = Movie.deserializeFromJSON("{\"id\":\"tt0092960\",\"title\":\"Emilio Varela vs. Camelia la Texana\",\"actors\":[{\"id\":\"nm0021728\",\"name\":\"Mario Almada\"},{\"id\":\"nm0525231\",\"name\":\"Alejandro Lugo\"},{\"id\":\"nm0165490\",\"name\":\"Samuel Claxton\"},{\"id\":\"nm0306308\",\"name\":\"Luis Alberto García\"},{\"id\":\"nm0106083\",\"name\":\"Irela Bravo\"},{\"id\":\"nm0050966\",\"name\":\"Mario Balmaseda\"}]}");

            System.out.println(mov + "\n\n" + mov2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
