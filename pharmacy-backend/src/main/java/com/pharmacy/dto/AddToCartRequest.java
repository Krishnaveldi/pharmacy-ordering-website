package com.pharmacy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartRequest {

    @NotNull
    private Long medicineId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}