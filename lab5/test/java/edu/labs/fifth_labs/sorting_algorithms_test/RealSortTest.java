package edu.labs.fifth_labs.sorting_algorithms_test;

import edu.labs.third_labs.fifth_task.SortingAlgorithms;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.Random;
import java.util.stream.Stream;

public class RealSortTest {
    private Integer[] sortedTable;

    {
        Random rand = new Random(1000L);
        sortedTable = Stream.generate(rand::nextInt).limit(10000).toArray(Integer[]::new);
    }

    //real condition should execute in 3s.
    @Rule
    public Timeout globalTimeout = Timeout.seconds(3);

    @Test
    public void shellSort_RealisticTable_SortTable() {
        SortingAlgorithms.shellSort(sortedTable);
    }

    @Test
    public void bubbleSort_RealisticTable_SortTable() {
        SortingAlgorithms.bubbleSort(sortedTable);
    }

    @Test
    public void mergeSort_RealisticTable_SortTable() {
        SortingAlgorithms.mergeSort(sortedTable,0,9999);
    }

    @Test
    public void quickSort_RealisticTable_SortTable() {
        SortingAlgorithms.quickSort(sortedTable,0,9999);
    }

    @Test
    public void cocktailSort_RealisticTable_SortTable() {
        SortingAlgorithms.cocktailSort(sortedTable);
    }
}
