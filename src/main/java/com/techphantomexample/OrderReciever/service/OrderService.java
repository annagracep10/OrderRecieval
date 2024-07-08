package com.techphantomexample.OrderReciever.service;

import com.techphantomexample.OrderReciever.entity.Order;
import com.techphantomexample.OrderReciever.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
