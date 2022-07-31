package com.crud.tasks;

import com.crud.tasks.controller.TaskController;
import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService dbService;

    @MockBean
    private TaskMapper taskMapper;

    @Test
    void shouldFetchEmptyTaskList() throws Exception {
        //Given
        final List<Task> tasks = new ArrayList<>();
        when(taskMapper.mapToTaskDtoList(tasks)).thenReturn(List.of());
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldFetchTaskList() throws Exception {
        //Given
        List<TaskDto> taskDtoList = new ArrayList<>();
        taskDtoList.add(new TaskDto(1L, "test_title", "test_content"));
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(10L, "test_title", "test_content"));

        when(taskMapper.mapToTaskDtoList(tasks)).thenReturn(taskDtoList);
        when(dbService.getAllTasks()).thenReturn(tasks);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("test_title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content", Matchers.is("test_content")));
    }

    @Test
    void testShouldFetchTask() throws Exception{
        //Given
        Task task = new Task(1L, "test_title", "test_content");
        TaskDto dto = new TaskDto(1L, "test_title", "test_content");

        when(taskMapper.mapToTaskDto(any(Task.class))).thenReturn(dto);
        when(dbService.getTask(anyLong())).thenReturn(task);

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("taskId", "1"))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("test_title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("test_content")));
    }

    @Test
    void shouldDeleteTask() throws Exception{
        //When & Then
        mockMvc.perform(delete("/v1/tasks/1").
                        param("taskId", "1").
                        contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

    }


    @Test
    void testShouldUpdateTask() throws Exception{
        //Given
        Task task = new Task(12L, "test_title", "test_content");
        TaskDto taskDto = new TaskDto(12L, "updated_task", "updated_content");

        when(taskMapper.mapToTaskDto(any(Task.class))).thenReturn(taskDto);
        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(task);
        when(dbService.saveTask(any(Task.class))).thenReturn(task);

        Gson gson = new Gson();
        String json = gson.toJson(taskDto);
        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(12)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("updated_task")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("updated_content")));
    }

    @Test
    public void shouldCreateTask() throws Exception {
        //Given
        TaskDto taskDto = new TaskDto(8L, "task_ABC", "task_ABC_content");
        Task task = new Task(8L, "task_ABC", "task_ABC_content");

        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(task);
        when(dbService.saveTask(task)).thenReturn(task);

        Gson gson = new Gson();
        String json = gson.toJson(taskDto);

        //When&Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is(200));
        verify(dbService, Mockito.times(1)).saveTask(task);
    }
}
