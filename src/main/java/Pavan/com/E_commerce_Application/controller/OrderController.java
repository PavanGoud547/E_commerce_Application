package Pavan.com.E_commerce_Application.controller;

import Pavan.com.E_commerce_Application.Service.OrderService;
import Pavan.com.E_commerce_Application.dto.OrderRequestDTO;
import Pavan.com.E_commerce_Application.model.Order;
import Pavan.com.E_commerce_Application.model.OrderStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Page<Order>> getAllOrders(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or @orderService.isOrderOwner(#id, authentication.principal.id)")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> createOrder(
            @RequestParam Long userId,
            @Valid @RequestBody OrderRequestDTO request,
            @RequestParam Long shippingAddressId) {
        return ResponseEntity.ok(orderService.createOrder(userId, request.getOrderItems(), shippingAddressId));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRATOR')")

    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMINISTRATOR') or @orderService.isOrderOwner(#id, authentication.principal.id)")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or #userId == authentication.principal.id")
    public ResponseEntity<Page<Order>> getOrdersByUser(
            @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId, pageable));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Page<Order>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status, pageable));
    }

    @PutMapping("/{id}/tracking")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Order> updateTrackingNumber(
            @PathVariable Long id,
            @RequestParam String trackingNumber) {
        return ResponseEntity.ok(orderService.updateTrackingNumber(id, trackingNumber));
    }
} 