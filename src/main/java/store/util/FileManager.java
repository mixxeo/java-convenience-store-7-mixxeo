package store.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static List<String> read(final String path) {
        try {
            return readLines(path);
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    private static List<String> readLines(final String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static void removeHeader(final List<String> fileContents) {
        fileContents.removeFirst();
    }
}
