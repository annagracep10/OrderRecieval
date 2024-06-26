package com.techphantomexample.OrderReciever.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private String productType;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


}
