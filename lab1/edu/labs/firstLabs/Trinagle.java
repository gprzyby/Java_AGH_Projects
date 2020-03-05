package edu.labs.firstLabs;

public class Trinagle extends Figure implements Print {
    private double a;
    private double b;
    private double c;

    Trinagle(double a, double b, double c) throws IllegalArgumentException{
        if (a < 0 || b < 0 || c < 0)
            throw new IllegalArgumentException("Length cannot be less than zero");
        else if ( a + b < c || a + c < b || b + c < a)
            throw new IllegalArgumentException("There isn't pair of sides that sum is greater than third side") ;

        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Trinagle copy(Trinagle pattern) {
        this.a = pattern.a;
        this.b = pattern.b;
        this.c = pattern.c;
        return this;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    @Override
    public double calculateArea() {
        double p = calculatePerimeter() / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    @Override
    public double calculatePerimeter() {
        return a + b + c;
    }

    @Override
    public void print() {
        System.out.println( "Trinagle with sides: " + a + ", " + b + ", " + c + " area: " + calculateArea() + " perimeter: " + calculatePerimeter());
    }
}
