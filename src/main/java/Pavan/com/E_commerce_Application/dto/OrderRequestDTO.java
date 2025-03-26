package Pavan.com.E_commerce_Application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class OrderRequestDTO {
    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private Set<OrderItemDTO> orderItems;
} 