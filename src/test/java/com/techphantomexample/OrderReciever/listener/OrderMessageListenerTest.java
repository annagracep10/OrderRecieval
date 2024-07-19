package com.techphantomexample.OrderReciever.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techphantomexample.OrderReciever.dto.CartDTO;
import com.techphantomexample.OrderReciever.dto.CartItemDTO;
import com.techphantomexample.OrderReciever.entity.Order;
import com.techphantomexample.OrderReciever.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderMessageListenerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderMessageListener orderMessageListener;

    private CartDTO cartDTO;
    private CartItemDTO cartItemDTO;
    private String cartJson;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(1);
        cartItemDTO.setProductName("Monstera");
        cartItemDTO.setPrice(25.0);
        cartItemDTO.setQuantity(1);
        cartItemDTO.setProductType("plant");

        cartDTO = new CartDTO();
        cartDTO.setItems(Collections.singletonList(cartItemDTO));

        cartJson = "{ \"items\": [{ \"productId\": 1, \"productName\": \"Monstera\", \"price\": 25.0, \"quantity\": 1, \"productType\": \"plant\" }] }";

        lenient().when(objectMapper.readValue(cartJson, CartDTO.class)).thenReturn(cartDTO);
    }

    @Test
    public void testReceiveMessage() throws JsonProcessingException {
        orderMessageListener.receiveMessage(cartJson);

        verify(objectMapper, times(1)).readValue(cartJson, CartDTO.class);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testReceiveMessage_Exception() throws JsonProcessingException {
        String invalidJson = "invalid json";
        when(objectMapper.readValue(invalidJson, CartDTO.class)).thenThrow(JsonProcessingException.class);

        assertThrows(JsonProcessingException.class, () -> {
            orderMessageListener.receiveMessage(invalidJson);
        });

        verify(orderRepository, times(0)).save(any(Order.class));
    }
}