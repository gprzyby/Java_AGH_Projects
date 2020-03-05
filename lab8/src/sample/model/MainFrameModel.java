package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static void clearAllLists() {
        TO_DO_LIST.clear();
        IN_PROGRESS_LIST.clear();
        DONE_LIST.clear();
    }

    public enum ListID {
        TO_DO("To Do",TO_DO_LIST),
        IN_PROGRESS("In Progress", IN_PROGRESS_LIST),
        DONE("Done",DONE_LIST);

        private String key;
        private ObservableList<Task> list;

        ListID(String key, ObservableList<Task> list) {
            this.key = key;
            this.list = list;
        }

        public String getKey() {
            return key;
        }

        public ObservableList<Task> getList() {
            return list;
        }

        public static ObservableList<Task> getListFromID(String key) {
            for(ListID obj : ListID.values()) {
                if(key.equals(obj.key)) return obj.list;
            }
            return null;
        }

        public static ListID getInstance(String key) {
            for(ListID obj : ListID.values()) {
                if(key.equals(obj.key)) return obj;
            }
            return null;
        }
    }

}
