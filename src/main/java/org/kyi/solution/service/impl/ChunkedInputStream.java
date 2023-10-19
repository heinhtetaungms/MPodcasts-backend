package org.kyi.solution.service.impl;

import java.io.IOException;
import java.io.InputStream;

public class ChunkedInputStream extends InputStream {
    private InputStream originalInputStream;
    private long start;
    private long remainingBytes;

    public ChunkedInputStream(InputStream inputStream, long start, long length) {
        this.originalInputStream = inputStream;
        this.start = start;
        this.remainingBytes = length;
    }

    @Override
    public int read() throws IOException {
        if (remainingBytes <= 0) {
            return -1; // No more data to read
        }

        if (start > 0) {
            long skipped = originalInputStream.skip(start);
            if (skipped < start) {
                // Handle skipped bytes less than expected (e.g., end of stream reached)
                return -1;
            }
            start = 0; // Reset start position
        }

        int byteRead = originalInputStream.read();
        if (byteRead != -1) {
            remainingBytes--;
        }
        return byteRead;
    }

    @Override
    public void close() throws IOException {
        originalInputStream.close();
    }
}
