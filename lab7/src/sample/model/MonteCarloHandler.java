package sample.model;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

public class MonteCarloHandler {
    private Random rand = new Random();
    private long pointsWhichBelongsAmount = 0L;
    private long pointsWhichNotBelongAmount = 0L;
    private final double x_b,x_e,y_b,y_e;
    private final BiPredicate<Double, Double> interfaceToCheckPoint;

    public MonteCarloHandler(double x_b, double x_e, double y_b, double y_e, BiPredicate<Double,Double> functionIfPointIsInIntegrateArea) {
        if(x_b > x_e || y_b > y_e) throw new IllegalArgumentException("Beginning must be less than ending coordinates");
        this.x_b = x_b;
        this.x_e = x_e;
        this.y_b = y_b;
        this.y_e = y_e;
        this.interfaceToCheckPoint = functionIfPointIsInIntegrateArea;
    }

    public double getCalculatingArea() {
        return (x_e-x_b) * (y_e - y_b);
    }

    public List<AnswerPoint> generatePoints(int amount) {
        List<AnswerPoint> points = new ArrayList<>(amount);

        for(int i = 0; i < amount; ++i) {
            points.add(generatePoint());
        }
        return points;
    }

    public AnswerPoint generatePoint() {
        double x = rand.nextDouble() * (x_e - x_b) + x_b;
        double y = rand.nextDouble() * (y_e - y_b) + y_b;
        boolean ifBelongToIntegrateArea = interfaceToCheckPoint.test(x,y);

        if(ifBelongToIntegrateArea) {
            ++pointsWhichBelongsAmount;
        } else {
            ++pointsWhichNotBelongAmount;
        }
        return new AnswerPoint(x,y,ifBelongToIntegrateArea);
    }

    public double calculateIntegrate() {
        double sum = Long.valueOf(pointsWhichBelongsAmount + pointsWhichNotBelongAmount).doubleValue();
        if(sum == 0) throw new ArithmeticException("No points was generated. Use generatePoints before");
        double area = this.getCalculatingArea();
        return area * (pointsWhichBelongsAmount/sum);
    }

    public static class AnswerPoint extends Point2D {


        private boolean ifBelongToIntegrateArea = false;

        /**
         * Creates a new instance of {@code Point2D}.
         *
         * @param x the x coordinate of the point
         * @param y the y coordinate of the point
         * @param ifBelongsToIntegrateArea boolean that indicates if point belong to integrate area
         */
        public AnswerPoint(double x, double y, boolean ifBelongsToIntegrateArea) {
            super(x, y);
            this.ifBelongToIntegrateArea = ifBelongsToIntegrateArea;
        }

        public boolean ifBelongToIntegrateArea() {
            return ifBelongToIntegrateArea;
        }


    }


}
