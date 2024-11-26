package ua.azelev.tracker.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.azelev.tracker.model.Task;
import ua.azelev.tracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;


    public List<Task> getTasksWithFilters(Long userId, Boolean completed, LocalDateTime startDate, LocalDateTime endDate, String sortBy, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findTasksWithFilters(userId, completed, startDate, endDate, sortBy, pageable);
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public Task createTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setDueDate(updatedTask.getDueDate());
                    task.setCompleted(updatedTask.isCompleted());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }


    public long countCompletedTasks(Long userId) {
        return taskRepository.countByUserIdAndCompleted(userId, true);
    }

    public long countIncompleteTasks(Long userId) {
        return taskRepository.countByUserIdAndCompleted(userId, false);
    }

    public long countTasksInDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return taskRepository.countByUserIdAndCreatedAtBetween(userId, startDate, endDate);
    }
}
