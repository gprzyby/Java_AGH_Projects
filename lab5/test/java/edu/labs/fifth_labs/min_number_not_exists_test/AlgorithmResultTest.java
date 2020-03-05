package edu.labs.fifth_labs.min_number_not_exists_test;

import edu.labs.third_labs.second_task.SecondTask;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlgorithmResultTest {

    @Test
    public void solution_ListWithGapBetweenSixAndEight_ReturnsSeven() {
        Assert.assertTrue("Should return seven", SecondTask.solution(Arrays.asList(1,2,3,4,5,6,8,9,10)) == 7);
    }

    @Test
    public void solution_ListWithNumberLessThanZero_ReturnsOne() {
        List<Integer> list = Stream.iterate(-1,a -> --a).limit(10).collect(Collectors.toList());
        Assert.assertFalse("Should return one", SecondTask.solution(list) != 1);
    }

    @Test
    public void solution_ListWithoutOne_ReturnsOne() {
        List<Integer> list = Stream.iterate(2,a -> ++a).limit(10).collect(Collectors.toList());
        Assert.assertSame("Should return one", SecondTask.solution(list),1);

    }
}
