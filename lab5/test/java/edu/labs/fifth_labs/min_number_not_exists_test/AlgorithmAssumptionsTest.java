package edu.labs.fifth_labs.min_number_not_exists_test;

import edu.labs.third_labs.second_task.ElementIntervalException;
import edu.labs.third_labs.second_task.ListSizeNotSupportedException;
import edu.labs.third_labs.second_task.SecondTask;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlgorithmAssumptionsTest {

    @Test(expected = ListSizeNotSupportedException.class)
    public void solution_ListLargerThan10000_ThrowsListSizeNotSupportedException() {
        List<Integer> listLargerThan10000 = Stream.iterate(1,a -> a+1).limit(11000).collect(Collectors.toList());
        SecondTask.solution(listLargerThan10000);
    }

    @Test(expected = ElementIntervalException.class)
    public void solution_ListWithElementsNotSupported_ThrowsElementIntervalExcepiton() {
        SecondTask.solution(Arrays.asList(1000005));
    }
}
