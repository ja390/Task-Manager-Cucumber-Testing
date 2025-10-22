Feature: Task Management
  As a user
  I want to manage my tasks
  So that I can keep track of my work

  Scenario: Creating a new task
    Given the application is running
    When I create a new task with title "Test Task" and description "This is a test task"
    Then I should see a task with title "Test Task"

  Scenario: Marking a task as completed
    Given the application is running
    And I have a task with title "Test Task" and description "This is a test task"
    When I mark the task as completed
    Then the task should be marked as completed