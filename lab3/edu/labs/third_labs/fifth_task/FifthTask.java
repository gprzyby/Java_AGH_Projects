package edu.labs.third_labs.fifth_task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class FifthTask {

    private static void bubbleSortTest(Integer[] tabOpt, Integer[] tabPes, Integer[] tabReal) {
        long timeStart,timeStop;

        //bubble sort optimistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.bubbleSort(tabOpt.clone());
        timeStop = System.currentTimeMillis();
        System.out.println("Bubble sort, optimistic: \t" + (timeStop-timeStart) + " milliseconds");

        //bubble sort pessimistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.bubbleSort(tabPes.clone());
        timeStop = System.currentTimeMillis();
        System.out.println("Bubble sort, pessimistic: \t" + (timeStop-timeStart) + " milliseconds");

        //bubble sort realistic

        timeStart = System.currentTimeMillis();
        SortingAlgorithms.bubbleSort(tabReal.clone());
        timeStop = System.currentTimeMillis();
        System.out.println("Bubble sort, realistic: \t" + (timeStop-timeStart) + " milliseconds\n");

    }

    private static void mergeSortTest(Integer[] tabOpt, Integer[] tabPes, Integer[] tabReal) {
        long timeStart,timeStop;

        //merge sort optimistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.mergeSort(tabOpt.clone(), 0, tabOpt.length - 1);
        timeStop = System.currentTimeMillis();
        System.out.println("Merge sort, optimistic: \t" + (timeStop-timeStart) + " milliseconds");

        //merge sort pessimistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.mergeSort(tabPes.clone(), 0, tabOpt.length - 1);
        timeStop = System.currentTimeMillis();
        System.out.println("Merge sort, pessimistic: \t" + (timeStop-timeStart) + " milliseconds");

        //merge sort realistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.mergeSort(tabReal.clone(), 0, tabOpt.length - 1);
        timeStop = System.currentTimeMillis();
        System.out.println("Merge sort, realistic: \t\t" + (timeStop-timeStart) + " milliseconds\n");

    }

    private static void quickSortTest(Integer[] tabOpt, Integer[] tabPes, Integer[] tabReal) {
        long timeStart;
        long timeStop;

        //quick sort optimistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.quickSort(tabOpt.clone(), 0, tabOpt.length - 1);
        timeStop = System.currentTimeMillis();
        System.out.println("Quick sort, optimistic: \t" + (timeStop-timeStart) + " milliseconds");

        //quick sort pessimistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.quickSort(tabPes.clone(), 0, tabOpt.length - 1);
        timeStop = System.currentTimeMillis();
        System.out.println("Quick sort, pessimistic: \t" + (timeStop-timeStart) + " milliseconds");

        //quick sort realistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.quickSort(tabReal.clone(), 0, tabOpt.length - 1);
        timeStop = System.currentTimeMillis();
        System.out.println("Quick sort, realistic: \t\t" + (timeStop-timeStart) + " milliseconds\n");
    }

    private static void cocktailSortTest(Integer[] tabOpt,Integer[] tabPes, Integer[] tabReal) {
        long timeStart;
        long timeStop;

        //cocktail sort optimistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.cocktailSort(tabOpt.clone());
        timeStop = System.currentTimeMillis();
        System.out.println("Cocktail sort, optimistic: \t" + (timeStop-timeStart) + " milliseconds");

        //cocktail sort pessimistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.cocktailSort(tabPes.clone());
        timeStop = System.currentTimeMillis();
        System.out.println("Cocktail sort, pessimistic: " + (timeStop-timeStart) + " milliseconds");

        //cocktail sort realistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.cocktailSort(tabReal.clone());
        timeStop = System.currentTimeMillis();
        System.out.println("Cocktail sort, realistic: \t" + (timeStop-timeStart) + " milliseconds\n");
    }

    private static void shellSortTest(Integer[] tabOpt,Integer[] tabPes, Integer[] tabReal) {
        long timeStart,timeStop;

        //shell sort optimistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.shellSort(tabOpt.clone());
        timeStop = System.currentTimeMillis();
        System.out.println("Shell sort, optimistic: \t" + (timeStop-timeStart) + " milliseconds");

        //shell sort pessimistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.shellSort(tabPes.clone());
        timeStop = System.currentTimeMillis();
        System.out.println("Shell sort, pessimistic: \t" + (timeStop-timeStart) + " milliseconds");

        //shell sort realistic
        timeStart = System.currentTimeMillis();
        SortingAlgorithms.shellSort(tabReal.clone());
        timeStop = System.currentTimeMillis();
        System.out.println("Shell sort, realistic: \t\t" + (timeStop-timeStart) + " milliseconds\n");

    }

    public static void main(String[] args) {
        long arraysLength = 10000L;
        long timeStart = 0L;
        long timeStop = 0L;
        Random rand = new Random();
        Integer[] tabOpt = Stream.iterate(1, number -> number + 1).limit(arraysLength).toArray(Integer[]::new);
        Integer[] tabPes = Stream.iterate(Math.toIntExact(arraysLength),number -> number - 1).limit(arraysLength).toArray(Integer[]::new);
        Integer[] tabReal = Stream.generate(rand::nextInt).limit(arraysLength).toArray(Integer[]::new);

        bubbleSortTest(tabOpt.clone(),tabPes.clone(),tabReal.clone());
        mergeSortTest(tabOpt.clone(),tabPes.clone(),tabReal.clone());
        quickSortTest(tabOpt.clone(),tabPes.clone(),tabReal.clone());
        cocktailSortTest(tabOpt.clone(),tabPes.clone(),tabReal.clone());
        shellSortTest(tabOpt.clone(),tabPes.clone(),tabReal.clone());

    }
}