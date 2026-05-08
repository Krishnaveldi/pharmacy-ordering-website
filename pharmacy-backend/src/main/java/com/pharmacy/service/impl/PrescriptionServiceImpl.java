package com.pharmacy.service.impl;

import com.pharmacy.entity.*;
import com.pharmacy.repository.PrescriptionRepository;
import com.pharmacy.repository.UserRepository;
import com.pharmacy.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;

    @Override
    public Prescription uploadPrescription(String fileUrl) {

        User user = getCurrentUser();

        Prescription prescription = Prescription.builder()
                .user(user)
                .fileUrl(fileUrl)
                .status(PrescriptionStatus.PENDING)
                .build();

        return prescriptionRepository.save(prescription);
    }

    @Override
    public Prescription approvePrescription(Long id) {

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        prescription.setStatus(PrescriptionStatus.APPROVED);

        return prescriptionRepository.save(prescription);
    }

    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}