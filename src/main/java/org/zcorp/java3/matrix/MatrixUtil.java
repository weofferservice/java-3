package org.zcorp.java3.matrix;

import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
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
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        CompletionService<Void> completionService = new ExecutorCompletionService<>(executor);

        for (int column = 0; column < matrixSize; column++) {
            final int columnNumber = column;
            final int columnB[] = new int[matrixSize];
            for (int k = 0; k < matrixSize; k++) {
                columnB[k] = matrixB[k][column];
            }

            completionService.submit(() -> {
                for (int row = 0; row < matrixSize; row++) {
                    int rowA[] = matrixA[row];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += rowA[k] * columnB[k];
                    }
                    matrixC[row][columnNumber] = sum;
                }
                return null;
            });
        }

        for (int task = 0; task < matrixSize; task++) {
            completionService.take();
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

        int columnB[] = new int[matrixSize];
        for (int column = 0; column < matrixSize; column++) {
            for (int k = 0; k < matrixSize; k++) {
                columnB[k] = matrixB[k][column];
            }

            for (int row = 0; row < matrixSize; row++) {
                int rowA[] = matrixA[row];
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += rowA[k] * columnB[k];
                }
                matrixC[row][column] = sum;
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

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                matrix[row][column] = rn.nextInt(10);
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
        for (int row = 0; row < matrixSize; row++) {
            for (int column = 0; column < matrixSize; column++) {
                if (matrixA[row][column] != matrixB[row][column]) {
                    return false;
                }
            }
        }
        return true;
    }

}