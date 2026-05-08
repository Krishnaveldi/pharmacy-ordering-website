package com.pharmacy.service;

import com.pharmacy.dto.OrderRequest;
import com.pharmacy.entity.Order;

import java.util.List;

public interface OrderService {

    Order placeOrder(OrderRequest request);

    List<Order> getMyOrders();
}