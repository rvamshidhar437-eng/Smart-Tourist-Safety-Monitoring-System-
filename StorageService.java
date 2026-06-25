package com.touristsafety.service;

import com.touristsafety.exception.BadRequestException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    private final Path uploadDir;

    public StorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.uploadDir = Path.of(uploadDir).toAbsolutePath().normalize();
    }

    public String storeEvidence(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            Files.createDirectories(uploadDir);
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() == null
                    ? "evidence"
                    : file.getOriginalFilename());
            String filename = UUID.randomUUID() + "-" + originalFilename;
            Path destination = uploadDir.resolve(filename).normalize();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return destination.toString();
        } catch (IOException ex) {
            throw new BadRequestException("Could not store evidence file.");
        }
    }
}
