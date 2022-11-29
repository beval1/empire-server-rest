package com.beval.server.repository;

import com.beval.server.model.entity.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingEntityRepository extends JpaRepository<BuildingEntity, Long> {
}
