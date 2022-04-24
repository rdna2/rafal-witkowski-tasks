package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final DbService service;
    private final TaskMapper taskMapper;

    @GetMapping
    public List<TaskDto> getTasks() {
        List<Task> tasks = (List<Task>) service.getAllTasks();
        return taskMapper.mapToTaskDtoList(tasks);
    }

    @GetMapping("{taskID}")
    public TaskDto getTask(@PathVariable Long taskId) throws TaskNotFoundException {
        return taskMapper.mapToTaskDto(
                service.getTask(taskId).orElseThrow(TaskNotFoundException::new)
        );
    }


//    @GetMapping(value="{taskId}")
//    public TaskDto getTask(@PathVariable Long taskId) {
//        return new TaskDto(1L, "test1 title", "test_content");
//    }
//
//    @DeleteMapping(value="{taskId}")
//    public TaskDto deleteTask(@PathVariable Long taskId) {
//        return null;
//    }
//
//    @PutMapping(value="{taskId}")
//    public TaskDto updateTask(@PathVariable Long taskId) {
//        return new TaskDto(1L, "Edited test title", "Test content");
//    }
//
//    @PostMapping(value="{taskId1}")
//    public TaskDto createTask(@PathVariable Long taskId1) { return new TaskDto(2L,"NEW TASK","CREATE TASK");
//
//    }
}