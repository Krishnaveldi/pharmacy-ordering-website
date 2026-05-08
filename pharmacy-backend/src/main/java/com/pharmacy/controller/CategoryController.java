package com.pharmacy.controller;

import com.pharmacy.dto.CategoryRequest;
import com.pharmacy.entity.MedicineCategory;
import com.pharmacy.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<MedicineCategory> createCategory(
            @RequestBody CategoryRequest request
    ) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @GetMapping
    public ResponseEntity<List<MedicineCategory>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}