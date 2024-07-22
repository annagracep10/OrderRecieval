package com.techphantomexample.OrderReciever.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {

    @Id
    private int id;

    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private String productType;

}
