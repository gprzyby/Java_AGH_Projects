package edu.labs.fifth_labs;

import edu.labs.fifth_labs.sorting_algorithms_test.OptimisticSortTest;
import edu.labs.fifth_labs.sorting_algorithms_test.PessimisticSortTest;
import edu.labs.fifth_labs.sorting_algorithms_test.RealSortTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PessimisticSortTest.class,
        RealSortTest.class,
        OptimisticSortTest.class
})
public class SortingAlgorithmsTest {
}
