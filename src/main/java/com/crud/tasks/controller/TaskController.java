package com.crud.tasks.controller;

import com.crud.tasks.domain.TaskDto;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/tasks/")
public class TaskController {

    @GetMapping(value = "")
    public List<TaskDto> getTasks() {
        return new ArrayList<>();
    }

    @GetMapping(value="{taskId}")
    public TaskDto getTask(@PathVariable Long taskId) {
    return new TaskDto(1L, "test1 title", "test_content");
    }

    @DeleteMapping(value="{taskId}")
    public TaskDto deleteTask(@PathVariable Long taskId) {
    return null;
    }

    @PutMapping(value="{taskId}")
    public TaskDto updateTask(@PathVariable Long taskId) {
        return new TaskDto(1L, "Edited test title", "Test content");
    }

    @PostMapping(value="{taskId1}")
    public TaskDto createTask(@PathVariable Long taskId1) { return new TaskDto(2L,"NEW TASK","CREATE TASK");

    }
}
