package com.pharmacy.controller;

import com.pharmacy.dto.CategoryRequest;
import com.pharmacy.entity.MedicineCategory;
import com.pharmacy.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<MedicineCategory> createCategory(
            @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }
}