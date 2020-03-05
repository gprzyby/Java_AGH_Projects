package edu.labs.fifth_labs.matrix_test;

import edu.labs.third_labs.Matrix;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

public class MatrixGetterSetterTest {

    @Test
    public void testGetAtIndex_UnitMatrix_GetsElementAtSecondRowAndColumn() {
        Matrix<Integer> matrix = new Matrix<>(new Integer[][] {{1,0},{0,1}});
        Assert.assertEquals("GetAtIndex should return 1", Integer.toString(1),matrix.getAtIndex(1,1).toString());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetAtIndex_UnitMatrix_ThrowsNoSuchElementExceptionWhenOutOfBound() {
        Matrix<Integer> matrix = new Matrix<>(new Integer[][] {{1,0},{0,1}});
        matrix.getAtIndex(2,2);
    }

    @Test
    public void testSetAtIndex_UnitMatrix_SetsOneInFirstRowSecondColumn() {
        Matrix<Integer> matrix = new Matrix<>(new Integer[][] {{1,0},{0,1}});
        matrix.setAtIndex(0,1,1);
        Assert.assertTrue(matrix.toString().equals("[ 1 1 ]\n" + "[ 0 1 ]\n"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testSetAtIndex_UnitMatrix_ThrowsNoSuchElementExceptionWhenOutOfBound() {
        Matrix<Integer> matrix = new Matrix<>(new Integer[][] {{1,0},{0,1}});
        matrix.setAtIndex(2,2,1);
    }


}
