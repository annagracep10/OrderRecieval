package com.techphantomexample.OrderReciever.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techphantomexample.OrderReciever.entity.Order;
import com.techphantomexample.OrderReciever.entity.OrderStatus;
import com.techphantomexample.OrderReciever.exception.OrderNotFoundException;
import com.techphantomexample.OrderReciever.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCancellationListener {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @JmsListener(destination = "${spring.activemq.destination.cancel-order}")
    public void receiveCancellationMessage(String cancellationMessageJson) throws JsonProcessingException {

        Integer orderId = objectMapper.readValue(cancellationMessageJson, Integer.class);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}
