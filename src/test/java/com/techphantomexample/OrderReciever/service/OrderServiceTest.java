package com.techphantomexample.OrderReciever.service;

import com.techphantomexample.OrderReciever.entity.*;
import com.techphantomexample.OrderReciever.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order1;
    private Order order2;

    @BeforeEach
    public void setUp() {

        order1 = new Order();
        order1.setId(1);
        order1.setStatus(OrderStatus.ORDER_RECEIVED);

        order2 = new Order();
        order2.setId(2);
        order2.setStatus(OrderStatus.ORDER_RECEIVED);
    }

    @Test
    public void testGetAllOrders() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.getAllOrders();

        assertEquals(2, orders.size());
        assertEquals(order1, orders.get(0));
        assertEquals(order2, orders.get(1));
    }

    @Test
    public void testUpdateOrderStatus() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        when(orderRepository.save(order1)).thenReturn(order1);

        Order updatedOrder = orderService.updateOrderStatus(1, OrderStatus.DELIVERED);

        assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
        verify(orderRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).save(order1);
    }

    @Test
    public void testUpdateOrderStatusOrderNotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.updateOrderStatus(1, OrderStatus.DELIVERED);
        });

        String expectedMessage = "Order not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(orderRepository, times(1)).findById(1);
        verify(orderRepository, times(0)).save(any(Order.class));
    }

}