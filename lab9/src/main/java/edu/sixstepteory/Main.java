package edu.sixstepteory;

import edu.sixstepteory.model.Movie;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.graph.SimpleGraph;

import java.io.IOException;
import java.util.Collection;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainFrameView.fxml"));
        primaryStage.setTitle("Six step theory tester");
        primaryStage.setScene(new Scene(root, 740, 490));
        primaryStage.show();
    }

    private static void jGraphTest() {
        Graph<Integer, String> graph = new SimpleGraph<Integer,String>(String.class);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1,2,"cos");
        System.out.println(new Movie("a","cos",null).hashCode());
        System.out.println(new Movie("a","cos",null).hashCode());
        System.out.println(graph.addEdge(2,3,"cos"));
        BellmanFordShortestPath<Integer, String> path = new BellmanFordShortestPath<>(graph);
        GraphPath<Integer, String> shortestPath = path.getPath(1,3);
        System.out.println(shortestPath);
    }
}
