package org.zcorp.java3.matrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainMatrix {
    private static final int MATRIX_SIZE = 1000;
    private static final int THREAD_NUMBER = 10;

    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

    public static void main(String[] args) {
        final int[][] matrixA = MatrixUtil.create(MATRIX_SIZE);
        final int[][] matrixB = MatrixUtil.create(MATRIX_SIZE);

        double singleThreadDurationSum = 0;
        double concurrentThreadDurationSum = 0;
        int count = 1;
        while (count < 6) {
            System.out.println("Pass " + count);

            long start = System.currentTimeMillis();
            final int[][] matrixC = MatrixUtil.singleThreadMultiply(matrixA, matrixB);
            double duration = (System.currentTimeMillis() - start) / 1000.;
            out("Single thread time, sec: %.3f", duration);
            singleThreadDurationSum += duration;

            start = System.currentTimeMillis();
            final int[][] concurrentMatrixC = MatrixUtil.concurrentMultiply(matrixA, matrixB, executor);
            duration = (System.currentTimeMillis() - start) / 1000.;
            out("Concurrent thread time, sec: %.3f", duration);
            concurrentThreadDurationSum += duration;

            count++;

            if (!MatrixUtil.compare(matrixC, concurrentMatrixC)) {
                System.err.println("Comparison failed");
                break;
            }
        }
        executor.shutdown();
        out("\nAverage single thread time, sec: %.3f", singleThreadDurationSum / (count - 1));
        out("Average concurrent thread time, sec: %.3f", concurrentThreadDurationSum / (count - 1));
    }

    private static void out(String format, double ms) {
        System.out.println(String.format(format, ms));
    }
}