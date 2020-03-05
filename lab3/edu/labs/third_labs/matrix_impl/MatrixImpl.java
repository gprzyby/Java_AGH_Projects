package edu.labs.third_labs.matrix_impl;


class MatrixImpl {

    public static void main(String[] args) {
        Matrix<String> matrix = new Matrix<>(new String[][] {{"1","1"},{"1","1"}});
        Matrix<String> matrix1 = new Matrix<>(new String[][] {{"1","2"},{"3","4"}});
        Matrix<Integer> unitMatrix = new Matrix<>(new Integer[][] {{1,0},{0,1}});
        System.out.println(matrix);

        for(String str : matrix) {
            System.out.println(str);
        }

        try {
            System.out.println(matrix.sum(matrix1,(e,f) ->  e + f));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }
}
