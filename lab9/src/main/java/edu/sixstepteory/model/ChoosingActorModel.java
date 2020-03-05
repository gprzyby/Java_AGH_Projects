package edu.sixstepteory.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class ChoosingActorModel implements ChoosingModel<Actor>{
    private final ObservableList<Actor> destinationActors = FXCollections.observableArrayList();
    private final ObservableList<Actor> sourceActors = FXCollections.observableArrayList();

    public ChoosingActorModel() {
    }

    @Override
    public ObservableList<Actor> getDestinationListModel() {
        return destinationActors;
    }

    @Override
    public ObservableList<Actor> getSourceListModel() {
        return sourceActors;
    }
}
