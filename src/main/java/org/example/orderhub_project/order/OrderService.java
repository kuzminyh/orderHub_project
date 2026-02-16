package org.example.orderhub_project.order;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.orderhub_project.order.exception.NotFoundOrderException;
import org.example.orderhub_project.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(CreateOrderRequest request) {
        List<OrderItem> items = request.items().stream()
                .map(item -> new OrderItem(
                        item.productId(),
                        item.productName(),
                        item.quantity(),
                        item.price()
                ))
                .toList();

        Order order = new Order(items);

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order findOrderById(Long id) {

       return orderRepository.findById(id).orElseThrow(
                () -> new NotFoundOrderException("Order not found")
        );

    }
}
