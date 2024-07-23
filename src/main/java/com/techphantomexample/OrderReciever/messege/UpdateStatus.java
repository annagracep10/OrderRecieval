package com.techphantomexample.OrderReciever.messege;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techphantomexample.OrderReciever.entity.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessageOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateStatus {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JmsMessageOperations jmsTemplate;

    @Value("${spring.activemq.destination.status-update}")
    private String statusUpdateDestination;

    public void sendStatusUpdate(int orderId, OrderStatus status) throws JsonProcessingException {

        Map<String, Object> messageContent = new HashMap<>();
        messageContent.put("orderId", orderId);
        messageContent.put("status", status);
        String statusUpdateMessageJson = objectMapper.writeValueAsString(messageContent);
        jmsTemplate.convertAndSend(statusUpdateDestination, statusUpdateMessageJson);

    }




}
