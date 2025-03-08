package com.example.jwt_auth_server.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {
  
  @GetMapping("/health")
  public Map<String, String> healthCheck() {
    Map<String, String> response = new HashMap<>();
    response.put("status", "UP");
    response.put("service", "jwt-auth-server");
    response.put("timestamp", new Date().toString());
    return response;
  }
}
