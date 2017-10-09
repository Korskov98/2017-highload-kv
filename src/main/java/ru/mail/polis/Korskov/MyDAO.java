package ru.mail.polis.Korskov;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Created by Aleksey on 08.10.2017.
 */
public interface MyDAO {
    @NotNull
    byte[] get(@NotNull String key) throws NoSuchElementException, IllegalArgumentException, IOException;
    void upsert(@NotNull String key, @NotNull byte[] value) throws IllegalArgumentException, IOException;
    @NotNull
    void delete(@NotNull String key) throws IllegalArgumentException, IOException;
}
