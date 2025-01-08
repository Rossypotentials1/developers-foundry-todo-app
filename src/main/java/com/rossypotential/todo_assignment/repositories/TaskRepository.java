package com.rossypotential.todo_assignment.repositories;

import com.rossypotential.todo_assignment.model.AppUser;
import com.rossypotential.todo_assignment.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page <Task> findByUserId(Long userId, Pageable pageable);
//    List<Task> findAllBy(AppUser user);
}
