package org.zcorp.java3.matrix.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.zcorp.java3.matrix.MatrixUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatrixBenchmarkWithExecutorService extends AbstractMatrixBenchmark {

    @Param({"3", "4", "10"})
    private int threadsCount;

    private ExecutorService executor;

    @Setup
    public void setup() {
        executor = Executors.newFixedThreadPool(threadsCount);
    }

    @TearDown
    public void tearDown() {
        executor.shutdown();
    }

    @Benchmark
    public int[][] concurrentMultiply1() throws Exception {
        return MatrixUtil.concurrentMultiply1(matrixA, matrixB, executor);
    }

    @Benchmark
    public int[][] concurrentMultiply2() throws Exception {
        return MatrixUtil.concurrentMultiply2(matrixA, matrixB, executor);
    }

    @Benchmark
    public int[][] concurrentMultiply3() throws Exception {
        return MatrixUtil.concurrentMultiply3(matrixA, matrixB, executor);
    }

    @Benchmark
    public int[][] concurrentMultiply4() throws Exception {
        return MatrixUtil.concurrentMultiply4(matrixA, matrixB, executor);
    }

    @Benchmark
    public int[][] concurrentMultiply5() throws Exception {
        return MatrixUtil.concurrentMultiply5(matrixA, matrixB, executor);
    }

    @Benchmark
    public int[][] concurrentMultiply6() throws Exception {
        return MatrixUtil.concurrentMultiply6(matrixA, matrixB, executor);
    }

}