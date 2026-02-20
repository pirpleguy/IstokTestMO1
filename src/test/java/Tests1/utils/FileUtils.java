package Tests1.utils;

import com.microsoft.playwright.Download;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static void saveDownloadedFile(Download download, String downloadDir) throws IOException {
        Path sourcePath = download.path();
        String fileName = download.suggestedFilename();
        Path targetPath = Paths.get(downloadDir, fileName);

        Files.createDirectories(Paths.get(downloadDir));
        Files.copy(sourcePath, targetPath);
    }

    public static void clearDirectory(String dirPath) throws IOException {
        Path directory = Paths.get(dirPath);
        if (Files.exists(directory)) {
            Files.list(directory).forEach(file -> {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    System.err.println("Не удалось удалить файл: " + file);
                }
            });
        }
    }
}