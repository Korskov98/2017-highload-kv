package ru.mail.polis.korskov;

import org.jetbrains.annotations.NotNull;
import java.io.*;
import java.util.NoSuchElementException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyFileDAO implements MyDAO {
    @NotNull
    private final String dir;

    public MyFileDAO(@NotNull final String dir) {
        this.dir = dir;
    }

    @NotNull
    private File getFile(@NotNull final String key) throws IOException {
        return new File(dir, key);
    }

    @NotNull
    @Override
    public byte[] get(@NotNull final String key) throws NoSuchElementException, IllegalArgumentException, IOException {
        return Files.readAllBytes(Paths.get(dir, key));
    }

    @Override
    public void upsert(@NotNull final String key, @NotNull final byte[] value) throws IllegalArgumentException, IOException {
        try (OutputStream os = new FileOutputStream(getFile(key))){
            os.write(value);
        }
    }

    @NotNull
    @Override
    public void delete(@NotNull final String key) throws IllegalArgumentException, IOException {
        getFile(key).delete();
    }
}
