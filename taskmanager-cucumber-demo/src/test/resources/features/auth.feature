Feature: Authentication
  In order to use the task manager
  As a user
  I want to sign up and sign in

  Scenario: Successful sign up
    Given the application is running
    When I sign up with fullname "Alice Smith", email "alice@example.com", and password "password123"
    Then I should be redirected to the sign in page

  Scenario: Successful sign in
    Given the application is running
    And a user exists with email "bob@example.com" and password "secret123"
    When I sign in with email "bob@example.com" and password "secret123"
    Then I should see a successful sign in message

  Scenario: Sign up with invalid email
    Given the application is running
    When I sign up with fullname "Invalid User", email "invalid-email", and password "password123"
    Then I should see a validation error message

  Scenario: Sign up with short password
    Given the application is running
    When I sign up with fullname "Short Pass", email "short@example.com", and password "12345"
    Then I should see a validation error message

  Scenario: Sign in with incorrect password
    Given the application is running
    And a user exists with email "test@example.com" and password "correct123"
    When I sign in with email "test@example.com" and password "wrong123"
    Then I should see an error message
