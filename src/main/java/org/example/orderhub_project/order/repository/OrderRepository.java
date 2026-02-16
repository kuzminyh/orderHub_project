package org.example.orderhub_project.order.repository;

import org.example.orderhub_project.order.Order;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = { "items" })
    Optional<Order> findWithItemsById(long id);


}
