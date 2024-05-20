package com.example.jpashopp.repository;

import com.example.jpashopp.domain.orders.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem    , Long> {
}
