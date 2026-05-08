package com.pharmacy.service;

import com.pharmacy.dto.MedicineRequest;
import com.pharmacy.dto.MedicineResponse;
import org.springframework.data.domain.Page;

public interface MedicineService {

    MedicineResponse createMedicine(MedicineRequest request);

    MedicineResponse updateMedicine(Long id, MedicineRequest request);

    void deleteMedicine(Long id);

    MedicineResponse getMedicineById(Long id);

    Page<MedicineResponse> getAllMedicines(int page, int size);

    Page<MedicineResponse> searchMedicines(String keyword, int page, int size);

    Page<MedicineResponse> getMedicinesByCategory(String category, int page, int size);
}