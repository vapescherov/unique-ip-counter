package com.example.ipcounter.counter;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static com.example.ipcounter.util.TestUtils.createTempFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ByteBufferUniqueIPCounterTest {

    private final UniqueIPCounter uniqueIPCounter = new ByteBufferUniqueIPCounter(8 * 1024);

    @Test
    void countUniqueIPs_WithSingleIPAndNewline_ShouldReturnOne() throws IOException {
        Path path = createTempFile("192.168.1.1\n");

        long result = uniqueIPCounter.countUniqueIPs(path);

        assertEquals(1, result);
    }

    @Test
    void countUniqueIPs_WithSingleIP_ShouldReturnOne() throws IOException {
        Path path = createTempFile("192.168.1.1");

        long result = uniqueIPCounter.countUniqueIPs(path);

        assertEquals(1, result);
    }

    @Test
    void countUniqueIPs_WithEmptyFile_ShouldReturnZero() throws IOException {
        Path path = createTempFile("");

        long result = uniqueIPCounter.countUniqueIPs(path);

        assertEquals(0, result);
    }

    @Test
    void countUniqueIPs_WithInvalidIP_ShouldThrowException() throws IOException {
        Path path = createTempFile("192");

        assertThrows(Exception.class, () -> uniqueIPCounter.countUniqueIPs(path));
    }

    @Test
    void countUniqueIPs_WithIPSegmentOutOfRange_ShouldThrowException() throws IOException {
        Path path = createTempFile("192.256.1.8");

        assertThrows(Exception.class, () -> uniqueIPCounter.countUniqueIPs(path));
    }

    @Test
    void countUniqueIPs_WithMultipleValidIPs_ShouldReturnUniqueIPs() throws IOException {
        Path path = createTempFile("""
                192.168.1.1
                255.255.255.255
                0.0.0.0
                """);

        long result = uniqueIPCounter.countUniqueIPs(path);

        assertEquals(3, result);
    }

    @Test
    void countUniqueIPs_WithRepeatedIPs_ShouldReturnUniqueIPs() throws IOException {
        Path path = createTempFile("""
                192.168.1.1
                255.255.255.255
                0.0.0.0
                192.168.1.1
                0.0.0.0
                255.255.255.255
                """);

        long result = uniqueIPCounter.countUniqueIPs(path);

        assertEquals(3, result);
    }

}
