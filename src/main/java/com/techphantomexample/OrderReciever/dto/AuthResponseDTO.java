package com.techphantomexample.OrderReciever.dto;

import lombok.Data;
import lombok.Generated;

@Generated
@Data
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}