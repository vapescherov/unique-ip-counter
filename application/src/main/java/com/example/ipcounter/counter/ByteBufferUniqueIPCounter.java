package com.example.ipcounter.counter;

import com.example.ipcounter.builder.DefaultIPAddressBuilder;
import com.example.ipcounter.builder.IPAddressBuilder;
import com.example.ipcounter.intset.BitSetBasedIntSet;
import com.example.ipcounter.intset.IntSet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ByteBufferUniqueIPCounter implements UniqueIPCounter {

    private final int bufferSize;
    private final IPAddressBuilder ipAddressBuilder;

    public ByteBufferUniqueIPCounter(int bufferSize) {
        this.bufferSize = bufferSize;
        this.ipAddressBuilder = new DefaultIPAddressBuilder();
    }

    @Override
    public long countUniqueIPs(Path path) throws IOException {
        try (InputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
            IntSet uniqueIPs = new BitSetBasedIntSet();

            byte[] buffer = new byte[bufferSize];

            int bytesRead;
            while ((bytesRead = is.read(buffer)) > 0) {
                for (int i = 0; i < bytesRead; i++) {
                    byte currentByte = buffer[i];
                    if (currentByte != '\n') {
                        ipAddressBuilder.append(currentByte);
                    } else {
                        int ip = ipAddressBuilder.build();
                        uniqueIPs.add(ip);
                    }
                }
            }

            if (ipAddressBuilder.hasUncommittedData()) {
                uniqueIPs.add(ipAddressBuilder.build());
            }

            return uniqueIPs.size();
        }
    }

}
