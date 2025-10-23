package com.example.taskmanager.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repo.TaskRepository;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository repo;

    public TaskController(TaskRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String listTasks(Model model) {
        List<Task> tasks = repo.findAll();
        model.addAttribute("tasks", tasks);
        model.addAttribute("message", "Successfully signed in");
        return "signin-success";
    }

    @GetMapping("/new")
    public String showTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "form";
    }

    @PostMapping
    public String createTask(Task task) {
        repo.save(task);
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/toggle")
    public String toggleTask(@PathVariable Long id) {
        Task task = repo.findById(id).orElseThrow();
        task.setCompleted(!task.isCompleted());
        repo.save(task);
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/tasks";
    }

    // API endpoints for tests
    @PostMapping("/api")
    @ResponseBody
    public Task createApi(@RequestBody Task t) {
        return repo.save(t);
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Task getApi(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public Task updateApi(@PathVariable Long id, @RequestBody Task t) {
        Task existing = repo.findById(id).orElseThrow();
        existing.setTitle(t.getTitle());
        existing.setDescription(t.getDescription());
        existing.setCompleted(t.isCompleted());
        return repo.save(existing);
    }
}
