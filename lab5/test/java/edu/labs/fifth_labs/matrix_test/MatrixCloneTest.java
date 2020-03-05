package edu.labs.fifth_labs.matrix_test;

import edu.labs.third_labs.Matrix;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class MatrixCloneTest {

    @Test
    public void testClone_UnitMatrix_CloneAndClonedMatrixAreTheSame() {
        Matrix<Integer> pattern = new Matrix<>(new Integer[][] {{1,0},{0,1}});
        try {
            Matrix<Integer> clone = pattern.clone();
            Iterator<Integer> patternIterator = pattern.iterator();
            Iterator<Integer> cloneIterator = clone.iterator();

            //checking if numbers are the same
            int i = 1;
            for(;cloneIterator.hasNext() && patternIterator.hasNext();++i) {
                Assert.assertSame("Elements should be the same",cloneIterator.next(),patternIterator.next());
            }

            //checking if all numbers has checked
            Assert.assertFalse(i<4);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

}
