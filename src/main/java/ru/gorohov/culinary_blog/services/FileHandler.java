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
            log.error("Неверный путь к файлу: {}", fileName);
            return null;
        }

        try {
            Path uploadPath = getUploadPath();
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Файл успешно сохранен: {}", fileName);
            return fileName;
        } catch (IOException e) {
            log.error("Ошибка сохранения файла: {}", e.getMessage(), e);
            return null;
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = getFilePath(fileName);
            if (Files.deleteIfExists(filePath)) {
                log.info("Файл успешно удален: {}", fileName);
            } else {
                log.warn("Файл не найден: {}", fileName);
            }
        } catch (IOException e) {
            log.error("Невозможно удалить файл: {} - {}", fileName, e.getMessage(), e);
        }
    }

    private Path getUploadPath() {
        return Paths.get(uploadDir);
    }

    private Path getFilePath(String fileName) {
        return getUploadPath().resolve(fileName);
    }
}
