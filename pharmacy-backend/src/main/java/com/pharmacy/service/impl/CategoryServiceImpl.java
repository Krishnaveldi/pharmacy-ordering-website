package com.pharmacy.service.impl;

import com.pharmacy.dto.CategoryRequest;
import com.pharmacy.entity.MedicineCategory;
import com.pharmacy.repository.MedicineCategoryRepository;
import com.pharmacy.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final MedicineCategoryRepository categoryRepository;

    @Override
    public MedicineCategory createCategory(CategoryRequest request) {

        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category already exists");
        }

        MedicineCategory category = MedicineCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .age(request.getAge())
                .build();

        return categoryRepository.save(category);
    }

    @Override
    public List<MedicineCategory> getAllCategories() {
        return categoryRepository.findAll();
    }
}