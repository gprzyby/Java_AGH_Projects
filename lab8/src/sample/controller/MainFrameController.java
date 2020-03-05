package sample.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MainFrameController {
    @FXML
    public Menu closeMenu;
    @FXML
    public Menu aboutMenu;
    @FXML
    public ListView listDone;
    @FXML
    public ListView listToDo;
    @FXML
    public ListView listInProgress;
    @FXML
    public Button buttonAddingTask;
    @FXML
    public MenuItem loadMenuItem;
    @FXML
    public MenuItem saveMenuItem;

    private Stage onCreatedStage;

    @FXML
    private void initialize() {
        MainFrameModel.getInProgressList().add(new Task("New task", Priority.LOW,"12.07.2019","Some kind of task to do"));
        MainFrameModel.getToDoList().add(new Task("New task",Priority.HIGH,"12.07.2018","Some kind of task to do"));

        listDone.setItems(MainFrameModel.getDoneList());
        listInProgress.setItems(MainFrameModel.getInProgressList());
        listToDo.setItems(MainFrameModel.getToDoList());

        listDone.setCellFactory(new ListElementCellFactory(MainFrameModel.getDoneList()));
        listInProgress.setCellFactory(new ListElementCellFactory(MainFrameModel.getInProgressList()));
        listToDo.setCellFactory(new ListElementCellFactory(MainFrameModel.getToDoList()));

        //setting drag handlers
        listToDo.setOnDragDetected(new DragDetectedHandler(listToDo));
        listDone.setOnDragDetected(new DragDetectedHandler(listDone));
        listInProgress.setOnDragDetected(new DragDetectedHandler(listInProgress));
        listToDo.setOnDragDropped(new DragDroppedHandler(listToDo));
        listDone.setOnDragDropped(new DragDroppedHandler(listDone));
        listInProgress.setOnDragDropped(new DragDroppedHandler(listInProgress));
        listToDo.setOnDragOver(this.onDragOverHandler);
        listDone.setOnDragOver(this.onDragOverHandler);
        listInProgress.setOnDragOver(this.onDragOverHandler);
    }
    
    public void exitMenuItemAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void aboutMenuItemAction(ActionEvent actionEvent) {
        //creating dialog popup showing version and author
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Info");
        info.setHeaderText("Information about this program");
        info.setContentText("Kanban application made by GP\nCracow 30.04.2019");
        info.showAndWait();
    }

    public void addingTaskAction(ActionEvent actionEvent) {
        EditingTaskController.createNewTaskAnchor();
    }

    public void saveMenuItemAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Save format", "*.csave"));
        File toSave = fileChooser.showSaveDialog(onCreatedStage);
        if(toSave == null) return;
        try {
            IOHandler.save(toSave);
        } catch (IOException exc) {
            Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
            errorPopUp.setTitle("Error");
            errorPopUp.setHeaderText("Error while saving to file");
            errorPopUp.setContentText(exc.getMessage());
            errorPopUp.showAndWait();
        }

    }

    public void loadMenuItemAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Save format", "*.csave"));
        File toLoad = fileChooser.showOpenDialog(onCreatedStage);
        if(toLoad == null) return;
        try {
            IOHandler.load(toLoad);
        } catch (IOException exc) {
            Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
            errorPopUp.setTitle("Error");
            errorPopUp.setHeaderText("Error while loading");
            errorPopUp.setContentText(exc.toString());
            errorPopUp.showAndWait();
        } catch (ClassNotFoundException e) {
            Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
            errorPopUp.setTitle("Error");
            errorPopUp.setHeaderText("Error while loading");
            errorPopUp.setContentText(e.toString());
            errorPopUp.showAndWait();
        }
    }

    public void setStage(Stage stage) {
        this.onCreatedStage = stage;
    }

    public void importMenuItemAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV format","*.csv"));
        File file = fileChooser.showOpenDialog(onCreatedStage);
        if(file == null) return;
        try {
            IOHandler.importFromCSV(file);
        } catch (FileNotFoundException e) {
            Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
            errorPopUp.setTitle("Error");
            errorPopUp.setHeaderText("Error while loading");
            errorPopUp.setContentText(e.getMessage());
            errorPopUp.showAndWait();
        } catch (CSVFromatException e) {
            Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
            errorPopUp.setTitle("Error");
            errorPopUp.setHeaderText("Error while loading");
            errorPopUp.setContentText(e.getMessage());
            errorPopUp.showAndWait();
        }

    }

    public void exportMenuItemAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV format","*.csv"));
        File file = fileChooser.showSaveDialog(onCreatedStage);
        if(file == null) return;
        try {
            IOHandler.exportToCSV(file);
        } catch (FileNotFoundException e) {
            Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
            errorPopUp.setTitle("Error");
            errorPopUp.setHeaderText("Error while saving to file");
            errorPopUp.setContentText(e.getMessage());
            errorPopUp.showAndWait();
        }
    }

    /**
     * Static inner class to handle menus for list view element
     */
    private static class ListElementCellFactory implements Callback<ListView<Task>, ListCell<Task>> {
        public final ObservableList<Task> list;

        public ListElementCellFactory(ObservableList<Task> list) {
            this.list = list;
        }

        @Override
        public ListCell<Task> call(ListView<Task> param) {
            ListCell<Task> listElement = new ListElementView();
            //creating menu
            ContextMenu menu = new ContextMenu();
            MenuItem edit = new MenuItem("Edit");
            edit.setOnAction((el) -> {
                EditingTaskController.createEditTaskAnchor(listElement.getItem());
                param.refresh();
            });
            MenuItem delete = new MenuItem("Delete");
            delete.setOnAction((el) -> {
                list.remove(listElement.getItem());
                param.refresh();
            });

            menu.getItems().addAll(edit, delete);

            listElement.emptyProperty().addListener(((observable, oldValue, newValue) -> {
                if(newValue) {
                    listElement.setContextMenu(null);
                } else {
                    listElement.setContextMenu(menu);
                    listElement.setTooltip(new Tooltip(listElement.getItem().getDescription()));
                }
            }));

            return listElement;
        }
    }

    /**
     * Static inner class to handling view of element in list
     */
    public static class ListElementView extends ListCell<Task> {
        @Override
        protected void updateItem(Task item, boolean empty) {
            super.updateItem(item, empty);
            Rectangle rectangle = new Rectangle(10,10);
            if(item != null) {
                rectangle.setFill(item.getPriority().getColor());
                setTextAlignment(TextAlignment.LEFT);
                setText(item.getTitle());
                setGraphic(rectangle);
            } else {
                rectangle.setFill(Color.WHITE);
                setTextAlignment(TextAlignment.LEFT);
                setText("");
                setGraphic(rectangle);
            }
        }
    }

    private class DragDetectedHandler implements EventHandler<MouseEvent> {
        private ListView<Task> list;

        DragDetectedHandler(ListView<Task> list) {
            this.list = list;
        }

        @Override
        public void handle(MouseEvent event) {
            System.out.println("Drag in list detected " + list.toString());
            Dragboard dragboard = list.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboard = new ClipboardContent();
            clipboard.put(Task.TASK_DATA_FORMAT,list.getSelectionModel().getSelectedItem());
            dragboard.setContent(clipboard);
            MainFrameController.dragSource = list;
        }
    }

    private class DragDroppedHandler implements EventHandler<DragEvent> {
        private ListView<Task> list;

        DragDroppedHandler(ListView<Task> list) {
            this.list = list;
        }

        @Override
        public void handle(DragEvent event) {
            System.out.println("Drag dropped in list detected " + list.toString());
            Task dragged = (Task) event.getDragboard().getContent(Task.TASK_DATA_FORMAT);
            list.getItems().add(dragged);
            try {
                ListView<Task> source = MainFrameController.dragSource;
                source.getItems().remove(dragged);
                source.refresh();
                list.refresh();
            } catch (ClassCastException exc) {
                exc.printStackTrace();
            }
            event.setDropCompleted(true);
        }
    }

    private EventHandler<DragEvent> onDragOverHandler = (DragEvent e) -> {
        System.out.println("Dragged over");
        e.acceptTransferModes(TransferMode.MOVE);
    };

    private static ListView<Task> dragSource = null;
}
