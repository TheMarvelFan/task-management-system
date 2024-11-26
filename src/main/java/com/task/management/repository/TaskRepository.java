package com.task.management.repository;

import com.task.management.model.Priority;
import com.task.management.model.Task;
import com.task.management.model.TaskStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isDeleted = 0")
    Page<Task> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isDeleted = 0 " +
           "AND t.priority = :priority AND t.dueDate <= :dueDate")
    Page<Task> findByUserIdAndPriorityAndDueDateBefore(
            @Param("userId") Long userId,
            @Param("priority") Priority priority,
            @Param("dueDate") LocalDate dueDate,
            Pageable pageable
    );

    @Query("SELECT t FROM Task t WHERE t.id = :taskId AND t.user.id = :userId AND t.isDeleted = 0")
    Optional<Task> findByIdAndUserId(@Param("taskId") Long taskId, @Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isDeleted = 0 " +
           "AND t.dueDate BETWEEN :startDate AND :endDate")
    Page<Task> findByUserIdAndDueDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT t FROM Task t " +
       "WHERE t.user.id = :userId " +
       "AND (:priority IS NULL OR t.priority = :priority) " +
       "AND (:dueDate IS NULL OR t.dueDate = :dueDate) " +
       "AND (:subtasksCount IS NULL OR (SELECT COUNT(st.id) FROM SubTask st WHERE st.task.id = t.id) = :subtasksCount) " +
       "AND (:title IS NULL OR t.title LIKE %:title%) " +
       "AND (:status IS NULL OR t.status = :status) " +
       "AND (:createdAt IS NULL OR t.createdAt = :createdAt) " +
       "AND (:updatedAt IS NULL OR t.updatedAt = :updatedAt) " +
       "AND t.isDeleted = 0")
    Page<Task> findTasksByCriteria(
            @Param("userId") Long userId,
            @Param("priority") Priority priority,
            @Param("dueDate") LocalDate dueDate,
            @Param("subtasksCount") Integer subtasksCount,
            @Param("title") String title,
            @Param("status") TaskStatus status,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("updatedAt") LocalDateTime updatedAt,
            Pageable pageable
    );
}
