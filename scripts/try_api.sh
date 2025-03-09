#!/bin/bash

# API Test Script for JWT Auth Server

API_URL="http://localhost:8080"
TIMESTAMP=$(date +%s)
TEST_USERNAME="testuser${TIMESTAMP}"
TEST_EMAIL="test${TIMESTAMP}@example.com"
TEST_PASSWORD="password123"

echo "🦄 Testing JWT Auth Server API"
echo ""

# 1. Test health endpoint
echo -e "\n--- 1. Testing health endpoint ---"
HEALTH_RESPONSE=$(curl -s $API_URL/api/health)
echo "Response: $HEALTH_RESPONSE"
echo ""

# 2. Register a new user
echo -e "\n--- 2. Registering new user ---"
REGISTER_RESPONSE=$(curl -s -X POST $API_URL/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$TEST_USERNAME\",\"email\":\"$TEST_EMAIL\",\"password\":\"$TEST_PASSWORD\"}")
echo "Response: $REGISTER_RESPONSE"
echo ""

# 3. Login to get token
echo -e "\n--- 3. Logging in to get JWT token ---"
LOGIN_RESPONSE=$(curl -s -X POST $API_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$TEST_USERNAME\",\"password\":\"$TEST_PASSWORD\"}")
echo "Response: $LOGIN_RESPONSE"
echo ""

TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*' | sed 's/"token":"//')

if [ -z "$TOKEN" ]; then
  echo "❌ Failed to get token. Check if login endpoint is working correctly."
  exit 1
fi

echo "Token received: ${TOKEN:0:20}..."
echo ""

# 4. Access protected user info endpoint with token
echo -e "\n--- 4. Accessing protected user info endpoint ---"
USER_RESPONSE=$(curl -s -H "Authorization: Bearer $TOKEN" $API_URL/api/users/me)
echo "Response: $USER_RESPONSE"
echo ""

# 5. Try accessing admin endpoint (should fail for normal user)
echo -e "\n--- 5. Attempting to access admin endpoint (should fail) ---"
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $TOKEN" $API_URL/api/users)
echo "Response status: $HTTP_STATUS (Expected: 403 Forbidden)"
echo ""

echo -e "\nAPI works:) 🦄"
