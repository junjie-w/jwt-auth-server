package com.example.jwt_auth_server.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

  @Value("${spring.application.name}")
  private String applicationName;
  
  @GetMapping("/")
  public Map<String, Object> rootInfo() {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("application", applicationName);
    response.put("description", "JWT Authentication Server");
    response.put("version", "1.0.0");
    
    Map<String, String> endpoints = new LinkedHashMap<>();
    endpoints.put("Check service health", "/api/health");
    endpoints.put("Register a new user", "/api/auth/register");
    endpoints.put("Authenticate and issue JWT", "/api/auth/login");
    endpoints.put("Get authenticated user info", "/api/users/me");
    
    response.put("endpoints", endpoints);
    return response;
  }
}
