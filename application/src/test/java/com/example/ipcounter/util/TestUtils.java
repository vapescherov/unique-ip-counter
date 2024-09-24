package com.example.ipcounter.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtils {

    public static int ip(int s1, int s2, int s3, int s4) {
        return (s1 << 24) | (s2 << 16) | (s3 << 8) | s4;
    }

    public static Path createTempFile(String text) throws IOException {
        Path tempFile = Files.createTempFile("test-ips", ".txt");
        tempFile.toFile().deleteOnExit();
        Files.writeString(tempFile, text, StandardCharsets.US_ASCII);
        return tempFile;
    }

}
