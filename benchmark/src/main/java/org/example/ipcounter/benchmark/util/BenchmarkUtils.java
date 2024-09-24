package org.example.ipcounter.benchmark.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class BenchmarkUtils {

    public static Path generateTempFile(int count) throws IOException {
        Random random = new Random();

        Path path = Files.createTempFile("benchmark-ips", ".txt");
        path.toFile().deleteOnExit();

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.US_ASCII)) {
            for (int i = 0; i < count; i++) {
                int ipv4 = random.nextInt();
                String line = intToIPv4(ipv4);
                writer.write(line);
                writer.newLine();
            }
        }

        return path;
    }

    private static String intToIPv4(int ipv4) {
        StringBuilder result = new StringBuilder();
        for (int i = 3; i >= 0; i--) {
            int octet = (ipv4 >> (i * 8)) & 0xFF;
            result.append(octet);
            if (i > 0) {
                result.append(".");
            }
        }
        return result.toString();
    }

}
