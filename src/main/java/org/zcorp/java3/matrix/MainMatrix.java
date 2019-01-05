package org.zcorp.java3.matrix;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainMatrix {
    private static final int MATRIX_SIZE = 1000;
    private static final int THREAD_NUMBER = 10;

    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final int[][] matrixA = MatrixUtil.create(MATRIX_SIZE);
        final int[][] matrixB = MatrixUtil.create(MATRIX_SIZE);

        double singleThread1DurationSum = 0;
        double singleThread2DurationSum = 0;
        double concurrentThread1DurationSum = 0;
        double concurrentThread2DurationSum = 0;
        double concurrentThread3DurationSum = 0;
        double concurrentThread4DurationSum = 0;
        double concurrentThread5DurationSum = 0;
        double concurrentThread6DurationSum = 0;
        double concurrentThread7DurationSum = 0;
        int count = 1;
        while (count < 6) {
            System.out.println("Pass " + count);

            long start = System.currentTimeMillis();
            final int[][] singleThreadMatrixC1 = MatrixUtil.singleThreadMultiply1(matrixA, matrixB);
            double duration = (System.currentTimeMillis() - start) / 1000.;
            out("Single thread time (variant 1), sec: %.3f", duration);
            singleThread1DurationSum += duration;

            start = System.currentTimeMillis();
            final int[][] singleThreadMatrixC2 = MatrixUtil.singleThreadMultiply2(matrixA, matrixB);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Single thread time (variant 2), sec: %.3f", duration);
            singleThread2DurationSum += duration;

            start = System.currentTimeMillis();
            final int[][] concurrentMatrixC1 = MatrixUtil.concurrentMultiply1(matrixA, matrixB, executor);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Concurrent thread time (variant 1), sec: %.3f", duration);
            concurrentThread1DurationSum += duration;

            start = System.currentTimeMillis();
            final int[][] concurrentMatrixC2 = MatrixUtil.concurrentMultiply2(matrixA, matrixB, executor);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Concurrent thread time (variant 2), sec: %.3f", duration);
            concurrentThread2DurationSum += duration;

            start = System.currentTimeMillis();
            final int[][] concurrentMatrixC3 = MatrixUtil.concurrentMultiply3(matrixA, matrixB, executor);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Concurrent thread time (variant 3), sec: %.3f", duration);
            concurrentThread3DurationSum += duration;

            start = System.currentTimeMillis();
            final int[][] concurrentMatrixC4 = MatrixUtil.concurrentMultiply4(matrixA, matrixB, executor);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Concurrent thread time (variant 4), sec: %.3f", duration);
            concurrentThread4DurationSum += duration;

            start = System.currentTimeMillis();
            final int[][] concurrentMatrixC5 = MatrixUtil.concurrentMultiply5(matrixA, matrixB, executor);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Concurrent thread time (variant 5), sec: %.3f", duration);
            concurrentThread5DurationSum += duration;

            start = System.currentTimeMillis();
            final int[][] concurrentMatrixC6 = MatrixUtil.concurrentMultiply6(matrixA, matrixB, executor);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Concurrent thread time (variant 6), sec: %.3f", duration);
            concurrentThread6DurationSum += duration;

            start = System.currentTimeMillis();
            final int[][] concurrentMatrixC7 = MatrixUtil.concurrentMultiply7(matrixA, matrixB);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Concurrent thread time (variant 7), sec: %.3f", duration);
            concurrentThread7DurationSum += duration;

            count++;

            if (!MatrixUtil.compare(
                    singleThreadMatrixC1,
                    singleThreadMatrixC2,
                    concurrentMatrixC1,
                    concurrentMatrixC2,
                    concurrentMatrixC3,
                    concurrentMatrixC4,
                    concurrentMatrixC5,
                    concurrentMatrixC6,
                    concurrentMatrixC7)) {
                System.err.println("Comparison failed");
                break;
            }
        }
        executor.shutdown();
        out("\nAverage single thread time (variant 1), sec: %.3f", singleThread1DurationSum / (count - 1));
        out("Average single thread time (variant 2), sec: %.3f", singleThread2DurationSum / (count - 1));
        out("Average concurrent thread time (variant 1), sec: %.3f", concurrentThread1DurationSum / (count - 1));
        out("Average concurrent thread time (variant 2), sec: %.3f", concurrentThread2DurationSum / (count - 1));
        out("Average concurrent thread time (variant 3), sec: %.3f", concurrentThread3DurationSum / (count - 1));
        out("Average concurrent thread time (variant 4), sec: %.3f", concurrentThread4DurationSum / (count - 1));
        out("Average concurrent thread time (variant 5), sec: %.3f", concurrentThread5DurationSum / (count - 1));
        out("Average concurrent thread time (variant 6), sec: %.3f", concurrentThread6DurationSum / (count - 1));
        out("Average concurrent thread time (variant 7), sec: %.3f", concurrentThread7DurationSum / (count - 1));
    }

    private static void out(String format, double ms) {
        System.out.println(String.format(format, ms));
    }
}