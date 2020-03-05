package edu.sixstepteory.model;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class PathFindingThread extends Task<Queue<MainFrameModel.TableNode>> {
    private Graph<Actor, Movie> graph = new SimpleGraph<>(Movie.class);
    private TextArea textAreaRef;
    private Queue<Actor> lastAddedActorsFromSource = new LinkedList<>();
    private Queue<Actor> lastAddedActorsFromDestination = new LinkedList<>();
    private Map<Integer, Actor> verticesFromSource = new HashMap<>();
    private Map<Integer, Actor> verticesFromDestination = new HashMap<>();
    private Actor sourceActor;
    private Actor destinationActor;
    private UpdatingDirection direction = UpdatingDirection.FROM_SOURCE;
    private int levels = 0;
    public PathFindingThread(String sourceActorID, String destinationActorID, TextArea textArea) {
        if(sourceActorID.equals(destinationActorID)) throw new IllegalArgumentException("Source and destination Actors are the same");
        this.sourceActor = new Actor(sourceActorID,"");
        this.destinationActor = new Actor(destinationActorID,"");
        this.textAreaRef = textArea;
    }

    public PathFindingThread(Actor sourceActor, Actor destinationActor, TextArea textArea) {
        if(sourceActor.equals(destinationActor)) throw new IllegalArgumentException("Source and destination Actors are the same");
        this.destinationActor = destinationActor;
        this.sourceActor = sourceActor;
        this.textAreaRef = textArea;
    }

    @Override
    protected Queue<MainFrameModel.TableNode> call() throws Exception {
        //Check if complete source actor and destination actor was given as parameter
        if(sourceActor.getName().isEmpty() || destinationActor.getName().isEmpty()) {
            this.destinationActor = HttpConnection.getInstance().getActorByID(destinationActor.getId());
            this.sourceActor = HttpConnection.getInstance().getActorByID(sourceActor.getId());
        }

        //adding source and destination actors to graph and queues
        this.lastAddedActorsFromDestination.add(destinationActor);
        this.lastAddedActorsFromSource.add(sourceActor);
        this.verticesFromDestination.put(destinationActor.hashCode(),destinationActor);
        this.verticesFromSource.put(sourceActor.hashCode(),sourceActor);
        graph.addVertex(destinationActor);
        graph.addVertex(sourceActor);
        if(HttpConnection.getInstance().getActorMovies(sourceActor.getId()).length == 0 || HttpConnection.getInstance().getActorMovies(destinationActor.getId()).length == 0) throw new IllegalArgumentException("One of actors haven't played in any movie");

        boolean connectionReached = false;
        makingConnectionLabel:
        while(!connectionReached) {
            long maxProgress = 0L;
            updateProgress(0L,1L);
            switch (direction)
            {
                case FROM_SOURCE: {
                    Platform.runLater(() -> textAreaRef.appendText("\nSource path checking lvl:" + (levels + 1)));
                    maxProgress = lastAddedActorsFromSource.size();
                    for(int i=lastAddedActorsFromSource.size();i > 0; --i) {
                        Actor prevActor = lastAddedActorsFromSource.poll();
                        Movie[] sourceMovies = HttpConnection.getInstance().getActorMovies(prevActor.getId());
                        for (Movie mov : sourceMovies) {
                            Movie tmp = HttpConnection.getInstance().getMovieByID(mov.getId());
                            for(Actor actor : tmp.getActors()) {
                                if(actor.equals(prevActor)) continue;
                                graph.addVertex(actor);
                                verticesFromSource.put(actor.hashCode(),actor);
                                graph.addEdge(prevActor,actor,(Movie)mov.clone());
                                if ( verticesFromDestination.containsKey(actor.hashCode()) ) {
                                    connectionReached = true;
                                    break makingConnectionLabel;
                                }
                                lastAddedActorsFromSource.add(actor);
                            }
                        }
                        updateProgress(maxProgress - i,maxProgress);
                    }
                    this.direction = UpdatingDirection.FROM_DESTINATION;
                    break;
                }
                case FROM_DESTINATION: {
                    Platform.runLater(() -> textAreaRef.appendText("\nDestination path checking lvl:" + (levels + 1)));
                    maxProgress = lastAddedActorsFromDestination.size();
                    for(int i = lastAddedActorsFromDestination.size(); i > 0; --i) {
                        Actor prevActor = lastAddedActorsFromDestination.poll();
                        Movie[] sourceMovies = HttpConnection.getInstance().getActorMovies(prevActor.getId());
                        for (Movie mov : sourceMovies) {
                            Movie tmp = HttpConnection.getInstance().getMovieByID(mov.getId());
                            for(Actor actor : tmp.getActors()) {
                                if(actor.equals(prevActor)) continue;
                                graph.addVertex(actor);
                                graph.addEdge(prevActor,actor,(Movie)mov.clone());
                                if (verticesFromSource.containsKey(actor.hashCode())) {
                                    connectionReached = true;
                                    break makingConnectionLabel;
                                }
                                lastAddedActorsFromDestination.add(actor);
                                verticesFromDestination.put(actor.hashCode(),actor);
                            }
                        }
                        updateProgress(maxProgress - i,maxProgress);
                    }
                    this.direction = UpdatingDirection.FROM_SOURCE;
                    break;
                }
            }

            ++levels;
        }
        Platform.runLater(() -> textAreaRef.appendText("\nConnection found"));
        BellmanFordShortestPath<Actor, Movie> pathFindingAlgorithm = new BellmanFordShortestPath<>(this.graph);
        GraphPath<Actor, Movie> finalPath = pathFindingAlgorithm.getPath(this.sourceActor,this.destinationActor);
        Queue<MainFrameModel.TableNode> toRet = new LinkedList<>();
        Actor previous = this.sourceActor;
        for(int i = 1; i< finalPath.getVertexList().size(); ++i) {
            toRet.add(new MainFrameModel.TableNode(previous,finalPath.getVertexList().get(i),finalPath.getEdgeList().get(i-1)));
            previous = finalPath.getVertexList().get(i);
        }
        updateProgress(1.0D,1.0D);
        return toRet;
    }


    private enum UpdatingDirection {
        FROM_SOURCE,FROM_DESTINATION;
    }
}
