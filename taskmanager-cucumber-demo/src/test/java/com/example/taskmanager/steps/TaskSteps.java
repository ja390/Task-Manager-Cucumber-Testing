package com.example.taskmanager.steps;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repo.TaskRepository;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TaskSteps {

    @Autowired
    private TaskRepository taskRepository;

    private Task testTask;

    @Given("I have a task with title {string} and description {string}")
    public void iHaveATaskWithTitleAndDescription(String title, String description) {
        testTask = new Task(title, description);
        testTask = taskRepository.save(testTask);
    }

    @When("I create a new task with title {string} and description {string}")
    public void iCreateANewTaskWithTitleAndDescription(String title, String description) {
        Task task = new Task(title, description);
        testTask = taskRepository.save(task);
    }

    @Then("I should see a task with title {string}")
    public void iShouldSeeATaskWithTitle(String title) {
        assertThat(taskRepository.findAll())
            .anyMatch(task -> task.getTitle().equals(title));
    }

    @When("I mark the task as completed")
    public void iMarkTheTaskAsCompleted() {
        testTask.setCompleted(true);
        taskRepository.save(testTask);
    }

    @Then("the task should be marked as completed")
    public void theTaskShouldBeMarkedAsCompleted() {
        Task updatedTask = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(updatedTask.isCompleted()).isTrue();
    }

    @When("I delete the task")
    public void iDeleteTheTask() {
        taskRepository.deleteById(testTask.getId());
    }

    @Then("the task should not exist in the system")
    public void theTaskShouldNotExistInTheSystem() {
        assertThat(taskRepository.findById(testTask.getId()).isEmpty()).isTrue();
    }

    @When("I view all tasks")
    public void iViewAllTasks() {
        // The tasks are already in the repository, nothing to do here
    }

    @Then("I should see a list of tasks containing:")
    public void iShouldSeeAListOfTasksContaining(io.cucumber.datatable.DataTable dataTable) {
        var tasks = taskRepository.findAll();
        var expectedTasks = dataTable.entries();
        
        assertThat(tasks).hasSize(expectedTasks.size());
        
        expectedTasks.forEach(expected -> {
            assertThat(tasks).anyMatch(task -> 
                task.getTitle().equals(expected.get("title")) &&
                task.getDescription().equals(expected.get("description"))
            );
        });
    }

    @After
    public void cleanup() {
        taskRepository.deleteAll();
    }
}