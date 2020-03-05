package edu.sixstepteory.controller;

import edu.sixstepteory.model.Actor;
import edu.sixstepteory.model.HttpConnection;
import edu.sixstepteory.model.MainFrameModel;
import edu.sixstepteory.model.PathFindingThread;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

public class MainFrameController {
    @FXML
    public Button findByIDButton;
    @FXML
    private TextField textStartActor;
    @FXML
    private TextField textEndActor;
    @FXML
    private Button findButton;
    @FXML
    private TextArea msgBox;
    @FXML
    private TableColumn<MainFrameModel.TableNode, String> columnCurrActor;
    @FXML
    private TableColumn<MainFrameModel.TableNode, String> columnPrevActor;
    @FXML
    private TableColumn<MainFrameModel.TableNode, String> columnMovie;
    @FXML
    private TableView tableView;
    @FXML
    private ProgressBar executionProgressBar;
    private Task<?> executionTask;


    @FXML
    public void initialize() {
        columnCurrActor.setCellValueFactory(new PropertyValueFactory<MainFrameModel.TableNode, String>("currentActor"));
        columnPrevActor.setCellValueFactory(new PropertyValueFactory<MainFrameModel.TableNode, String>("prevActor"));
        columnMovie.setCellValueFactory(new PropertyValueFactory<MainFrameModel.TableNode, String>("metInMovie"));
        tableView.setItems(MainFrameModel.getInstance().getTableNodes());

    }

    public void startFinding(ActionEvent actionEvent) {
        try {
            ChoosingActorController.create(textStartActor.getText(),textEndActor.getText(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute(Actor start, Actor finish) {
        PathFindingThread findingThread = new PathFindingThread(start.getId(),finish.getId(),msgBox);
        this.executionTask = findingThread;
        findButton.setDisable(true);
        findByIDButton.setDisable(true);
        findingThread.setOnSucceeded((event) -> {
            try {
                Queue<MainFrameModel.TableNode> ret = findingThread.get();
                MainFrameModel.getInstance().getTableNodes().clear();
                MainFrameModel.getInstance().getTableNodes().addAll(ret);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            findButton.setDisable(false);
            findByIDButton.setDisable(false);
        });
        findingThread.setOnFailed(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Throwable exc = findingThread.getException();
            alert.setTitle("Error");
            alert.setHeaderText("Exception throwed by path finding thread");
            alert.setContentText(exc.getMessage());
            alert.showAndWait();
            executionProgressBar.setProgress(1.0D);
            findButton.setDisable(false);
            findByIDButton.setDisable(false);
        });
        executionProgressBar.progressProperty().bind((findingThread.progressProperty()));
        Thread thread = new Thread(findingThread);
        thread.setDaemon(true);
        thread.start();
    }

    public void findByID(ActionEvent actionEvent) {
        execute(new Actor(textStartActor.getText(),""), new Actor(textEndActor.getText(),""));
    }
}
