package com.example.ipcounter.counter;

import java.io.IOException;
import java.nio.file.Path;

public interface UniqueIPCounter {
    long countUniqueIPs(Path path) throws IOException;
}
