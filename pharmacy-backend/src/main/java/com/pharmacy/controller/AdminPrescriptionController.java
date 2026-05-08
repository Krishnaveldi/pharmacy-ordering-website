package com.pharmacy.controller;

import com.pharmacy.entity.Prescription;
import com.pharmacy.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/prescriptions")
@RequiredArgsConstructor
public class AdminPrescriptionController {

    private final PrescriptionService prescriptionService;

    @PutMapping("/{id}/approve")
    public ResponseEntity<Prescription> approvePrescription(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                prescriptionService.approvePrescription(id)
        );
    }

    @GetMapping
    public ResponseEntity<java.util.List<Prescription>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions());
    }
}