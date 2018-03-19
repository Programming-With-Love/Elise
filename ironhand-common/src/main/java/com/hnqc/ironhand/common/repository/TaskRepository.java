package com.hnqc.ironhand.common.repository;

import com.hnqc.ironhand.common.pojo.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
