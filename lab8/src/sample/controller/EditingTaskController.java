package sample.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import sample.Main;
import sample.model.DateFormatter;
import sample.model.Priority;
import sample.model.Task;

import java.io.IOException;
import java.time.format.DateTimeParseException;

public class EditingTaskController {
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField deadLineTextBox;
    @FXML
    private ComboBox<Priority> priorityComboBox;
    @FXML
    private TextArea descriptionTextField;
    @FXML
    private Button execButton;

    private Stage dialogStage;
    private Task task;

    @FXML
    protected void initialize() {
        priorityComboBox.setItems(FXCollections.observableArrayList(Priority.values()));
        priorityComboBox.setCellFactory((e) -> new ComboBoxElementView());
        deadLineTextBox.setTooltip(new Tooltip("Date in format" + DateFormatter.getDataFormat()));
        deadLineTextBox.setPromptText(DateFormatter.getDataFormat());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void createEditingTaskAnchorPane(Task taskToEdit) {
        this.task = taskToEdit;
        //parsing all data to editor elements
        titleTextField.setText(taskToEdit.getTitle());
        priorityComboBox.setValue(taskToEdit.getPriority());
        deadLineTextBox.setText(DateFormatter.parseToString(taskToEdit.getDate()));
        execButton.setText("Edit");
        descriptionTextField.setText(taskToEdit.getDescription());
    }

    private void createNewTaskAnchorPane() {
        this.task = null;
        execButton.setText("Add new task");
        priorityComboBox.setValue(Priority.MEDIUM);
    }

    public static void createNewTaskAnchor() {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(Main.class.getResource("view/EditingTaskView.fxml"));
            SplitPane pane = (SplitPane)loader.load();

            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Create new task");
            secondaryStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(pane);
            secondaryStage.setScene(scene);

            EditingTaskController controller = loader.getController();
            controller.createNewTaskAnchorPane();
            controller.setDialogStage(secondaryStage);
            secondaryStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createEditTaskAnchor(Task toEdit) {
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(Main.class.getResource("view/EditingTaskView.fxml"));
            SplitPane pane = (SplitPane)loader.load();

            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Create new task");
            secondaryStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(pane);
            secondaryStage.setScene(scene);

            EditingTaskController controller = loader.getController();
            controller.createEditingTaskAnchorPane(toEdit);
            controller.setDialogStage(secondaryStage);
            secondaryStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String checkSyntax() {
        //Firstly check if text boxes are empty
        if(titleTextField.getText().isEmpty()) return "Title text is empty";
        if(deadLineTextBox.getText().isEmpty()) return "Dead line text is empty";
        if(descriptionTextField.getText().isEmpty()) return "Description text is empty";

        //checking if data format is correct
        try{
            DateFormatter.parseToDate(deadLineTextBox.getText());
        } catch (DateTimeParseException exc) {
            return "Data format is invalid. Data format is: " + DateFormatter.getDataFormat();
        }

        return null;
    }

    public void execButtonAction(ActionEvent actionEvent) {
        //firstly check is syntax is valid
        String syntaxErrorMessage = this.checkSyntax();
        if(this.checkSyntax() != null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(syntaxErrorMessage);
            alert.setHeaderText("Error in parsing data");
            alert.showAndWait();
            return;
        }

        //checking type of editing task
        if(task == null) {
            sample.model.MainFrameModel.getToDoList().add(new Task( titleTextField.getText(),
                                                                    priorityComboBox.getValue(),
                                                                    deadLineTextBox.getText(),
                                                                    descriptionTextField.getText()));
        } else {
            this.task.setDate(deadLineTextBox.getText());
            this.task.setDescription(descriptionTextField.getText());
            this.task.setTitle(titleTextField.getText());
            this.task.setPriority(priorityComboBox.getValue());
        }
        dialogStage.close();
    }

    private static class ComboBoxElementView extends ComboBoxListCell<Priority> {
        @Override
        public void updateItem(Priority item, boolean empty) {
            super.updateItem(item, empty);
            if(item != null) {
                Rectangle rect = new Rectangle(10,10);
                rect.setFill(item.getColor());
                setTextAlignment(TextAlignment.LEFT);
                setText(item.getPriorityName());
                setGraphic(rect);
            }
        }
    }
}
