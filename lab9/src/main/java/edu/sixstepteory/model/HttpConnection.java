package edu.sixstepteory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HttpConnection implements HttpRequests{
    private static final HttpConnection instance = new HttpConnection();
    private static final OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES).readTimeout(1,TimeUnit.MINUTES).callTimeout(1, TimeUnit.MINUTES).build();
    private static final String serverURL = "https://java.kisim.eu.org/";

    public static HttpConnection getInstance() {
        return instance;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public String getRequest(String url) throws IOException {
        Request request = new Request.Builder().url(serverURL + url).build();
        Call call = client.newCall(request);
        long timeBeforeExec = System.nanoTime();
        try(Response response = call.execute()) {
            System.out.println(System.nanoTime() - timeBeforeExec);
            if(response.code() != 200) throw new IOException("Server didn't response with 200(OK) code but:" + response.code());
            return response.body().string();
        }
    }

    public void getRequest(String url, Callback ret) {
        Request request = new Request.Builder().url(serverURL + url).build();
        Call call = client.newCall(request);
        call.enqueue(ret);
    }

    public Actor getActorByID(String id) throws IOException {
        return Actor.deserializeFromJSON(getRequest("actors/" + id));
    }

    public Actor getActorByID(int id) throws IOException {
        return Actor.deserializeFromJSON(getRequest("actors/nm" + String.format( "%07d", id)));
    }

    public Movie getMovieByID(int id) throws IOException {
        return Movie.deserializeFromJSON(getRequest("movies/tt" + String.format("%07d", id)));
    }

    public Actor getActorByPattern(String pattern) throws IOException {
        Actor[] actors = Actor.deserializeFromJSONToTable(getRequest("actors/search/" + pattern));
        for(Actor act : actors) {
            if(getActorMovies(act.getIDInNumber()).length != 0) {
                return act;
            }
        }
        return null;
    }

    @Override
    public void getActorsByPattern(String pattern, Callback responseHandler) {
        getRequest("/actors/search/" + pattern, responseHandler);
    }


    public Movie[] getActorMovies(int id) throws IOException {
        return Movie.deserializeFromJSONToTable(getRequest("actors/nm" + String.format("%07d", id) + "/movies"));
    }

    public Movie[] getActorMovies(String id) throws IOException {
        return Movie.deserializeFromJSONToTable(getRequest("actors/" + id + "/movies"));
    }

    public Movie getMovieByID(String id) throws IOException {
        return Movie.deserializeFromJSON(getRequest("movies/" + id));

    }


    private HttpConnection() {
    }

    public static void main(String[] args) {
        try {
            HttpConnection.getInstance().getActorByID("nm0039162");
            final long time = System.nanoTime();
            HttpConnection.getInstance().getActorByID("nm0039162");
            HttpConnection.getInstance().getActorByID("nm0039162");
            HttpConnection.getInstance().getActorByID("nm0303472");
            HttpConnection.getInstance().getActorByID("nm0039162");
            HttpConnection.getInstance().getActorByID("nm0303472");
            System.out.println("Elapsed time: " + (System.nanoTime() - time) + "\nNow in async functions");
            final long time2 = System.nanoTime();
            Callback callback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.err.println("Exception occurred" + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println("Response time:" + (System.nanoTime() - time2));
                }
            };
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
