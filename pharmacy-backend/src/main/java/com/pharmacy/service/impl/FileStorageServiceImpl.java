package com.pharmacy.service.impl;

import com.pharmacy.exception.BadRequestException;
import com.pharmacy.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String uploadFile(MultipartFile file) {

        // FILE TYPE VALIDATION
        if (!file.getContentType().equals("application/pdf")) {
            throw new BadRequestException("Only PDF files allowed");
        }

        // FILE SIZE VALIDATION
        if (file.getSize() > 5_000_000) {
            throw new BadRequestException("File size exceeds limit");
        }

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String filename = UUID.randomUUID()
                    + "_"
                    + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(filename);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return filePath.toString();

        } catch (IOException e) {

            throw new RuntimeException("Failed to upload file");
        }
    }
}