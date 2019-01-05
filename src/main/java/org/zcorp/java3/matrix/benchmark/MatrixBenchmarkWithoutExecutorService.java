package org.zcorp.java3.matrix.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.zcorp.java3.matrix.MatrixUtil;

public class MatrixBenchmarkWithoutExecutorService extends AbstractMatrixBenchmark {

    @Benchmark
    public int[][] singleThreadMultiply1() {
        return MatrixUtil.singleThreadMultiply1(matrixA, matrixB);
    }

    @Benchmark
    public int[][] singleThreadMultiply2() {
        return MatrixUtil.singleThreadMultiply2(matrixA, matrixB);
    }

    @Benchmark
    public int[][] concurrentMultiply7() throws Exception {
        return MatrixUtil.concurrentMultiply7(matrixA, matrixB);
    }

}