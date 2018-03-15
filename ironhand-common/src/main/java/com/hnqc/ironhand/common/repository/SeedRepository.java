package com.hnqc.ironhand.common.repository;

import com.hnqc.ironhand.common.pojo.entity.Seed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeedRepository extends JpaRepository<Seed, Long> {
}
