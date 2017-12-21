package ru.mail.polis.Korskov;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileReader {
    private static final int SIZE = 1024;
    private final InputStream inputStream;

    public FileReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public byte[] read() throws IOException {
        byte[] buffer = new byte[SIZE];
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int numberBytes = inputStream.read(buffer);
            while (numberBytes != -1) {
                out.write(buffer, 0, numberBytes);
            }
            out.flush();
            return out.toByteArray();
        }
    }
}
