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
    endpoints.put("GET /api/health", "Check service health");
    endpoints.put("POST /api/auth/register", "Register a new user");
    endpoints.put("POST /api/auth/login", "Authenticate and issue JWT");
    endpoints.put("GET /api/users/me", "Get authenticated user info");
    
    response.put("endpoints", endpoints);
    return response;
  }
}
