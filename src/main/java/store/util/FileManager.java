package store.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import store.constant.ExceptionMessage;

public class FileManager {
    public static Queue<String> read(final String path) {
        try {
            return readLines(path);
        } catch (IOException e) {
            throw new IllegalStateException(ExceptionMessage.INVALID_DATA_FILE.getMessage());
        }
    }

    private static Queue<String> readLines(final String path) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            return new LinkedList<>(bufferedReader.lines().toList());
        }
    }

    public static void removeHeader(final Queue<String> fileContents) {
        fileContents.poll();
    }
}
