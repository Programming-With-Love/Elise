package com.hnqc.ironhand.common.repository;

import com.hnqc.ironhand.common.pojo.entity.Scheduler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Scheduler, Long> {
}
