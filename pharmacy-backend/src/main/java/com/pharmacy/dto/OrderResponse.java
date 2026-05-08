package com.pharmacy.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private Long orderId;

    private BigDecimal totalAmount;

    private String status;

    private String deliveryAddress;

    private Integer loyaltyPointsEarned;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;
}