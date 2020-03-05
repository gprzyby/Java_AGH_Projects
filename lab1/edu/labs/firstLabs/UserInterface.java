package edu.labs.firstLabs;

import java.util.InputMismatchException;
import java.util.Scanner;



public abstract class UserInterface {
    private static final int CREATE = 1;
    private static final int MODIFY = 2;
    private static final int PRINT = 3;
    private static final int EXIT = 4;

    public static void runUserInterface() {
        Scanner input = new Scanner(System.in);
        Figure inputFigure = null;
        boolean exitProgramm = false;

        do {
            System.out.println( "Type what you want to do:\n" +
                                "1. Create object\n" +
                                "2. Modify exiting object\n" +
                                "3. Print exiting object\n" +
                                "4. Exit program\n" +
                                "Your choice: " );
            try {
                int inputNumber = input.nextInt();

                switch (inputNumber) {
                    case CREATE:
                        inputFigure = createFigureFromUI();
                        break;

                    case MODIFY:
                        modifyCurrentFigure(inputFigure);
                        break;

                    case PRINT:
                        if (inputFigure == null)
                            System.out.println("Any figure object hasn't been created!");
                        if (inputFigure instanceof Print)
                            ((Print) inputFigure).print();
                        break;

                    case EXIT:
                        exitProgramm = true;
                        break;

                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.toString());
            } catch (InputMismatchException e) {
                System.out.println("Incorrect data input");
                //clearing input
                if(input.hasNext())
                    input.next();
            }
        } while (!exitProgramm);
    }

    private static Figure createFigureFromUI() throws InputMismatchException,IllegalArgumentException {
        Scanner input = new Scanner(System.in);

        System.out.println( "Type name of object class you want to create:\n" +
                            "Trinagle\n" +
                            "Square\n" +
                            "Circle\n" +
                            "Your choice: " );

        String choice = input.nextLine().toUpperCase();

        switch(choice) {
            case "TRINAGLE":
                return createTrinagleFromUI();
            case "SQUARE":
                return createSquareFromUI();
            case "CIRCLE":
                return createCircleFromUI();
            default:
                System.out.println("Incorrect input!");
        }


        return null;
    }

    private static void modifyCurrentFigure(Figure figure) throws InputMismatchException,IllegalArgumentException {
        //checking if figure is null
        if(figure == null) {
            System.out.println("Any figure object hasn't been created!");
            return;
        }
        if (figure instanceof Circle) {
            Circle tmp = createCircleFromUI();
            Circle original = (Circle) figure;
            original.copy(tmp);
        } else if (figure instanceof Square) {
            Square tmp = createSquareFromUI();
            Square original = (Square) figure;
            original.copy(tmp);
        } else if (figure instanceof Trinagle) {
            Trinagle tmp = createTrinagleFromUI();
            Trinagle original = (Trinagle) figure;
            original.copy(tmp);
        }
    }

    private static Trinagle createTrinagleFromUI() throws InputMismatchException,IllegalArgumentException {
        Scanner input = new Scanner(System.in);

        System.out.println("Type length of a: ");
        float a = input.nextFloat();

        System.out.println("Type length of b: ");
        float b = input.nextFloat();

        System.out.println("Type length of c: ");
        float c = input.nextFloat();

        return new Trinagle(a,b,c);
    }

    private static Square createSquareFromUI() throws InputMismatchException,IllegalArgumentException {
        Scanner input = new Scanner(System.in);

        System.out.println("Type length of a: ");
        float a = input.nextFloat();

        return new Square(a);
    }

    private static Circle createCircleFromUI() throws InputMismatchException,IllegalArgumentException  {
        Scanner input = new Scanner(System.in);

        System.out.println("Type length of perimeter: ");
        float perimeter = input.nextFloat();

        return new Circle(perimeter);
    }
}
