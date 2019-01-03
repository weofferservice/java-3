package org.zcorp.java3.matrix;

import java.util.Random;
import java.util.concurrent.ExecutorService;

public class MatrixUtil {

    /**
     * Multithreading multiplication of two matrices
     * Assumption:
     * {@code matrixA} and {@code matrixB} are square matrices and their sizes are equal
     *
     * @param matrixA  is first matrix
     * @param matrixB  is second matrix
     * @param executor is {@code ExecutorService} to submit tasks
     * @return {@code matrixC} is a multiplication {@code matrixA} and {@code matrixB}
     */
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        // TODO implement parallel multiplication matrixA * matrixB
        // Пока добавил неоптимизированную однопоточную реализацию,
        // чтобы в методе main удачно проходило сравнение матриц
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }

        return matrixC;
    }

    /**
     * Single threaded multiplication of two matrices
     * Assumption:
     * {@code matrixA} and {@code matrixB} are square matrices and their sizes are equal
     *
     * @param matrixA is first matrix
     * @param matrixB is second matrix
     * @return {@code matrixC} is a multiplication {@code matrixA} and {@code matrixB}
     */
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        // TODO optimize by https://habr.com/post/114797/
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }

        return matrixC;
    }

    /**
     * Creating a square matrix
     *
     * @param size is size of new square matrix
     * @return {@code matrix} is a new square matrix
     */
    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    /**
     * Comparing matrices
     * Assumption:
     * {@code matrixA} and {@code matrixB} are square matrices and their sizes are equal
     *
     * @param matrixA is first matrix
     * @param matrixB is second matrix
     * @return {@code true} if all values are equal
     */
    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

}