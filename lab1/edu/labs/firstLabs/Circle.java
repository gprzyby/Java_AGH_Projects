package edu.labs.firstLabs;

public class Circle extends Figure implements Print {
    private double radius;

    public Circle(double radius) throws IllegalArgumentException {
        if(radius < 0)
            throw new IllegalArgumentException("Length cannot be less than zero");

        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Circle copy(Circle pattern)
    {
        this.radius = pattern.radius;
        return this;
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public void print() {
        System.out.println("Circle with radius " + radius + ", area: " + calculateArea() + " perimeter: " + calculatePerimeter());
    }
}
