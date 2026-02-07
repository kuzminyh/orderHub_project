package org.example.orderhub_project.order;

import io.micrometer.core.ipc.http.HttpSender;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
//@RequiredArgsConstructor
@RequestMapping
public class Controller {
    private final OrderService orderService;

    public Controller(OrderService orderService) {
        this.orderService = orderService;
    }

    public ResponseEntity<OrderResponse> createOrder(
            @Valid
            @RequestBody
            CreateOrderRequest request
    ) {
      Order order = orderService.createOrder(request);
      OrderResponse response = OrderResponse.from(order);
      return ResponseEntity.created(URI.create("/orders/"))
                      //+ order.getId()))
              .body(response);

    }
}
