package com.techphantomexample.OrderReciever.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.techphantomexample.OrderReciever.entity.Order;
import com.techphantomexample.OrderReciever.entity.OrderStatus;
import com.techphantomexample.OrderReciever.exception.OrderNotFoundException;
import com.techphantomexample.OrderReciever.messege.UpdateStatus;
import com.techphantomexample.OrderReciever.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UpdateStatus updateStatus;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(int orderId, OrderStatus status) throws JsonProcessingException {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            updateStatus.sendStatusUpdate(orderId,status);
            return orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found");
        }
    }

}
