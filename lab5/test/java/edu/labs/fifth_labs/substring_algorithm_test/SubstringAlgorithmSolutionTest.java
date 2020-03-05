package edu.labs.fifth_labs.substring_algorithm_test;

import edu.labs.third_labs.third_task.NoSolutionExistException;
import edu.labs.third_labs.third_task.ThirdTask;
import org.junit.Assert;
import org.junit.Test;

public class SubstringAlgorithmSolutionTest {

    @Test(expected = IllegalArgumentException.class)
    public void substring_FirstStringDoesntContainsSecondStringLetters_ThrowsIllegalArgumentException() {
        String firstArgument = "abc";
        String secondArgument = "cde";
        ThirdTask.substring(firstArgument,secondArgument);
    }

    @Test
    public void substring_FirstStringCanBeSubstringOfSecond_ReturnsThree() {
        String firstArgument = "abcd";
        String secondArgument = "cdabcdab";
        Assert.assertTrue("Should return 3", ThirdTask.substring(firstArgument,secondArgument) == 3);
    }

    @Test(expected = NoSolutionExistException.class)
    public void substring_FirstStringABCSecondStringCBA_ThrowsNoSolutionExistException() {
        ThirdTask.substring("abc","cba");
    }
}
