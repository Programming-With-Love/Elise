package com.hnqc.ironhand.common.repository;

import com.hnqc.ironhand.common.pojo.entity.SchedulerCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SchedulerCountRepository extends JpaRepository<SchedulerCount, Long> {
    @Modifying
    @Query(value = "update SchedulerCount set count = (SchedulerCount.count + 1) where schedulerId = :schedulerId")
    void increaseCount(Long schedulerId);
}
