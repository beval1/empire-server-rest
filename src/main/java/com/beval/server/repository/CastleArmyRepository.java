package com.beval.server.repository;

import com.beval.server.model.entity.CastleArmy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CastleArmyRepository extends JpaRepository<CastleArmy, Long> {
}
