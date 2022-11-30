package com.beval.server.repository;

import com.beval.server.model.entity.BuildingType;
import com.beval.server.model.entity.CastleBuilding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CastleBuildingRepository extends JpaRepository<CastleBuilding, Long> {
    List<CastleBuilding> findAllByBuildingEntity_BuildingType(BuildingType buildingType);
}
