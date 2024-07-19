package com.techphantomexample.OrderReciever.dto;

import lombok.*;

@Generated
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
