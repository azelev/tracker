package ua.azelev.tracker.repository;

import ua.azelev.tracker.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);

    @Query(value = "SELECT * FROM tasks t WHERE " +
                   "(:completed IS NULL OR t.completed = :completed) AND " +
                   "(:startDate IS NULL OR t.created_at >= :startDate) AND " +
                   "(:endDate IS NULL OR t.due_date <= :endDate) AND " +
                   "(:userId IS NULL OR t.user_id = :userId) " +
                   "ORDER BY " +
                   "CASE WHEN :sortBy = 'createdAt' THEN t.created_at ELSE t.due_date END ASC",
           nativeQuery = true)
    List<Task> findTasksWithFilters(
            @Param("userId") Long userId,
            @Param("completed") Boolean completed,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("sortBy") String sortBy,
            Pageable pageable
    );

    long countByUserIdAndCompleted(Long userId, boolean completed);

    long countByUserIdAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}