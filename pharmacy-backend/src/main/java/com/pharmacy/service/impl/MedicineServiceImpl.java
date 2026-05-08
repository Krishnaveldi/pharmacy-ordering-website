package com.pharmacy.service.impl;

import com.pharmacy.dto.MedicineRequest;
import com.pharmacy.dto.MedicineResponse;
import com.pharmacy.entity.Medicine;
import com.pharmacy.entity.MedicineCategory;
import com.pharmacy.repository.MedicineCategoryRepository;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineCategoryRepository categoryRepository;

    @Override
    public MedicineResponse createMedicine(MedicineRequest request) {
        MedicineCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Medicine medicine = Medicine.builder()
                .name(request.getName())
                .description(request.getDescription())
                .manufacturer(request.getManufacturer())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .dosage(request.getDosage())
                .requiresPrescription(request.getRequiresPrescription())
                .expiryDate(request.getExpiryDate())
                .imageUrl(request.getImageUrl())
                .category(category)
                .isActive(true) // Ensure it starts as active
                .build();

        return mapToResponse(medicineRepository.save(medicine));
    }

    @Override
    public MedicineResponse updateMedicine(Long id, MedicineRequest request) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        MedicineCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        medicine.setName(request.getName());
        medicine.setDescription(request.getDescription());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setPrice(request.getPrice());
        medicine.setStockQuantity(request.getStockQuantity());
        medicine.setDosage(request.getDosage());
        medicine.setRequiresPrescription(request.getRequiresPrescription());
        medicine.setExpiryDate(request.getExpiryDate());
        medicine.setImageUrl(request.getImageUrl());
        medicine.setCategory(category);

        return mapToResponse(medicineRepository.save(medicine));
    }

    @Override
    public void deleteMedicine(Long id) {
        // SOFT DELETE: Instead of medicineRepository.delete(), we toggle isActive
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        medicine.setIsActive(false); // Make it inactive instead of deleting
        medicineRepository.save(medicine);
    }

    @Override
    public MedicineResponse getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
        return mapToResponse(medicine);
    }

    @Override
    public Page<MedicineResponse> getAllMedicines(int page, int size) {
        // You can change this to only find active medicines:
        // medicineRepository.findByIsActiveTrue(PageRequest.of(page, size))
        return medicineRepository.findAll(PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    @Override
    public Page<MedicineResponse> searchMedicines(String keyword, int page, int size) {
        return medicineRepository
                .findByNameContainingIgnoreCase(keyword, PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    @Override
    public Page<MedicineResponse> getMedicinesByCategory(String category, int page, int size) {
        return medicineRepository
                .findByCategory_Name(category, PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    private MedicineResponse mapToResponse(Medicine medicine) {
        // Determine stock status for the response
        String stockStatus = (medicine.getStockQuantity() != null && medicine.getStockQuantity() > 0)
                ? "IN_STOCK" : "OUT_OF_STOCK";

        return MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .description(medicine.getDescription())
                .manufacturer(medicine.getManufacturer())
                .price(medicine.getPrice())
                .stockQuantity(medicine.getStockQuantity())
                .dosage(medicine.getDosage())
                .requiresPrescription(medicine.getRequiresPrescription())
                .expiryDate(medicine.getExpiryDate())
                .imageUrl(medicine.getImageUrl())
                .categoryName(medicine.getCategory().getName())
                // You should add these two fields to your MedicineResponse DTO:
                // .isActive(medicine.getIsActive())
                // .status(stockStatus)
                .build();
    }
}