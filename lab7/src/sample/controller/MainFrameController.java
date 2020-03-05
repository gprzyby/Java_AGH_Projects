package sample.controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.model.CalculationThread;
import sample.model.Equation;
import sample.model.MainFrameModel;
import sample.model.MonteCarloHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MainFrameController {
    @FXML
    public TextField textAreaUpdateAmount;
    @FXML
    public TextField textAreaGenerateAmount;
    @FXML
    public Canvas drawingPane;
    @FXML
    public Button buttonRun;
    @FXML
    public Button buttonStop;
    @FXML
    public TextArea textAreaAnswer;
    @FXML
    public ProgressBar progressBar;
    public Button test;

    private CalculationThread calculationThread;

    public void buttonRun_OnAction(ActionEvent actionEvent) {

        try {
            int refreshPeriod = Integer.parseInt(textAreaUpdateAmount.getText());
            int pointsAmount = Integer.parseInt(textAreaGenerateAmount.getText());
            if (refreshPeriod > pointsAmount) refreshPeriod = pointsAmount;
            drawingPane.getGraphicsContext2D().restore();
            buttonRun.setDisable(true);
            buttonStop.setDisable(false);
            MonteCarloHandler monteCarloHandler = new MonteCarloHandler(MainFrameModel.minX, MainFrameModel.maxX,
                    MainFrameModel.minY, MainFrameModel.maxY, Equation::calc);

            calculationThread = new CalculationThread(monteCarloHandler, drawingPane.getGraphicsContext2D(),
                    pointsAmount, refreshPeriod, drawingPane.getWidth(), drawingPane.getHeight());

            calculationThread.setOnSucceeded(e ->
                Platform.runLater(() ->{
                    textAreaAnswer.setText("Result: " + calculationThread.getValue());
                    buttonRun.setDisable(false);
                    buttonStop.setDisable(true);
                })
            );


            progressBar.progressProperty().bind(calculationThread.progressProperty());
            Thread thread = new Thread(calculationThread);
            thread.setDaemon(true);
            thread.start();

        } catch(NullPointerException e) {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setTitle("Error");
            message.setContentText("Nothing was in text fields");
            message.setHeaderText("Error in getting values from text areas");
            message.showAndWait();
            return;
        } catch(NumberFormatException e) {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setTitle("Error");
            message.setContentText("Number format was invalid");
            message.setHeaderText("Error in getting values from text areas");
            message.showAndWait();
            return;
        }
    }

    public void buttonStop_OnAction(ActionEvent actionEvent) {
        textAreaAnswer.setText("Execution was stopped");
        buttonRun.setDisable(false);
        buttonStop.setDisable(true);
        calculationThread.cancel();

    }

}
