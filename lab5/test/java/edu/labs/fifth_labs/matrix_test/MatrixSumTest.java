package edu.labs.fifth_labs.matrix_test;

import edu.labs.third_labs.Matrix;
import org.junit.Assert;
import org.junit.Test;

public class MatrixSumTest {

    @Test
    public void sum_IntegerUnitMatrices_SumTwoUnitMatrices() {
        Matrix<Integer> matrix1 = new Matrix<>(new Integer[][] { {1 , 0} , {0 , 1}});
        try {
            Matrix<Integer> summedMatrix = matrix1.sum(matrix1.clone(),Integer::sum);
            Assert.assertTrue(summedMatrix.toString().equals("[ 2 0 ]\n" + "[ 0 2 ]\n"));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        }

    @Test(expected = IllegalArgumentException.class)
    public void sum_MatricesWithDifferentDimensions_ThrowsIllegalArgumentExceprion() {
        Matrix<Integer> matrix1 = new Matrix<>(new Integer[][] { {1 , 0} , {0 , 1}});
        Matrix<Integer> matrix2 = new Matrix<>(new Integer[][] { {1 , 1 , 0} , {0 , 1 , 1}});
        try {
            matrix1.sum(matrix2,(a,b) -> a+b);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }


}
