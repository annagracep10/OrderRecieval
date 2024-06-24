package com.techphantomexample.OrderReciever.repository;

import com.techphantomexample.OrderReciever.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
