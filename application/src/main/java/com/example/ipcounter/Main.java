package com.example.ipcounter;

import com.example.ipcounter.counter.ByteBufferUniqueIPCounter;
import com.example.ipcounter.counter.UniqueIPCounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static final int BUFFER_SIZE = 10 * 1024;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Error: No file path provided.");
            return;
        }

        Path path = Path.of(args[0]);
        if (!Files.exists(path)) {
            System.out.println("Error: File not found - " + path);
            return;
        }

        UniqueIPCounter ipCounter = new ByteBufferUniqueIPCounter(BUFFER_SIZE);

        System.out.printf("Processing file: \"%s\"\n", path);

        long start = System.nanoTime();
        long uniqueIps = ipCounter.countUniqueIPs(path);
        long end = System.nanoTime();

        printMessage(path, uniqueIps, start, end);
    }

    private static void printMessage(Path path, long uniqueIps, long start, long end) throws IOException {
        long durationNanos = end - start;
        String time = formatTime(durationNanos);
        long bytes = Files.size(path);
        double throughput = 1000.0 * bytes / durationNanos;
        System.out.printf("Found %d unique IPs in %s (%.2f MB/s)%n", uniqueIps, time, throughput);
    }

    private static String formatTime(long nanos) {
        if (nanos >= 1_000_000_000) {
            return String.format("%d seconds", nanos / 1_000_000_000);
        }

        if (nanos >= 1_000_000) {
            return String.format("%d milliseconds", nanos / 1_000_000);
        }

        if (nanos >= 1000) {
            return String.format("%d microseconds", nanos / 1000);
        }

        return String.format("%d nanoseconds", nanos);
    }

}