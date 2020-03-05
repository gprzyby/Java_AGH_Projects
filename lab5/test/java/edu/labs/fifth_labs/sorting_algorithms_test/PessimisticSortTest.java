package edu.labs.fifth_labs.sorting_algorithms_test;

import edu.labs.third_labs.fifth_task.SortingAlgorithms;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.stream.Stream;

public class PessimisticSortTest {
    private Integer[] sortedTable = Stream.iterate(10000, number -> --number).limit(10000).toArray(Integer[]::new);

    //pesimistic condition should execute in 4s.
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Test
    public void shellSort_DecreasingSortedTable_SortTable() {
        SortingAlgorithms.shellSort(sortedTable);
    }

    @Test
    public void bubbleSort_DecreasingSortedTable_SortTable() {
        SortingAlgorithms.bubbleSort(sortedTable);
    }

    @Test
    public void mergeSort_DecreasingSortedTable_SortTable() {
        SortingAlgorithms.mergeSort(sortedTable,0,9999);
    }

    @Test
    public void quickSort_DecreasingSortedTable_SortTable() {
        SortingAlgorithms.quickSort(sortedTable,3,4000);
    }

    @Test
    public void cocktailSort_DecreasingSortedTable_SortTable() {
        SortingAlgorithms.cocktailSort(sortedTable);
    }
}
