package com.pharmacy.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {

    private Long itemId;

    private Long medicineId;

    private String medicineName;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;
}