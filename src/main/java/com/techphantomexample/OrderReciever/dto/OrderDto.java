package com.techphantomexample.OrderReciever.dto;

import com.techphantomexample.OrderReciever.entity.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private int id;
    private int userId;
    private List<OrderItem> items= new ArrayList<>();
}
