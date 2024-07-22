package com.techphantomexample.OrderReciever.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private int id;
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private String productType;
}
