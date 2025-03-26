package Pavan.com.E_commerce_Application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import Pavan.com.E_commerce_Application.dto.OrderItemDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "orders")
@EqualsAndHashCode(exclude = {"user", "orderItems", "shippingAddress"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"orders", "addresses", "wishlist"})
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"order", "product"})
    private Set<OrderItem> orderItems = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address_id", nullable = false)
    @JsonIgnoreProperties({"orders", "user"})
    private Address shippingAddress;

    @Column
    private String trackingNumber;

    // Override the default getter for orderItems to include product details
    @JsonIgnoreProperties({"order", "product"})
    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    // Add a setter for order items that accepts OrderItems
    public void setOrderItems(Set<OrderItem> items) {
        this.orderItems = items;
    }

    // Add a method to get order items as DTOs
    @Transient
    public Set<OrderItemDTO> getOrderItemsAsDTOs() {
        Set<OrderItemDTO> items = new HashSet<>();
        for (OrderItem item : orderItems) {
            OrderItemDTO dto = new OrderItemDTO();
            dto.setId(item.getId());
            dto.setProductId(item.getProduct().getId());
            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getPrice());
            dto.setSubtotal(item.getSubtotal());
            dto.setProduct(item.getProductDetails());
            items.add(dto);
        }
        return items;
    }
}
