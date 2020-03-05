package sample.model;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class CalculationThread extends Task<Double> {
    private MonteCarloHandler monteCarloHandler;
    private final GraphicsContext g2D;
    private final int maxPoints;
    private final int pointsWhenRefreshGUI;
    private final double canvasWidth;
    private final double canvasHeight;

    public CalculationThread(MonteCarloHandler calculation, GraphicsContext graphics,
                             int maxPoints, int pointsWhenRefreshGUI, double canvasWidth, double canvasHeight) {
        this.g2D = graphics;
        this.monteCarloHandler = calculation;
        this.maxPoints = maxPoints;
        this.pointsWhenRefreshGUI = pointsWhenRefreshGUI;
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
    }

    @Override
    protected Double call() throws Exception {
        BufferedImage img = new BufferedImage((int)canvasWidth,(int)canvasHeight,BufferedImage.TYPE_INT_RGB);
        //defining scaling ratio for drawing
        double scaleXRatio = (-1.0D * canvasWidth)/((MainFrameModel.maxX - MainFrameModel.minX));
        double scaleYRatio = (-1.0D * canvasHeight)/((MainFrameModel.maxY - MainFrameModel.minY));
        double translateX = canvasWidth/2;
        double translateY = canvasHeight/2;

        //main for
        for(int i = 0; i<maxPoints;) {
            if(isCancelled()) break;
            for(int j = 0; j < pointsWhenRefreshGUI; ++j, ++i) {
                MonteCarloHandler.AnswerPoint point = monteCarloHandler.generatePoint();
                if (point.ifBelongToIntegrateArea()) {
                    img.setRGB((int) (point.getX() * scaleXRatio + translateX), (int) (point.getY() * scaleYRatio + translateY), Color.YELLOW.getRGB());
                } else {
                    img.setRGB((int) (point.getX() * scaleXRatio + translateX), (int) (point.getY() * scaleYRatio + translateY), new Color(25, 25, 100).getRGB());
                }
                updateProgress(i,maxPoints);
            }
            //refreshing gui
            final Image imgToPrint = SwingFXUtils.toFXImage(img,null);
            Platform.runLater(() -> g2D.drawImage(imgToPrint,0.0,0.0));
        }
        updateProgress(1.0D,1.0D);
        return monteCarloHandler.calculateIntegrate();
    }
}
