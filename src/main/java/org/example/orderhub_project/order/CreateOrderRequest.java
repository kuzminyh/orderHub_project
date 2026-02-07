package org.example.orderhub_project.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        @NotEmpty(message = "Не должен быть пустым")
        List<OrderItemRequest> items
) {

    public record OrderItemRequest(
            @NotEmpty(message = "Не должен быть пустым")
            Long productId,

            @NotBlank(message = "Обязательно имя")
            String productName,

            @Min(value = 1)
            int quantity,

            @NotNull(message = "price must be")
            @DecimalMin(value = "0.01", message = "price > 0 must be")
            BigDecimal price) {

    }

}
