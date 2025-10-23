package com.example.taskmanager.steps;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repo.UserRepository;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AuthSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private ResponseEntity<String> lastResponse;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @When("I sign up with fullname {string}, email {string}, and password {string}")
    public void signUp(String fullName, String email, String password) {
        String url = getBaseUrl() + "/signup";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("fullName", fullName);
        map.add("email", email);
        map.add("password", password);
        map.add("confirmPassword", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        lastResponse = restTemplate.postForEntity(url, request, String.class);
    }

    @When("I sign in with email {string} and password {string}")
    public void signIn(String email, String password) {
        String url = getBaseUrl() + "/signin";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", email);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        lastResponse = restTemplate.postForEntity(url, request, String.class);
    }

    @Given("a user exists with email {string} and password {string}")
    public void createUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setFullName("Test User");
        user.setPassword(password);
        userRepository.save(user);
    }

    @Then("I should be redirected to the sign in page")
    public void verifyRedirectToSignin() {
        // Spring Test follows redirects, so we'll check if we're on the signin page
        assertThat(lastResponse.getStatusCode().is2xxSuccessful())
            .as("Response should be successful")
            .isTrue();
        
        // Check if we're on the signin page with the registration success message
        assertThat(lastResponse.getBody())
            .as("Response should contain registration success message")
            .contains("Registration successful");
    }

    @Then("I should see a successful sign in message")
    public void verifySuccessfulSignIn() {
        assertThat(lastResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(lastResponse.getBody()).contains("success");
    }

    @Then("I should see a validation error message")
    public void verifyValidationError() {
        assertThat(lastResponse.getStatusCode().is2xxSuccessful()).isTrue();
        String body = lastResponse.getBody();
        assertThat(body)
            .as("Response should contain validation error message")
            .matches(content -> 
                content.contains("invalid-feedback") || 
                content.contains("alert-danger") ||
                content.contains("Please provide a valid email") ||
                content.contains("Password must be at least 6 characters"));
    }

    @Then("I should see an error message")
    public void verifyErrorMessage() {
        assertThat(lastResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(lastResponse.getBody())
            .as("Response should contain error message")
            .contains("Invalid email or password");
    }

    @After
    public void cleanup() {
        userRepository.deleteAll();
    }
}