package edu.sixstepteory.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

public class MainFrameModel {
    private static MainFrameModel instance = new MainFrameModel();
    private ObservableList<TableNode> tableNodes = FXCollections.observableArrayList();

    public static MainFrameModel getInstance() {
        return instance;
    }

    public ObservableList<TableNode> getTableNodes() {
        return tableNodes;
    }

    public static class TableNode {
        private Actor prevActor;
        private Actor currentActor;
        private Movie metInMovie;

        public TableNode(Actor prevActor, Actor currentActor, Movie metInMovie) {
            this.prevActor = prevActor;
            this.currentActor = currentActor;
            this.metInMovie = metInMovie;
        }

        public Actor getPrevActor() {
            return prevActor;
        }

        public void setPrevActor(Actor prevActor) {
            this.prevActor = prevActor;
        }

        public Actor getCurrentActor() {
            return currentActor;
        }

        public void setCurrentActor(Actor currentActor) {
            this.currentActor = currentActor;
        }

        public Movie getMetInMovie() {
            return metInMovie;
        }

        public void setMetInMovie(Movie metInMovie) {
            this.metInMovie = metInMovie;
        }
    }
}
