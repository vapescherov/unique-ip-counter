package com.example.ipcounter.reader;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.IntStream;

import static com.example.ipcounter.util.TestUtils.createTempFile;
import static com.example.ipcounter.util.TestUtils.ip;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BytesIPv4ReaderTest {

    private final IPv4Reader reader = new BytesIPv4Reader(8 * 1024);

    @Test
    void emptyFile_ShouldReturnArrayWithZeroSize() throws IOException {
        Path file = createTempFile("");

        try (IntStream ips = reader.ipAddressStream(file)) {
            int[] actual = ips.toArray();
            assertEquals(0, actual.length);
        }
    }

    @Test
    void singleIP_ShouldReturnArrayWithSingleElement() throws IOException {
        Path file = createTempFile("192.168.1.1");

        try (IntStream ips = reader.ipAddressStream(file)) {
            int[] actual = ips.toArray();
            assertArrayEquals(new int[]{ip(192, 168, 1, 1)}, actual);
        }
    }

    @Test
    void singleIPAndEndLine_ShouldReturnArrayWithSingleElement() throws IOException {
        Path file = createTempFile("192.168.1.1\n");

        try (IntStream ips = reader.ipAddressStream(file)) {
            int[] actual = ips.toArray();
            assertArrayEquals(new int[]{ip(192, 168, 1, 1)}, actual);
        }
    }

    @Test
    void multipleIPs_ShouldReturnArrayWithMultipleElements() throws IOException {
        Path file = createTempFile("""
                1.1.1.1
                192.168.1.1
                255.255.255.255
                """);

        try (IntStream ips = reader.ipAddressStream(file)) {
            int[] actual = ips.toArray();
            assertArrayEquals(new int[]{
                    ip(1, 1, 1, 1),
                    ip(192, 168, 1, 1),
                    ip(255, 255, 255, 255)
            }, actual);
        }
    }


}

