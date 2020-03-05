package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainFrameModel {
    private static final ObservableList<Task> TO_DO_LIST = FXCollections.observableArrayList();
    private static final ObservableList<Task> IN_PROGRESS_LIST = FXCollections.observableArrayList();
    private static final ObservableList<Task> DONE_LIST = FXCollections.observableArrayList();

    public static ObservableList<Task> getToDoList() {
        return TO_DO_LIST;
    }

    public static ObservableList<Task> getInProgressList() {
        return IN_PROGRESS_LIST;
    }

    public static ObservableList<Task> getDoneList() {
        return DONE_LIST;
    }

}
