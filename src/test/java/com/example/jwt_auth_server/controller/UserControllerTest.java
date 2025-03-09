package com.example.jwt_auth_server.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.jwt_auth_server.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private UserService userService;
    
    @Test
    public void getCurrentUser_WithValidJwt_ReturnsUserInfo() throws Exception {
        UserDetails userDetails = new User(
            "testuser",
            "password",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        
        when(userService.loadUserByUsername(anyString())).thenReturn(userDetails);
        
        mockMvc.perform(get("/api/users/me")
                .with(jwt().jwt(jwt -> {
                    jwt.subject("testuser");
                })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.authorities[0].authority").value("ROLE_USER"));
    }
    
    @Test
    public void getCurrentUser_WithoutJwt_Returns401() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }
}
