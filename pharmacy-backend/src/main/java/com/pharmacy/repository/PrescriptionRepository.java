package com.pharmacy.repository;

import com.pharmacy.entity.Prescription;
import com.pharmacy.entity.PrescriptionStatus;
import com.pharmacy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    boolean existsByUserAndStatus(User user, PrescriptionStatus status);
}