package com.example.ipcounter.reader;

import com.example.ipcounter.builder.DefaultIPAddressBuilder;
import com.example.ipcounter.builder.IPAddressBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class BytesIPv4Reader implements IPv4Reader {

    private final int bufferSize;

    public BytesIPv4Reader(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public IntStream ipAddressStream(Path path) throws IOException {
        InputStream bis = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ));
        Spliterator.OfInt spliterator = new IPv4InputStreamSpliterator(bis, bufferSize);
        return StreamSupport.intStream(spliterator, false)
                .onClose(() -> {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static class IPv4InputStreamSpliterator extends Spliterators.AbstractIntSpliterator {
        private final InputStream inputStream;
        private final IPAddressBuilder ipAddressBuilder;
        private final int bufferSize;

        private byte[] buffer;
        private int index;
        private int bytesInBuffer;

        public IPv4InputStreamSpliterator(InputStream inputStream, int bufferSize) {
            super(Long.MAX_VALUE, Spliterator.NONNULL | Spliterator.ORDERED);
            this.inputStream = inputStream;
            this.bufferSize = bufferSize;
            this.ipAddressBuilder = new DefaultIPAddressBuilder();
            this.index = 0;
            this.bytesInBuffer = 0;
        }

        @Override
        public boolean tryAdvance(IntConsumer action) {
            if (buffer == null) {
                buffer = new byte[bufferSize];
            }

            while ((index < bytesInBuffer) || loadBuffer() > 0) {
                byte c = buffer[index];
                index++;
                if (c != '\n') {
                    ipAddressBuilder.append(c);
                } else {
                    action.accept(ipAddressBuilder.build());
                    return true;
                }
            }

            if (ipAddressBuilder.hasUncommittedData()) {
                action.accept(ipAddressBuilder.build());
                return true;
            }

            return false;
        }

        private int loadBuffer() {
            try {
                int bytesRead = inputStream.read(buffer);
                bytesInBuffer = bytesRead;
                index = 0;
                return bytesRead;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
