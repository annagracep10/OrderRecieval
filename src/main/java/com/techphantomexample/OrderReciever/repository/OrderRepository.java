package com.techphantomexample.OrderReciever.repository;

import com.techphantomexample.OrderReciever.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
