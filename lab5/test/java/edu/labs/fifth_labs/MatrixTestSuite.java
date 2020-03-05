package edu.labs.fifth_labs;

import edu.labs.fifth_labs.matrix_test.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MatrixCloneTest.class,
        MatrixGetterSetterTest.class,
        MatrixIteratorTest.class,
        MatrixToStringTest.class,
        MatrixSumTest.class
})

public class MatrixTestSuite {
}
