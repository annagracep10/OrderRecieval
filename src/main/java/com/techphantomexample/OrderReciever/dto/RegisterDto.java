package com.techphantomexample.OrderReciever.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

@Generated
@Data
@AllArgsConstructor
public class RegisterDto {
    private String username;
    private String password;
    private String role;
}