package com.example.jwt_auth_server;

import org.json.JSONObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.jwt_auth_server.controller.AuthController.LoginRequest;
import com.example.jwt_auth_server.controller.AuthController.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class AuthenticationFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static final String TEST_USERNAME = "integrationtestuser";
    private static final String TEST_EMAIL = "integration@test.com";
    private static final String TEST_PASSWORD = "testPassword123";
    private static String jwtToken;
    
    @Test
    @Order(1)
    public void testRegisterUser() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(TEST_USERNAME);
        registerRequest.setEmail(TEST_EMAIL);
        registerRequest.setPassword(TEST_PASSWORD);
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        
        String content = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(content);
        
        assertEquals("User registered successfully!", jsonResponse.getString("message"));
    }
    
    @Test
    @Order(2)
    public void testLoginUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(TEST_USERNAME);
        loginRequest.setPassword(TEST_PASSWORD);
        
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        
        String content = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(content);
        
        jwtToken = jsonResponse.getString("token");
        assertNotNull(jwtToken);
        assertTrue(jwtToken.length() > 20);
    }
    
    @Test
    @Order(3)
    public void testAccessProtectedEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/me")
                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        
        String content = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(content);
        
        assertEquals(TEST_USERNAME, jsonResponse.getString("username"));
    }
    
    @Test
    @Order(4)
    public void testAccessProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/me"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
