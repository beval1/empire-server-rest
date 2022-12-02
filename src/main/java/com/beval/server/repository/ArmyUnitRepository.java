package com.beval.server.repository;

import com.beval.server.model.entity.ArmyUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArmyUnitRepository extends JpaRepository<ArmyUnitEntity, Long> {
}
