package com.pharmacy.controller;

import com.pharmacy.entity.Prescription;
import com.pharmacy.service.FileStorageService;
import com.pharmacy.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<Prescription> uploadPrescription(
            @RequestParam MultipartFile file
    ) {

        String fileUrl = fileStorageService.uploadFile(file);

        return ResponseEntity.ok(
                prescriptionService.uploadPrescription(fileUrl)
        );
    }
}