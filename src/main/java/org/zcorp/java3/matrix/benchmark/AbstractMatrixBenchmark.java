package org.zcorp.java3.matrix.benchmark;

import org.openjdk.jmh.annotations.*;
import org.zcorp.java3.matrix.MatrixUtil;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5)
@Measurement(iterations = 5)
@BenchmarkMode({Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Threads(1)
@Fork(1)
@Timeout(time = 5, timeUnit = TimeUnit.MINUTES)
public abstract class AbstractMatrixBenchmark {

    private static final int MATRIX_SIZE = 1000;

    protected int[][] matrixA;
    protected int[][] matrixB;

    @Setup
    public void matricesSetup() {
        matrixA = MatrixUtil.create(MATRIX_SIZE);
        matrixB = MatrixUtil.create(MATRIX_SIZE);
    }

}