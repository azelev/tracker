package ua.azelev.tracker.controller;

import ua.azelev.tracker.model.Task;
import ua.azelev.tracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Отримати задачі користувача з фільтрацією", description = "Отримує список задач користувача за ID з можливістю фільтрації за статусом, датами та сортуванням.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задачі успішно отримані"),
            @ApiResponse(responseCode = "404", description = "Користувач не знайдений")
    })
    @GetMapping("/user/{userId}/filtered")
    public List<Task> getTasksByUserWithFilters(
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return taskService.getTasksWithFilters(userId, completed, startDate, endDate, sortBy, page, size);
    }

    @Operation(summary = "Отримати всі задачі користувача", description = "Отримує всі задачі користувача за його ID.")
    @ApiResponse(responseCode = "200", description = "Задачі успішно отримані")
    @GetMapping("/user/{userId}")
    public List<Task> getTasksByUser(@PathVariable Long userId) {
        return taskService.getTasksByUserId(userId);
    }

    @Operation(summary = "Отримати задачу по ID", description = "Отримує задачу за її ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успішно знайдена"),
            @ApiResponse(responseCode = "404", description = "Задача не знайдена")
    })
    @GetMapping("/{taskId}")
    public Task getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Operation(summary = "Створити нову задачу", description = "Створює нову задачу для користувача.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача успішно створена"),
            @ApiResponse(responseCode = "400", description = "Невірні дані")
    })
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @Operation(summary = "Оновити задачу", description = "Оновлює задачу за її ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успішно оновлена"),
            @ApiResponse(responseCode = "400", description = "Невірні дані"),
            @ApiResponse(responseCode = "404", description = "Задача не знайдена")
    })
    @PutMapping("/{taskId}")
    public Task updateTask(@PathVariable Long taskId, @RequestBody Task task) {
        return taskService.updateTask(taskId, task);
    }

    @Operation(summary = "Видалити задачу", description = "Видаляє задачу за її ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успішно видалена"),
            @ApiResponse(responseCode = "404", description = "Задача не знайдена")
    })
    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }

    @Operation(summary = "Отримати кількість завершених задач користувача", description = "Отримує кількість завершених задач для конкретного користувача.")
    @ApiResponse(responseCode = "200", description = "Кількість задач успішно отримана")
    @GetMapping("/completed/{userId}")
    public long getCompletedTasks(@PathVariable Long userId) {
        return taskService.countCompletedTasks(userId);
    }

    @Operation(summary = "Отримати кількість незавершених задач користувача", description = "Отримує кількість незавершених задач для конкретного користувача.")
    @ApiResponse(responseCode = "200", description = "Кількість задач успішно отримана")
    @GetMapping("/incomplete/{userId}")
    public long getIncompleteTasks(@PathVariable Long userId) {
        return taskService.countIncompleteTasks(userId);
    }

    @Operation(summary = "Отримати кількість задач, створених в діапазоні дат", description = "Отримує кількість задач, створених в межах заданого діапазону дат для користувача.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Кількість задач успішно отримана"),
            @ApiResponse(responseCode = "400", description = "Невірні дати")
    })
    @GetMapping("/createdInRange/{userId}")
    public long getTasksCreatedInRange(
            @PathVariable Long userId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        return taskService.countTasksInDateRange(userId, startDate, endDate);
    }
}
