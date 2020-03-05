package edu.sixstepteory.model;

import okhttp3.Callback;
import java.io.IOException;

public interface HttpRequests {
    Actor getActorByID(int id) throws IOException;
    Actor getActorByID(String id) throws IOException;
    Movie[] getActorMovies(int id) throws IOException;
    Movie[] getActorMovies(String id) throws IOException;
    Movie getMovieByID(String id) throws IOException;
    public Movie getMovieByID(int id) throws IOException;
    public Actor getActorByPattern(String pattern) throws IOException;
    public void getActorsByPattern(String pattern, Callback responseHandler);
}
