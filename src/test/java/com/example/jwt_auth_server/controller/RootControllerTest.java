package com.example.jwt_auth_server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RootControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @Value("${spring.application.name}")
  private String applicationName;
  
  @Test 
  public void rootEndpointShouldReturnApiInfo() throws Exception {
    mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.application").value(applicationName))
            .andExpect(jsonPath("$.description").value("JWT Authentication Server"))
            .andExpect(jsonPath("$.version").value("1.0.0"))
            .andExpect(jsonPath("$.endpoints['Check service health']").value("/api/health"))
            .andExpect(jsonPath("$.endpoints['Register a new user']").value("/api/auth/register"))
            .andExpect(jsonPath("$.endpoints['Authenticate and issue JWT']").value("/api/auth/login"))
            .andExpect(jsonPath("$.endpoints['Get authenticated user info']").value("/api/users/me"));
  }
}
