package com.pharmacy.service.impl;

import com.pharmacy.dto.OrderRequest;
import com.pharmacy.entity.*;
import com.pharmacy.repository.*;
import com.pharmacy.service.AuditService;
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
    private final AuditService auditService;

    @Override
    @Transactional
    public Order placeOrder(OrderRequest request) {
        User user = getCurrentUser();

        // 1. Fetch Cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. Initialize Order Object
        BigDecimal totalAmount = BigDecimal.ZERO;
        Order order = Order.builder()
                .user(user)
                .deliveryAddress(request.getDeliveryAddress())
                .status(OrderStatus.CONFIRMED)
                .items(new ArrayList<>())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();

        // 3. Process each item in the cart
        for (CartItem cartItem : cart.getItems()) {
            Medicine medicine = cartItem.getMedicine();

            // --- FIX: NULL-SAFE PRESCRIPTION CHECK ---
            // Boolean.TRUE.equals handles null by returning false instead of throwing NPE
            if (Boolean.TRUE.equals(medicine.getRequiresPrescription())) {
                boolean approvedPrescription = prescriptionRepository
                        .existsByUserAndStatus(user, PrescriptionStatus.APPROVED);
                if (!approvedPrescription) {
                    throw new RuntimeException("Approved prescription required for: " + medicine.getName());
                }
            }

            // 4. Stock Validation
            if (medicine.getStockQuantity() == null || medicine.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + medicine.getName());
            }

            // 5. Update Inventory
            medicine.setStockQuantity(medicine.getStockQuantity() - cartItem.getQuantity());
            medicineRepository.save(medicine);

            // 6. Audit Logging
            auditService.log(
                    "INVENTORY_UPDATED",
                    "Medicine",
                    medicine.getId(),
                    "Stock reduced by " + cartItem.getQuantity()
            );

            // 7. Create Order Item
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .medicine(medicine)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(medicine.getPrice())
                    .build();

            orderItems.add(orderItem);

            // 8. Calculate Total
            BigDecimal itemTotal = medicine.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        // 9. Finalize Order Details
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        // Example Loyalty Logic: 1 point for every 100 currency units
        order.setLoyaltyPointsEarned(totalAmount.divide(BigDecimal.valueOf(100)).intValue());

        Order savedOrder = orderRepository.save(order);

        // 10. Clear Cart after successful order
        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    @Override
    public List<Order> getMyOrders() {
        User user = getCurrentUser();
        return orderRepository.findByUser(user);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
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