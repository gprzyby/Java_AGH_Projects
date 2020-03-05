package edu.labs.fifth_labs.sorting_algorithms_test;

import edu.labs.third_labs.fifth_task.SortingAlgorithms;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.stream.Stream;

public class OptimisticSortTest {
    private Integer[] sortedTable = Stream.iterate(1, number -> ++number).limit(10000).toArray(Integer[]::new);

    //optimistic condition should execute in 1s.
    @Rule
    public Timeout globalTimeout = Timeout.seconds(2L);

    @Test
    public void shellSort_SortedTable_SortTable() {
        SortingAlgorithms.shellSort(sortedTable);
    }

    @Test
    public void bubbleSort_SortedTable_SortTable() {
        SortingAlgorithms.bubbleSort(sortedTable);
    }

    @Test
    public void mergeSort_SortedTable_SortTable() {
        SortingAlgorithms.mergeSort(sortedTable,0,9999);
    }

    @Test
    public void quickSort_SortedTable_SortTable() {
        SortingAlgorithms.quickSort(sortedTable,0,9999);
    }

    @Test
    public void cocktailSort_SortedTable_SortTable() {
        SortingAlgorithms.cocktailSort(sortedTable);
    }
}
