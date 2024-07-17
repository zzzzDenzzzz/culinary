package ru.gorohov.culinary_blog.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@Slf4j
public class FileHandler {
    private final String uploadDir;

    public FileHandler() {
        uploadDir = "src/main/resources/static/recipeImages";
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (fileName.contains("..")) {
            log.error("Invalid file path: {}", fileName);
            return null;
        }

        try {
            Path uploadPath = getUploadPath();
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File stored successfully: {}", fileName);
            return fileName;
        } catch (IOException e) {
            log.error("Error storing file: {}", e.getMessage(), e);
            return null;
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = getFilePath(fileName);
            if (Files.deleteIfExists(filePath)) {
                log.info("File deleted successfully: {}", fileName);
            } else {
                log.warn("File not found: {}", fileName);
            }
        } catch (IOException e) {
            log.error("Unable to delete file: {} - {}", fileName, e.getMessage(), e);
        }
    }

    private Path getUploadPath() {
        return Paths.get(uploadDir);
    }

    private Path getFilePath(String fileName) {
        return getUploadPath().resolve(fileName);
    }
}
