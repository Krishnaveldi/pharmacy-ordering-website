package com.pharmacy.service;

import com.pharmacy.dto.CategoryRequest;
import com.pharmacy.entity.MedicineCategory;

import java.util.List;

public interface CategoryService {

    MedicineCategory createCategory(CategoryRequest request);

    List<MedicineCategory> getAllCategories();
}