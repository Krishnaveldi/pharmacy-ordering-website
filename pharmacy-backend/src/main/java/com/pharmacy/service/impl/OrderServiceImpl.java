package com.pharmacy.service.impl;

import com.pharmacy.dto.OrderRequest;
import com.pharmacy.entity.*;
import com.pharmacy.repository.*;
import com.pharmacy.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    @Transactional
    public Order placeOrder(OrderRequest request) {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        Order order = Order.builder()
                .user(user)
                .deliveryAddress(request.getDeliveryAddress())
                .status(OrderStatus.CONFIRMED)
                .items(new ArrayList<>())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {

            Medicine medicine = cartItem.getMedicine();

            if (medicine.getRequiresPrescription()) {

                boolean approvedPrescription = prescriptionRepository
                        .existsByUserAndStatus(user, PrescriptionStatus.APPROVED);

                if (!approvedPrescription) {
                    throw new RuntimeException(
                            "Approved prescription required for: " + medicine.getName()
                    );
                }
            }

            if (medicine.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for: " + medicine.getName()
                );
            }

            medicine.setStockQuantity(
                    medicine.getStockQuantity() - cartItem.getQuantity()
            );

            medicineRepository.save(medicine);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .medicine(medicine)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(medicine.getPrice())
                    .build();

            orderItems.add(orderItem);

            totalAmount = totalAmount.add(
                    medicine.getPrice().multiply(
                            BigDecimal.valueOf(cartItem.getQuantity())
                    )
            );
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setLoyaltyPointsEarned(totalAmount.intValue() / 100);

        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    @Override
    public List<Order> getMyOrders() {

        User user = getCurrentUser();

        return orderRepository.findByUser(user);
    }
    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
