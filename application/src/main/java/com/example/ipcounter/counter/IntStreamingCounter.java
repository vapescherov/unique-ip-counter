package com.example.ipcounter.counter;

import com.example.ipcounter.intset.BitSetBasedIntSet;
import com.example.ipcounter.intset.IntSet;
import com.example.ipcounter.reader.IPv4Reader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.IntStream;

public class IntStreamingCounter implements UniqueIPCounter {

    private final IPv4Reader reader;

    public IntStreamingCounter(IPv4Reader reader) {
        this.reader = reader;
    }

    @Override
    public long countUniqueIPs(Path path) throws IOException {
        try (IntStream stream = reader.ipAddressStream(path)) {
            IntSet uniqueIPs = new BitSetBasedIntSet();
            stream.forEach(uniqueIPs::add);
            return uniqueIPs.size();
        }
    }

}
