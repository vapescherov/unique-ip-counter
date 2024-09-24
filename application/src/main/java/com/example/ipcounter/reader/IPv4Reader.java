package com.example.ipcounter.reader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.IntStream;

public interface IPv4Reader {
    IntStream ipAddressStream(Path path) throws IOException;
}
