package com.pharmacy.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class MedicineResponse {

    private Long id;

    private String name;

    private String description;

    private String manufacturer;

    private BigDecimal price;

    private Integer stockQuantity;

    private String dosage;

    private Boolean requiresPrescription;

    private LocalDate expiryDate;

    private LocalDate manufactureDate;

    private String imageUrl;

    private String categoryName;
}