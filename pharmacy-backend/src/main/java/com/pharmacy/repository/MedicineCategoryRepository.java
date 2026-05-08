package com.pharmacy.repository;

import com.pharmacy.entity.MedicineCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineCategoryRepository
        extends JpaRepository<MedicineCategory, Long> {

    boolean existsByName(String name);
}