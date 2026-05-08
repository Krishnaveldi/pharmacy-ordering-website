package com.pharmacy.service.impl;

import com.pharmacy.dto.MedicineRequest;
import com.pharmacy.dto.MedicineResponse;
import com.pharmacy.entity.Medicine;
import com.pharmacy.entity.MedicineCategory;
import com.pharmacy.repository.MedicineCategoryRepository;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.service.AuditService; // Import the AuditService
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
    private final AuditService auditService; // 1. Inject AuditService as shown in image_532299.png

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
                //now added
                .manufactureDate(request.getManufactureDate())
                .imageUrl(request.getImageUrl())
                .category(category)
                .isActive(true)
                .build();

        // 2. Save the medicine first to get an ID for the audit log
        Medicine savedMedicine = medicineRepository.save(medicine);

        // 3. Log the creation event to the Audit Service as shown in image_532299.png
        auditService.log(
                "MEDICINE_CREATED",
                "Medicine",
                savedMedicine.getId(),
                "Medicine created: " + savedMedicine.getName()
        );

        return mapToResponse(savedMedicine);
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
        //now added
        medicine.setManufactureDate(request.getManufactureDate());
        medicine.setImageUrl(request.getImageUrl());
        medicine.setCategory(category);

        return mapToResponse(medicineRepository.save(medicine));
    }

    @Override
    public void deleteMedicine(Long id) {
        // 1. Find the medicine or throw error if it doesn't exist
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        // 2. Perform Soft Delete
        medicine.setIsActive(false);
        medicineRepository.save(medicine);

        // 3. Log the deletion event to the Audit Service
        auditService.log(
                "MEDICINE_DELETED",
                "Medicine",
                medicine.getId(),
                "Medicine deleted: " + medicine.getName()
        );
    }

    @Override
    public MedicineResponse getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
        return mapToResponse(medicine);
    }

    @Override
    public Page<MedicineResponse> getAllMedicines(int page, int size) {
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
        return MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .description(medicine.getDescription())
                .manufacturer(medicine.getManufacturer())
                .price(medicine.getPrice())
                .stockQuantity(medicine.getStockQuantity())
                .dosage(medicine.getDosage())
                .requiresPrescription(medicine.getRequiresPrescription())
                .manufactureDate(medicine.getManufactureDate())
                .expiryDate(medicine.getExpiryDate())
                .imageUrl(medicine.getImageUrl())
                .categoryName(medicine.getCategory().getName())
                .build();
    }
}