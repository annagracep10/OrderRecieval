package com.techphantomexample.OrderReciever.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    private int id;
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private String productType;

}
