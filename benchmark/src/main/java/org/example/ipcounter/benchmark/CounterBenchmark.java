package org.example.ipcounter.benchmark;

import com.example.ipcounter.counter.ByteBufferUniqueIPCounter;
import com.example.ipcounter.counter.IntStreamingCounter;
import com.example.ipcounter.counter.UniqueIPCounter;
import com.example.ipcounter.reader.BytesIPv4Reader;
import org.example.ipcounter.benchmark.util.BenchmarkUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@State(Scope.Benchmark)
public class CounterBenchmark {

    private static final int BUFFER_SIZE = 10 * 1024;
    private static final int DATA_SET_SAMPLE_COUNT = 10_000_000;

    @State(Scope.Thread)
    public static class BenchmarkState {
        Path path;

        @Setup(Level.Iteration)
        public void setup() throws IOException {
            this.path = BenchmarkUtils.generateTempFile(DATA_SET_SAMPLE_COUNT);
        }

        @TearDown(Level.Iteration)
        public void teardown() throws IOException {
            Files.deleteIfExists(this.path);
        }
    }

    private final UniqueIPCounter bufferedReader = new ByteBufferUniqueIPCounter(BUFFER_SIZE);
    private final UniqueIPCounter intStreaming = new IntStreamingCounter(new BytesIPv4Reader(BUFFER_SIZE));

    @Benchmark
    public long bufferedReader(BenchmarkState state) throws IOException {
        return bufferedReader.countUniqueIPs(state.path);
    }

    @Benchmark
    public long intStreaming(BenchmarkState state) throws IOException {
        return intStreaming.countUniqueIPs(state.path);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CounterBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}
