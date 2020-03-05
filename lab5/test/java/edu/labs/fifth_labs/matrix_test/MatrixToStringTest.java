package edu.labs.fifth_labs.matrix_test;

import edu.labs.third_labs.Matrix;
import org.junit.Assert;
import org.junit.Test;


public class MatrixToStringTest {

    @Test
    public void testToString_UnitMatrix_UnitMatrixInString() {
        Matrix<Integer> unitMatrix = new Matrix<>(new Integer[][]{{1,0},{0,1}});
        //optionalTest


        Assert.assertEquals("[ 1 0 ]\n" +
                "[ 0 1 ]\n", unitMatrix.toString());
    }
}
