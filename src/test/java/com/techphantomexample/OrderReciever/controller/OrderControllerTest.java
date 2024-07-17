package com.techphantomexample.OrderReciever.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techphantomexample.OrderReciever.entity.Order;
import com.techphantomexample.OrderReciever.entity.OrderStatus;
import com.techphantomexample.OrderReciever.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;


import java.util.Arrays;
import java.util.List;


@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Order order1;
    private Order order2;
    private List<Order> orders;
    private OrderStatus orderStatus;

    @BeforeEach
    public void setUp() {
        order1 = new Order();
        order1.setId(1);
        order1.setStatus(OrderStatus.DELIVERED);

        order2 = new Order();
        order2.setId(2);
        order2.setStatus(OrderStatus.ORDER_RECEIVED);

        orders = Arrays.asList(order1, order2);
        orderStatus = OrderStatus.DELIVERED;
    }

    @Test
    public void testGetAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform((RequestBuilder) get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order1.getId()))
                .andExpect(jsonPath("$[0].status").value(order1.getStatus().toString()))
                .andExpect(jsonPath("$[1].id").value(order2.getId()))
                .andExpect(jsonPath("$[1].status").value(order2.getStatus().toString()));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testUpdateOrderStatus_Success() throws Exception {
        when(orderService.updateOrderStatus(1, orderStatus)).thenReturn(order1);

        mockMvc.perform(put("/orders/update/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderStatus)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order1.getId()))
                .andExpect(jsonPath("$.status").value(orderStatus.toString()));

        verify(orderService, times(1)).updateOrderStatus(1, orderStatus);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testUpdateOrderStatus_OrderNotFound() throws Exception {
        when(orderService.updateOrderStatus(1, orderStatus)).thenThrow(new RuntimeException("Order not found"));

        mockMvc.perform(put("/orders/update/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderStatus)))
                .andExpect(content().string("Order not found"));

        verify(orderService, times(1)).updateOrderStatus(1, orderStatus);
    }
}