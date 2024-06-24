package com.techphantomexample.OrderReciever.dto;

import java.util.List;

public class CartDTO {

    private List<CartItemDTO> items;


    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public CartDTO() {
    }

    public CartDTO(List<CartItemDTO> items) {
        this.items = items;
    }
}
