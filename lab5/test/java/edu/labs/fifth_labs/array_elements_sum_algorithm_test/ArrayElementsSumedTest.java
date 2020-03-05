package edu.labs.fifth_labs.array_elements_sum_algorithm_test;

import edu.labs.third_labs.fourth_task.FourthTask;
import edu.labs.third_labs.third_task.NoSolutionExistException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ArrayElementsSumedTest {

    @Test(expected = NoSolutionExistException.class)
    public void solution_ArrayThatTargetSumDoesntExist_ThrowsNoSolutionExistException() {
        float[] array = new float[] {1.F,2.F,3.F,4.F,9.F};
        FourthTask.solution(array,20.F);
    }

    @Test
    public void solution_ArrayThatSolutionExistAtThirdAndFifthIndex_ReturnPositionInArray() {
        float[] array = new float[] {4.F,7.F,1.F,2.F,10.F,21.F};
        Assert.assertArrayEquals("Should retrun array with 2 and 4 int",new int[] {5,3}, FourthTask.solution(array,23.F));
    }

}
