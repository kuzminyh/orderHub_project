package org.example.orderhub_project.order;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.example.orderhub_project.order.exception.NotFoundOrderException;
import org.example.orderhub_project.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@NoArgsConstructor(force = true)
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    @Autowired
    private  OrderRepository orderRepository;

//    public OrderService(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }


    public Order createOrder(CreateOrderRequest request) {
        log.debug("В метод Create Order получен запрос{}",request);

        List<OrderItem> items = request.items().stream()
                .map(item -> new OrderItem(
                        item.productId(),
                        item.productName(),
                        item.quantity(),
                        item.price()
                ))
                .toList();

        Order order = new Order(items);

        log.debug("все успешно сохранено");

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order getOrderWithItems(Long id) {

        log.debug("В метод findOrderById получен запрос по ордеру id: {} ", id);

       return orderRepository.findWithItemsById(id).orElseThrow(
                () -> new NotFoundOrderException("Order not found")
        );

    }
}
