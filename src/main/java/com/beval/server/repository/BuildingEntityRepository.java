package com.beval.server.repository;

import com.beval.server.model.entity.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildingEntityRepository extends JpaRepository<BuildingEntity, Long> {
    Optional<BuildingEntity> findByBuildingTypeBuildingNameAndLevel(String buildingName, int level);
    List<BuildingEntity> findAllByLevel(int level);
}
