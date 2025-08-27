package com.examly.springapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpringappApplicationTests {

    private String usertoken;
    private String admintoken;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper; // To parse JSON responses

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @Order(1)
    void backend_testRegisterAdmin() {
        String requestBody = "{\"userId\": 1,\"email\": \"demoadmin@gmail.com\", \"password\": \"admin@1234\", \"username\": \"admin123\", \"userRole\": \"ADMIN\", \"mobileNumber\": \"9876543210\"}";
        ResponseEntity<String> response = restTemplate.postForEntity("/api/register",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(2)
    void backend_testRegisterUser() {
        String requestBody = "{\"userId\": 2,\"email\": \"demouser@gmail.com\", \"password\": \"user@1234\", \"username\": \"user123\", \"userRole\": \"USER\", \"mobileNumber\": \"1122334455\"}";
        ResponseEntity<String> response = restTemplate.postForEntity("/api/register",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(3)
    void backend_testLoginAdmin() throws Exception {
        String requestBody = "{\"email\": \"demoadmin@gmail.com\", \"password\": \"admin@1234\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/login",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

    // Check if response body is null
    Assertions.assertNotNull(response.getBody(), "Response body is null!");

        JsonNode responseBody = objectMapper.readTree(response.getBody());
        String token = responseBody.get("token").asText();
        admintoken = token;

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(token);
    }

    @Test
    @Order(4)
    void backend_testLoginUser() throws Exception {
        String requestBody = "{\"email\": \"demouser@gmail.com\", \"password\": \"user@1234\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/login",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        JsonNode responseBody = objectMapper.readTree(response.getBody());
        String token = responseBody.get("token").asText();
        usertoken = token;

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(token);
    }

    


    @Test
    @Order(5)
    void backend_testAdminAddFood() throws Exception {
        // Ensure tokens are available
        Assertions.assertNotNull(admintoken, "Admin token should not be null");
        Assertions.assertNotNull(usertoken, "User token should not be null");
    
        String requestBody = "{"
            + "\"foodId\": 1,"
            + "\"foodName\": \"Pizza\","
            + "\"price\": 500,"
            + "\"stockQuantity\": 10,"
            + "\"photo\": \"base64EncodedString\","
            + "\"user\": {\"userId\": 1}"
            + "}";
    
        // Test with Admin token (Expecting 201 Created)
        HttpHeaders adminHeaders = createHeaders();
        adminHeaders.set("Authorization", "Bearer " + admintoken);
        HttpEntity<String> adminRequest = new HttpEntity<>(requestBody, adminHeaders);
    
        ResponseEntity<String> adminResponse = restTemplate.exchange("/api/food", HttpMethod.POST, adminRequest, String.class);
        JsonNode responseBody = objectMapper.readTree(adminResponse.getBody());
    
        System.out.println(adminResponse.getStatusCode() + " Status code for Admin adding food");
        Assertions.assertEquals(HttpStatus.CREATED, adminResponse.getStatusCode());
        Assertions.assertEquals(500, responseBody.get("price").asInt());
        Assertions.assertEquals(10, responseBody.get("stockQuantity").asInt());
        Assertions.assertEquals("Pizza", responseBody.get("foodName").asText());
    
        // Test with User token (Expecting 403 Forbidden)
        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<String> userRequest = new HttpEntity<>(requestBody, userHeaders);
    
        ResponseEntity<String> userResponse = restTemplate.exchange("/api/food", HttpMethod.POST, userRequest, String.class);
    
        System.out.println(userResponse.getStatusCode() + " Status code for User trying to add food");
        Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode());
    }
    

    @Test
    @Order(6)
    void backend_testGetFoodByIdWithRoleValidation() throws Exception {
        // Ensure tokens are available
        Assertions.assertNotNull(admintoken, "Admin token should not be null");
        Assertions.assertNotNull(usertoken, "User token should not be null");
    
        int foodId = 1;
        String url = "/api/food/" + foodId;
    
        // Test with Admin token (Expecting 200 OK)
        HttpHeaders adminHeaders = createHeaders();
        adminHeaders.set("Authorization", "Bearer " + admintoken);
        HttpEntity<Void> adminRequest = new HttpEntity<>(adminHeaders);
    
        ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.GET, adminRequest, String.class);
    
        System.out.println(adminResponse.getStatusCode() + " Status code for Admin retrieving food");
        Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());
    
        // Parse and validate JSON response
        JsonNode responseBody = objectMapper.readTree(adminResponse.getBody());
    
        Assertions.assertEquals(1, responseBody.get("foodId").asInt());
        Assertions.assertEquals("Pizza", responseBody.get("foodName").asText());
        Assertions.assertEquals(500, responseBody.get("price").asInt());
        Assertions.assertEquals(10, responseBody.get("stockQuantity").asInt());
    
        System.out.println("Admin Response JSON: " + responseBody.toString());
    
        // Test with User token (Expecting 200 OK)
        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);
    
        ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.GET, userRequest, String.class);
    
        System.out.println(userResponse.getStatusCode() + " Status code for User retrieving food");
        Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());
    }
    
   
    @Test
    @Order(7)
    void backend_testGetAllFoods() throws Exception {
        // Ensure tokens are available
        Assertions.assertNotNull(admintoken, "Admin token should not be null");
        Assertions.assertNotNull(usertoken, "User token should not be null");
    
        // Test GET with Admin token (Expecting 200 OK)
        HttpHeaders adminHeaders = createHeaders();
        adminHeaders.set("Authorization", "Bearer " + admintoken);
        HttpEntity<String> adminRequest = new HttpEntity<>(adminHeaders);
    
        ResponseEntity<String> adminResponse = restTemplate.exchange("/api/food", HttpMethod.GET, adminRequest, String.class);
        JsonNode responseBody = objectMapper.readTree(adminResponse.getBody());
    
        System.out.println(adminResponse.getStatusCode() + " Status code for Admin getting all foods");
        Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());
        Assertions.assertTrue(responseBody.isArray());
    
        // Test GET with User token (Expecting 200 OK)
        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);
    
        ResponseEntity<String> userResponse = restTemplate.exchange("/api/food", HttpMethod.GET, userRequest, String.class);
        JsonNode userResponseBody = objectMapper.readTree(userResponse.getBody());
    
        System.out.println(userResponse.getStatusCode() + " Status code for User getting all foods");
        Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());
        Assertions.assertTrue(userResponseBody.isArray());
    }
    
    @Test
    @Order(8)
    void backend_testUpdateFoodWithRoleValidation() throws Exception {
        // Ensure tokens are available
        Assertions.assertNotNull(admintoken, "Admin token should not be null");
        Assertions.assertNotNull(usertoken, "User token should not be null");
    
        int foodId = 1;
        String url = "/api/food/" + foodId;
    
        String updateRequestBody = "{"
            + "\"foodId\": " + foodId + ","
            + "\"foodName\": \"Burger\","
            + "\"price\": " + 300 + ","
            + "\"stockQuantity\": " + 15 + ","
            + "\"photo\": \"updatedBase64String\","
            + "\"user\": {\"userId\": 1}"
            + "}";
    
        // Test with Admin token (Expecting 200 OK)
        HttpHeaders adminHeaders = createHeaders();
        adminHeaders.set("Authorization", "Bearer " + admintoken);
        HttpEntity<String> adminRequest = new HttpEntity<>(updateRequestBody, adminHeaders);
    
        ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.PUT, adminRequest, String.class);
    
        System.out.println(adminResponse.getStatusCode() + " Status code for Admin updating food");
        Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());
    
        JsonNode responseBody = objectMapper.readTree(adminResponse.getBody());
    
        Assertions.assertEquals(foodId, responseBody.get("foodId").asLong());
        Assertions.assertEquals("Burger", responseBody.get("foodName").asText());
        Assertions.assertEquals(300, responseBody.get("price").asInt());
        Assertions.assertEquals(15, responseBody.get("stockQuantity").asInt());
    
        System.out.println("Admin Update Response JSON: " + responseBody.toString());
    
        // Test with User token (Expecting 403 Forbidden)
        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<String> userRequest = new HttpEntity<>(updateRequestBody, userHeaders);
    
        ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.PUT, userRequest, String.class);
    
        System.out.println(userResponse.getStatusCode() + " Status code for User trying to update food");
        Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode());
    }
    


    @Test
@Order(9)
void backend_testUserCanAddOrder() throws Exception {
    // Ensure tokens are available
    Assertions.assertNotNull(usertoken, "User token should not be null");
    Assertions.assertNotNull(admintoken, "Admin token should not be null");

    String requestBody = "{"
        + "\"orderStatus\": \"Pending\","
        + "\"totalAmount\": 500.0,"
        + "\"quantity\": 2,"
        + "\"orderDate\": \"2025-02-27\","
        + "\"user\": {\"userId\": 2},"
        + "\"food\": {\"foodId\": 1}"
        + "}";

    // Test with User token (Expecting 201 Created)
    HttpHeaders userHeaders = createHeaders();
    userHeaders.set("Authorization", "Bearer " + usertoken);
    HttpEntity<String> userRequest = new HttpEntity<>(requestBody, userHeaders);

    ResponseEntity<String> userResponse = restTemplate.exchange("/api/orders", HttpMethod.POST, userRequest, String.class);
    JsonNode responseBody = objectMapper.readTree(userResponse.getBody());

    System.out.println(userResponse.getStatusCode() + " Status code for User adding order");
    Assertions.assertEquals(HttpStatus.CREATED, userResponse.getStatusCode());
    Assertions.assertEquals(500.0, responseBody.get("totalAmount").asDouble());
    Assertions.assertEquals(2, responseBody.get("quantity").asInt());
    Assertions.assertEquals("Pending", responseBody.get("orderStatus").asText());

    // Test with Admin token (Expecting 403 Forbidden)
    HttpHeaders adminHeaders = createHeaders();
    adminHeaders.set("Authorization", "Bearer " + admintoken);
    HttpEntity<String> adminRequest = new HttpEntity<>(requestBody, adminHeaders);

    ResponseEntity<String> adminResponse = restTemplate.exchange("/api/orders", HttpMethod.POST, adminRequest, String.class);

    System.out.println(adminResponse.getStatusCode() + " Status code for Admin trying to place an order");
    Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode());
}




@Test
@Order(10)
void backend_testRoleBasedAccessForViewingAllOrders() throws Exception {
    // Ensure tokens are available
    Assertions.assertNotNull(admintoken, "Admin token should not be null");
    Assertions.assertNotNull(usertoken, "User token should not be null");

    String url = "/api/orders";

    // ✅ Test with Admin token (Expecting 200 OK)
    HttpHeaders adminHeaders = createHeaders();
    adminHeaders.set("Authorization", "Bearer " + admintoken);
    HttpEntity<Void> adminRequest = new HttpEntity<>(adminHeaders);

    ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.GET, adminRequest, String.class);
    JsonNode adminResponseBody = objectMapper.readTree(adminResponse.getBody());

    System.out.println(adminResponse.getStatusCode() + " Status code for Admin viewing all orders");
    Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());
    Assertions.assertTrue(adminResponseBody.isArray());

    System.out.println("Admin View Orders Response JSON: " + adminResponseBody.toString());

    // ❌ Test with User token (Expecting 403 Forbidden)
    HttpHeaders userHeaders = createHeaders();
    userHeaders.set("Authorization", "Bearer " + usertoken);
    HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

    ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.GET, userRequest, String.class);

    System.out.println(userResponse.getStatusCode() + " Status code for User trying to view all orders");
    Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode());
}



@Test
@Order(11)
void backend_testUserCanViewOwnOrders() throws Exception {
    // Ensure tokens are available
    Assertions.assertNotNull(usertoken, "User token should not be null");

    int userId = 2; // Assuming user ID 1 is the logged-in user
    String url = "/api/orders/user/" + userId; // Adjust API path if needed

    // ✅ Test with User token (Expecting 200 OK)
    HttpHeaders userHeaders = createHeaders();
    userHeaders.set("Authorization", "Bearer " + usertoken);
    HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

    ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.GET, userRequest, String.class);
    JsonNode userResponseBody = objectMapper.readTree(userResponse.getBody());

    System.out.println(userResponse.getStatusCode() + " Status code for User viewing their own orders");
    Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());
    Assertions.assertTrue(userResponseBody.isArray());

    System.out.println("User View Own Orders Response JSON: " + userResponseBody.toString());

    // ❌ Test with another user's token (Expecting 403 Forbidden)
    HttpHeaders anotherUserHeaders = createHeaders();
    anotherUserHeaders.set("Authorization", "Bearer " + admintoken);
    HttpEntity<Void> anotherUserRequest = new HttpEntity<>(anotherUserHeaders);

    ResponseEntity<String> anotherUserResponse = restTemplate.exchange(url, HttpMethod.GET, anotherUserRequest, String.class);

    System.out.println(anotherUserResponse.getStatusCode() + " Status code for another user trying to view orders");
    Assertions.assertEquals(HttpStatus.FORBIDDEN, anotherUserResponse.getStatusCode());
}


@Test
@Order(12)
void backend_testAdminCanUpdateOrder_UserCannot() throws Exception {
    // Ensure tokens are available
    Assertions.assertNotNull(usertoken, "User token should not be null");
    Assertions.assertNotNull(admintoken, "Admin token should not be null");

    // Sample order update payload
    String updateRequestBody = "{"
        + "\"orderId\": 1," // Assuming this order exists
        + "\"orderStatus\": \"Delivered\","
        + "\"totalAmount\": 600.0,"
        + "\"quantity\": 3,"
        + "\"orderDate\": \"2025-02-28\","
        + "\"user\": {\"userId\": 1},"
        + "\"food\": {\"foodId\": 1}"
        + "}";

    // Admin can update (Expecting 200 OK)
    HttpHeaders adminHeaders = createHeaders();
    adminHeaders.set("Authorization", "Bearer " + admintoken);
    HttpEntity<String> adminRequest = new HttpEntity<>(updateRequestBody, adminHeaders);

    ResponseEntity<String> adminResponse = restTemplate.exchange(
        "/api/orders/1", HttpMethod.PUT, adminRequest, String.class);

    System.out.println(adminResponse.getStatusCode() + " Status code for Admin updating order");
    Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());

    JsonNode adminResponseBody = objectMapper.readTree(adminResponse.getBody());
    Assertions.assertEquals("Delivered", adminResponseBody.get("orderStatus").asText());
    Assertions.assertEquals(600.0, adminResponseBody.get("totalAmount").asDouble());
    Assertions.assertEquals(3, adminResponseBody.get("quantity").asInt());

    // User trying to update (Expecting 403 Forbidden)
    HttpHeaders userHeaders = createHeaders();
    userHeaders.set("Authorization", "Bearer " + usertoken);
    HttpEntity<String> userRequest = new HttpEntity<>(updateRequestBody, userHeaders);

    ResponseEntity<String> userResponse = restTemplate.exchange(
        "/api/orders/1", HttpMethod.PUT, userRequest, String.class);

    System.out.println(userResponse.getStatusCode() + " Status code for User trying to update order");
    Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode());
}

    

@Test
@Order(13)
void backend_testAddFeedbackWithRoleValidation() throws Exception {
    // Ensure tokens are available
    Assertions.assertNotNull(admintoken, "Admin token should not be null");
    Assertions.assertNotNull(usertoken, "User token should not be null");

    String requestBody = "{"
    + "\"feedbackId\": 1,"
    + "\"feedbackText\": \"Great service!\","  // Changed "message" to "feedbackText"
    + "\"rating\": 4,"
    + "\"user\": { \"userId\": 2 },"
    + "\"food\": { \"foodId\": 1 },"
    + "\"date\": \"2025-02-27\""  // Added date field to match your model
    + "}";


    // Test with User token (Expecting 201 Created)
    HttpHeaders userHeaders = createHeaders();
    userHeaders.set("Authorization", "Bearer " + usertoken);
    HttpEntity<String> userRequest = new HttpEntity<>(requestBody, userHeaders);

    ResponseEntity<String> userResponse = restTemplate.exchange("/api/feedback", HttpMethod.POST, userRequest, String.class);
    JsonNode responseBody = objectMapper.readTree(userResponse.getBody());

    System.out.println(userResponse.getStatusCode() + " Status code for User adding feedback");
    Assertions.assertEquals(HttpStatus.CREATED, userResponse.getStatusCode());
    Assertions.assertEquals("Great service!", responseBody.get("feedbackText").asText());
    Assertions.assertEquals(4, responseBody.get("rating").asInt());

    // Test with Admin token (Expecting 403 Forbidden)
    HttpHeaders adminHeaders = createHeaders();
    adminHeaders.set("Authorization", "Bearer " + admintoken);
    HttpEntity<String> adminRequest = new HttpEntity<>(requestBody, adminHeaders);

    ResponseEntity<String> adminResponse = restTemplate.exchange("/api/feedback", HttpMethod.POST, adminRequest, String.class);
    System.out.println(adminResponse.getStatusCode() + " Status code for Admin trying to add feedback");
    Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode());
}

@Test
@Order(14)
void backend_testGetAllFeedbackWithRoleValidation() throws Exception {
    Assertions.assertNotNull(admintoken, "Admin token should not be null");
    Assertions.assertNotNull(usertoken, "User token should not be null");

    // Admin should be able to view all feedback
    HttpHeaders adminHeaders = createHeaders();
    adminHeaders.set("Authorization", "Bearer " + admintoken);
    HttpEntity<String> adminRequest = new HttpEntity<>(adminHeaders);

    ResponseEntity<String> adminResponse = restTemplate.exchange("/api/feedback", HttpMethod.GET, adminRequest, String.class);
    Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());

    // User should NOT be able to view all feedback
    HttpHeaders userHeaders = createHeaders();
    userHeaders.set("Authorization", "Bearer " + usertoken);
    HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);

    ResponseEntity<String> userResponse = restTemplate.exchange("/api/feedback", HttpMethod.GET, userRequest, String.class);
    Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode());
}

@Test
@Order(15)
void backend_testGetFeedbackByUserIdWithRoleValidation() throws Exception {
    Assertions.assertNotNull(admintoken, "Admin token should not be null");
    Assertions.assertNotNull(usertoken, "User token should not be null");

    String url = "/api/feedback/user/2";

    // User should be able to view their own feedback
    HttpHeaders userHeaders = createHeaders();
    userHeaders.set("Authorization", "Bearer " + usertoken);
    HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);

    ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.GET, userRequest, String.class);
    Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());

    // Admin should also be able to view feedback by user ID
    HttpHeaders adminHeaders = createHeaders();
    adminHeaders.set("Authorization", "Bearer " + admintoken);
    HttpEntity<String> adminRequest = new HttpEntity<>(adminHeaders);

    ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.GET, adminRequest, String.class);
    Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode());
}



}
