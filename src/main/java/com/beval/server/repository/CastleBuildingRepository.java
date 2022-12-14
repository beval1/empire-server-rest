package com.beval.server.repository;

import com.beval.server.model.entity.CastleBuilding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CastleBuildingRepository extends JpaRepository<CastleBuilding, Long> {
}
