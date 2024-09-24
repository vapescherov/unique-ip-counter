package org.example.ipcounter.benchmark;

import com.example.ipcounter.builder.DefaultIPAddressBuilder;
import com.example.ipcounter.builder.IPAddressBuilder;
import com.example.ipcounter.reader.BytesIPv4Reader;
import com.example.ipcounter.reader.IPv4Reader;
import org.example.ipcounter.benchmark.util.BenchmarkUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.IntStream;

@State(Scope.Benchmark)
public class IPv4ReaderBenchmark {

    private static final int BUFFER_SIZE = 10 * 1024;
    private static final int DATA_SET_SAMPLE_COUNT = 1_000_000;

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

    private final IPv4Reader streamingByteReader = new BytesIPv4Reader(BUFFER_SIZE);

    @Benchmark
    public void streamingByteReader(Blackhole bh, BenchmarkState state) throws IOException {
        try (IntStream stream = streamingByteReader.ipAddressStream(state.path)) {
            stream.forEach(bh::consume);
        }
    }

    private final IPAddressBuilder ipAddressBuilder = new DefaultIPAddressBuilder();

    @Benchmark
    public void bufferedInputStreamByteReader(Blackhole bh, BenchmarkState state) throws IOException {
        try (InputStream is = new BufferedInputStream(Files.newInputStream(state.path, StandardOpenOption.READ))) {
            byte[] buffer = new byte[BUFFER_SIZE];

            int bytesRead;
            while ((bytesRead = is.read(buffer)) > 0) {
                for (int i = 0; i < bytesRead; i++) {
                    byte currentByte = buffer[i];
                    if (currentByte != '\n') {
                        ipAddressBuilder.append(currentByte);
                    } else {
                        int ip = ipAddressBuilder.build();
                        bh.consume(ip);
                    }
                }
            }

            if (ipAddressBuilder.hasUncommittedData()) {
                bh.consume(ipAddressBuilder.build());
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(IPv4ReaderBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

}
