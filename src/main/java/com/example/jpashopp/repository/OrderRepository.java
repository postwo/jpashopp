package com.example.jpashopp.repository;

import com.example.jpashopp.domain.orders.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}