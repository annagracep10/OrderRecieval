package com.techphantomexample.OrderReciever.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techphantomexample.OrderReciever.dto.CartDTO;
import com.techphantomexample.OrderReciever.dto.CartItemDTO;
import com.techphantomexample.OrderReciever.entity.Order;
import com.techphantomexample.OrderReciever.entity.OrderItem;
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
    public void receiveMessage(String cartJson) throws JsonProcessingException {
            CartDTO cartDTO = objectMapper.readValue(cartJson, CartDTO.class);
            Order order = new Order();
            for (CartItemDTO cartItemDTO : cartDTO.getItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(cartItemDTO.getProductId());
                orderItem.setProductName(cartItemDTO.getProductName());
                orderItem.setPrice(cartItemDTO.getPrice());
                orderItem.setQuantity(cartItemDTO.getQuantity());
                orderItem.setProductType(cartItemDTO.getProductType());
                orderItem.setOrder(order);
                order.getItems().add(orderItem);
            }
            orderRepository.save(order);

    }
}
