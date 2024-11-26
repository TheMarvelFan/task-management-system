package com.task.management.repository;

import com.task.management.model.SubTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SubTaskRepository extends JpaRepository<SubTask, Long>, JpaSpecificationExecutor<SubTask> {

    @Query("SELECT st FROM SubTask st WHERE st.task.id = :taskId AND st.isDeleted = false")
    Page<SubTask> findAllByTaskId(@Param("taskId") Long taskId, Pageable pageable);

    @Query("SELECT st FROM SubTask st WHERE st.task.user.id = :userId AND st.isDeleted = false")
    Page<SubTask> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT st FROM SubTask st WHERE st.id = :subTaskId AND st.task.user.id = :userId " +
           "AND st.isDeleted = false")
    Optional<SubTask> findByIdAndUserId(@Param("subTaskId") Long subTaskId, @Param("userId") Long userId);

    @Query("SELECT COUNT(st) FROM SubTask st WHERE st.task.id = :taskId AND st.status = 1 " +
           "AND st.isDeleted = false")
    Long countCompletedSubTasksByTaskId(@Param("taskId") Long taskId);

    @Query("SELECT COUNT(st) FROM SubTask st WHERE st.task.id = :taskId AND st.isDeleted = false")
    Long countTotalSubTasksByTaskId(@Param("taskId") Long taskId);

}