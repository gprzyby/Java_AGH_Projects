package edu.sixstepteory.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import edu.sixstepteory.Main;
import edu.sixstepteory.model.Actor;
import edu.sixstepteory.model.ChoosingActorModel;
import edu.sixstepteory.model.ChoosingModel;
import edu.sixstepteory.model.HttpConnection;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

public class ChoosingActorController {
    @FXML
    public ProgressIndicator progress;
    @FXML
    private ListView<Actor> listSource;
    @FXML
    private ListView<Actor> listDestination;
    @FXML
    private Button executionButton;
    private ChoosingModel<Actor> model;
    private Stage secondStage;
    private MainFrameController rootController;

    private void setModel(ChoosingModel<Actor> model) {
        listDestination.setItems(model.getDestinationListModel());
        listSource.setItems(model.getSourceListModel());
        this.model = model;
    }

    private void setRootController(MainFrameController controller) {
        this.rootController = controller;
    }

    public synchronized void addHalfProgress() {
        progress.setProgress(progress.getProgress() + 0.5D);
    }

    public ListView<Actor> getListSource() {
        return listSource;
    }

    public ListView<Actor> getListDestination() {
        return listDestination;
    }

    public ChoosingModel<Actor> getModel() {
        return model;
    }

    private void setSecondStage(Stage stage) {
        this.secondStage = stage;
    }

    private void searchSourceActor(String pattern) {
        HttpConnection.getInstance().getActorsByPattern(pattern,new ActorsFinder(model.getSourceListModel(),pattern));
    }

    private void searchDestinationActor(String pattern) {
        HttpConnection.getInstance().getActorsByPattern(pattern,new ActorsFinder(model.getDestinationListModel(),pattern));
    }

    public static void create(String sourceActorPattern, String destinationActorPattern, MainFrameController rootController) throws IOException {
        //loading fxml from resource folder
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/view/ChoosingActorView.fxml"));
        Parent choosingPane = loader.load();
        //setting up window
        Stage secondStage = new Stage();
        secondStage.setTitle("Actor chooser");
        secondStage.setResizable(false);
        secondStage.initModality(Modality.WINDOW_MODAL);
        Scene sceneOfPane = new Scene(choosingPane);
        secondStage.setScene(sceneOfPane);
        //initializing callbacks to fill up controller fields
        ChoosingActorController controller = loader.getController();
        controller.setSecondStage(secondStage);
        controller.setModel(new ChoosingActorModel());
        controller.setRootController(rootController);
        controller.searchSourceActor(sourceActorPattern);
        controller.searchDestinationActor(destinationActorPattern);
        //showing window
        secondStage.showAndWait();
    }

    public void executionPathFinding(ActionEvent actionEvent) {
        Actor sourceActor = listSource.getSelectionModel().getSelectedItem();
        Actor destinationActor = listDestination.getSelectionModel().getSelectedItem();
        if(sourceActor == null || destinationActor == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error occurred while executing path");
            alert.setContentText("One of list items hasn't been selected");
            alert.showAndWait();
            return;
        }

        rootController.execute(sourceActor,destinationActor);
        secondStage.close();

    }

    private void showError(String errorMessage, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(errorMessage);
        alert.showAndWait();
        secondStage.close();
    }

    private class ActorsFinder implements Callback {
        private final ObservableList<Actor> list;
        private String actorPattern;

        public ActorsFinder(ObservableList<Actor> list, String actorPattern) {
            this.list = list;
            this.actorPattern = actorPattern;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Problem occurred while searching actor: " + actorPattern);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseBody = response.body().string();
            try {
                Actor[] actors = Actor.deserializeFromJSONToTable(responseBody);
                ChoosingActorController.this.addHalfProgress();
                list.addAll(actors);
            } catch (JsonParseException exc) {
                Platform.runLater(() -> showError(exc.getMessage(),"Invalid return type"));
            }

        }
    }
}
