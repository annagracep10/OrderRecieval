package com.techphantomexample.OrderReciever.controller;

import com.techphantomexample.OrderReciever.entity.Order;
import com.techphantomexample.OrderReciever.entity.OrderStatus;
import com.techphantomexample.OrderReciever.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("update/{orderId}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable int orderId, @RequestBody OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }



}
