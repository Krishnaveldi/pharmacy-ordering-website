package com.pharmacy.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MedicineRequest {

    @NotBlank(message = "Medicine name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stockQuantity;

    @NotBlank(message = "Dosage is required")
    private String dosage;

    @NotNull(message = "Prescription requirement is mandatory")
    private Boolean requiresPrescription;

    @Future(message = "Expiry date must be in future")
    private LocalDate expiryDate;

    @Future(message = "Expiry date must be in future")
    private LocalDate manufactureDate;

    private String imageUrl;

    @NotNull(message = "Category is required")
    private Long categoryId;
}