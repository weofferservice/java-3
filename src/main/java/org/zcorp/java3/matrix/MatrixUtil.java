package org.zcorp.java3.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class MatrixUtil {

    /**
     * Multithreading multiplication (variant 1) of two matrices
     * Assumption:
     * {@code matrixA} and {@code matrixB} are square matrices and their sizes are equal
     *
     * @param matrixA  is first matrix
     * @param matrixB  is second matrix
     * @param executor is {@code ExecutorService} to submit tasks
     * @return {@code matrixC} is a multiplication {@code matrixA} and {@code matrixB}
     */
    public static int[][] concurrentMultiply1(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException {
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
     * Multithreading multiplication (variant 2) of two matrices
     * Assumption:
     * {@code matrixA} and {@code matrixB} are square matrices and their sizes are equal
     *
     * @param matrixA  is first matrix
     * @param matrixB  is second matrix
     * @param executor is {@code ExecutorService} to submit tasks
     * @return {@code matrixC} is a multiplication {@code matrixA} and {@code matrixB}
     */
    public static int[][] concurrentMultiply2(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        class ColumnMultiplyResult {
            private final int columnNumber;
            private final int columnC[];

            private ColumnMultiplyResult(int columnNumber, int columnC[]) {
                this.columnNumber = columnNumber;
                this.columnC = columnC;
            }
        }

        final CompletionService<ColumnMultiplyResult> completionService = new ExecutorCompletionService<>(executor);

        for (int column = 0; column < matrixSize; column++) {
            final int columnNumber = column;
            final int columnB[] = new int[matrixSize];
            for (int k = 0; k < matrixSize; k++) {
                columnB[k] = matrixB[k][column];
            }
            completionService.submit(() -> {
                final int columnC[] = new int[matrixSize];

                for (int row = 0; row < matrixSize; row++) {
                    int rowA[] = matrixA[row];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += rowA[k] * columnB[k];
                    }
                    columnC[row] = sum;
                }

                return new ColumnMultiplyResult(columnNumber, columnC);
            });
        }

        for (int task = 0; task < matrixSize; task++) {
            ColumnMultiplyResult res = completionService.take().get();
            for (int row = 0; row < matrixSize; row++) {
                matrixC[row][res.columnNumber] = res.columnC[row];
            }
        }

        return matrixC;
    }

    /**
     * Multithreading multiplication (variant 3) of two matrices
     * Assumption:
     * {@code matrixA} and {@code matrixB} are square matrices and their sizes are equal
     *
     * @param matrixA  is first matrix
     * @param matrixB  is second matrix
     * @param executor is {@code ExecutorService} to submit tasks
     * @return {@code matrixC} is a multiplication {@code matrixA} and {@code matrixB}
     */
    public static int[][] concurrentMultiply3(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        final int[][] matrixBT = new int[matrixSize][matrixSize];
        for (int row = 0; row < matrixSize; row++) {
            for (int column = 0; column < matrixSize; column++) {
                matrixBT[column][row] = matrixB[row][column];
            }
        }

        List<Callable<Void>> tasks = new ArrayList<>(matrixSize);

        for (int row = 0; row < matrixSize; row++) {
            final int rowNumber = row;
            final int rowA[] = matrixA[rowNumber];

            tasks.add(() -> {
                for (int column = 0; column < matrixSize; column++) {
                    int columnB[] = matrixBT[column];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += rowA[k] * columnB[k];
                    }
                    matrixC[rowNumber][column] = sum;
                }
                return null;
            });
        }

        executor.invokeAll(tasks);

        return matrixC;
    }

    /**
     * Multithreading multiplication (variant 4) of two matrices
     * Assumption:
     * {@code matrixA} and {@code matrixB} are square matrices and their sizes are equal
     *
     * @param matrixA  is first matrix
     * @param matrixB  is second matrix
     * @param executor is {@code ExecutorService} to submit tasks
     * @return {@code matrixC} is a multiplication {@code matrixA} and {@code matrixB}
     */
    public static int[][] concurrentMultiply4(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        final int[][] matrixBT = new int[matrixSize][matrixSize];
        for (int row = 0; row < matrixSize; row++) {
            for (int column = 0; column < matrixSize; column++) {
                matrixBT[column][row] = matrixB[row][column];
            }
        }

        List<Callable<Void>> tasks = new ArrayList<>();

        int processorsCount = Runtime.getRuntime().availableProcessors();
        int rowsCountPerProcessor = matrixSize / processorsCount;
        if (rowsCountPerProcessor == 0) {
            processorsCount = matrixSize;
            rowsCountPerProcessor = 1;
        }

        for (int processor = 1; processor <= processorsCount; processor++) {
            int firstRowNumber = rowsCountPerProcessor * (processor - 1);
            int lastRowNumber = processor == processorsCount ? matrixSize - 1 : rowsCountPerProcessor * processor - 1;

            tasks.add(() -> {
                for (int row = firstRowNumber; row <= lastRowNumber; row++) {
                    for (int column = 0; column < matrixSize; column++) {
                        int sum = 0;
                        for (int k = 0; k < matrixSize; k++) {
                            sum += matrixA[row][k] * matrixBT[column][k];
                        }
                        matrixC[row][column] = sum;
                    }
                }
                return null;
            });
        }

        executor.invokeAll(tasks);

        return matrixC;
    }

    /**
     * Multithreading multiplication (variant 5) of two matrices
     * Assumption:
     * {@code matrixA} and {@code matrixB} are square matrices and their sizes are equal
     *
     * @param matrixA  is first matrix
     * @param matrixB  is second matrix
     * @param executor is {@code ExecutorService} to submit tasks
     * @return {@code matrixC} is a multiplication {@code matrixA} and {@code matrixB}
     */
    public static int[][] concurrentMultiply5(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        List<Callable<Void>> tasks = IntStream.range(0, matrixSize)
                .parallel()
                .mapToObj(column -> (Callable<Void>) () -> {
                    int[] columnB = new int[matrixSize];
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
                    return null;
                })
                .collect(toList());

        executor.invokeAll(tasks);

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
    private static boolean compare(int[][] matrixA, int[][] matrixB) {
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

    /**
     * Comparing matrices
     * Assumption:
     * {@code matrices} are square matrices and their sizes are equal
     *
     * @param matrices is array of matrices
     * @return {@code true} if all matrices are equal
     */
    public static boolean compare(int[][]... matrices) {
        final int size = matrices.length;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (!compare(matrices[i], matrices[j])) {
                    return false;
                }
            }
        }
        return true;
    }

}