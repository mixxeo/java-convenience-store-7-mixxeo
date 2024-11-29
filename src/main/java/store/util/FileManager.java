package store.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import store.constant.ExceptionMessage;

public class FileManager {
    public static List<String> read(final String path) {
        try {
            return readLines(path);
        } catch (IOException e) {
            throw new IllegalStateException(ExceptionMessage.INVALID_DATA_FILE.getMessage());
        }
    }

    private static List<String> readLines(final String path) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            return bufferedReader.lines().collect(Collectors.toList());
        }
    }

    public static void removeHeader(final List<String> fileContents) {
        fileContents.removeFirst();
    }
}
