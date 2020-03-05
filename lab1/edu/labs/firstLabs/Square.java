package edu.labs.firstLabs;

import java.lang.IllegalArgumentException;

public class Square extends Figure implements Print {
    private double a;

    public Square(double a) throws IllegalArgumentException {
        if(a<0) {
            throw new IllegalArgumentException("Side of square cannot be less than zero");
        }

        this.a = a;

    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public Square copy(Square pattern) {
        this.a = pattern.a;
        return this;
    }

    @Override
    public double calculateArea() {
        return a*a;
    }

    @Override
    public double calculatePerimeter() {
        return 4*a;
    }

    @Override
    public void print() {
        System.out.println("Square with side " + a + " length, area: " + calculateArea() + " perimeter: " + calculatePerimeter());
    }
}
