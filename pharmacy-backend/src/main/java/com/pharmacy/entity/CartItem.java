package com.pharmacy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private Integer quantity;

    private BigDecimal price;
}