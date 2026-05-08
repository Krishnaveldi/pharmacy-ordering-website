package com.pharmacy.service;

import com.pharmacy.entity.Prescription;

public interface PrescriptionService {

    Prescription uploadPrescription(String fileUrl);

    Prescription approvePrescription(Long id);

    java.util.List<Prescription> getAllPrescriptions();
}