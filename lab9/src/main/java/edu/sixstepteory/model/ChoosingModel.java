package edu.sixstepteory.model;

import javafx.collections.ObservableList;

public interface ChoosingModel<T> {
    ObservableList<T> getSourceListModel();
    ObservableList<T> getDestinationListModel();

}
