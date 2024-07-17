package com.techphantomexample.OrderReciever.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techphantomexample.OrderReciever.dto.LoginDto;
import com.techphantomexample.OrderReciever.dto.RegisterDto;
import com.techphantomexample.OrderReciever.entity.Role;
import com.techphantomexample.OrderReciever.entity.UserEntity;
import com.techphantomexample.OrderReciever.repository.RoleRepository;
import com.techphantomexample.OrderReciever.repository.UserRepository;
import com.techphantomexample.OrderReciever.security.JWTGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JWTGenerator jwtGenerator;

    private ObjectMapper objectMapper = new ObjectMapper();

    private LoginDto loginDto;
    private RegisterDto registerDto;
    private UserEntity user;
    private Role role;

    @BeforeEach
    public void setUp() {
        loginDto = new LoginDto("testuser", "password");
        registerDto = new RegisterDto("newuser", "password", "USER");
        user = new UserEntity();
        user.setUsername("newuser");
        user.setPassword("password");
        role = new Role();
        role.setName("USER");
    }

    @Test
    public void testLogin_Success() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtGenerator.generateToken(authentication)).thenReturn("test-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("test-jwt-token"));
    }

    @Test
    public void testLogin_Failure() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testRegister_Success() throws Exception {
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(roleRepository.findByName(registerDto.getRole().toUpperCase())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered success!"));
    }

    @Test
    public void testRegister_UsernameTaken() throws Exception {
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is taken!"));
    }

    @Test
    public void testRegister_InvalidRole() throws Exception {
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(roleRepository.findByName(registerDto.getRole().toUpperCase())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid role!"));
    }
}