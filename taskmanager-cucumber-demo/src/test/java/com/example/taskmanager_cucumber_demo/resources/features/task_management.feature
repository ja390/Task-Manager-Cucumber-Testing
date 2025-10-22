Feature: Task management via API

  Scenario: Create a new task and retrieve it
    Given a clean database
    When I create a task with title "Buy milk" and description "2 liters"
    Then the creation is successful
    And when I fetch the task by id
    Then the title should be "Buy milk"
    And the description should be "2 liters"

  Scenario: Update a task
    Given a task exists with title "Write report" and description "weekly"
    When I update the task title to "Write final report" and mark it done
    Then fetching the task shows title "Write final report" and done true
