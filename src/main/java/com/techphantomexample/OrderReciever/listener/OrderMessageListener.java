package com.techphantomexample.OrderReciever.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techphantomexample.OrderReciever.entity.Order;
import com.techphantomexample.OrderReciever.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderMessageListener {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @JmsListener(destination = "${spring.activemq.destination}")
    public void receiveMessage(String orderJson) throws JsonProcessingException {
        Order order = objectMapper.readValue(orderJson, Order.class);

        orderRepository.save(order);
    }
}
