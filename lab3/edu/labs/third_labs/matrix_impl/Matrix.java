package edu.labs.third_labs.matrix_impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BinaryOperator;

public class Matrix<E> implements Iterable<E>, Cloneable {
    private List<ArrayList<E>> matrix;

    private int rows;
    private int columns;

    public Matrix(int rows,int columns) {
        this.rows = rows;
        this.columns = columns;

        matrix = new ArrayList<>(rows);
        for (int i = 0; i < rows; ++i) {
            matrix.add(new ArrayList<E>(columns));
        }
    }

    public Matrix(E[][] pattern) {
        this(pattern.length,pattern[0].length);
        for(int i=0; i<this.rows; ++i) {
            for(int j=0; j<this.columns; ++j) {
                matrix.get(i).add(pattern[i][j]);
            }
        }
    }

    public void setAtIndex(int row, int column, E element) {
        if(row >= rows && column >= columns) throw new NoSuchElementException();
        matrix.get(row).set(column, element);
    }

    public E getAtIndex(int row, int column) {
        if(row >= rows && column >= columns) throw new NoSuchElementException();
        return matrix.get(row).get(column);
    }

    public Matrix<E> sum(Matrix<E> element, BinaryOperator<E> summer) throws CloneNotSupportedException {
        Matrix<E> toRet = this.clone();
        //checking if arrays has the same dimensions
        if(element.rows != this.rows || element.columns != this.columns ) throw new IllegalArgumentException("Matrices has different dimensions");

        for (int i = 0; i < rows; ++i) {
            for (int j =0; j < columns; ++j) {
                toRet.setAtIndex(i,j,summer.apply(this.getAtIndex(i,j), element.getAtIndex(i,j)));
            }
        }
        return toRet;
    }

    @Override
    public Matrix<E> clone() throws CloneNotSupportedException {
        Matrix<E> ker= (Matrix<E>) super.clone();
        ker.matrix = new ArrayList<>();
        for(int i=0; i<this.rows; ++i) {
            ker.matrix.add(new ArrayList<>());
            for(int j=0; j<this.columns; ++j) {
                ker.matrix.get(i).add(j,getAtIndex(i,j));
            }
        }
        return ker;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        Iterator<E> iter = new Iter();
        for(int i=0;i<rows;++i) {
            buffer.append("[ ");
            for(int j=0;j<columns;++j) {
                buffer.append(iter.next().toString()).append(' ');
            }
            buffer.append("]\n");
        }

        return buffer.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iter();
    }

    private class Iter implements Iterator<E> {
        private int columnPos = 0;
        private int rowPos = 0;

        Iter() {

        }

        @Override
        public boolean hasNext() {
            //iterator has next if it isn't on last row or in last row is not on last element
            return rowPos != Matrix.this.rows && columnPos != Matrix.this.rows;
        }

        @Override
        public E next() {
            if(!hasNext()) throw new NoSuchElementException();
            //saving object before
            E toRet = Matrix.this.matrix.get(rowPos).get(columnPos);
            ++columnPos;
            if(columnPos == Matrix.this.columns) {
                if (rowPos != Matrix.this.rows - 1) {
                    ++rowPos;
                    columnPos = 0;
                }
            }
            return toRet;
        }
    }
}
