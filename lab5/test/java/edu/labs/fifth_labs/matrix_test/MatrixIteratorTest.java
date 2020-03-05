package edu.labs.fifth_labs.matrix_test;

import edu.labs.third_labs.Matrix;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MatrixIteratorTest {

    @Test
    public void testIterator_FirstElementInMatrix_NumberInFirstPosition() {
        Matrix<Integer> matrix = new Matrix<>(new Integer[][] {{1,0},{0,1}});
        Iterator<Integer> iterator = matrix.iterator();
        Assert.assertEquals("Iterator retruned invaild number", Integer.toString(1),iterator.next().toString());

    }

    @Test
    public void testIterator_UnitMatrix_GetsAllElementsUsingForeachLoop() {
        Integer[][] unitMatrix = new Integer[][] {{1,0},{0,1}};
        Matrix<Integer> matrix = new Matrix<>(new Integer[][] {{1,0},{0,1}});
        int i=0;
        int y =0;
        for(Integer number : matrix) {
            int x = i%2;
            if(i>0 && x == 0) ++y;
            Assert.assertEquals("Matrix is not the same as array pharsed in constructor", unitMatrix[y][x].toString(),number.toString());
            ++i;
        }
    }


    @Test(expected = NoSuchElementException.class)
    public void testIterator_UnitMatrix_ThrowsNoSuchElementExceptionWhenIsOutOfMatrix() {
        Matrix<Integer> matrix = new Matrix<>(new Integer[][] {{1,0},{0,1}});
        Iterator<Integer> iter = matrix.iterator();
        //making iter is in ending of matrix
        while(iter.hasNext()) {
            iter.next();
        }
        iter.next();
    }

}
