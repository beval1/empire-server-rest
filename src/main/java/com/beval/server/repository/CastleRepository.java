package com.beval.server.repository;

import com.beval.server.model.entity.CastleEntity;
import com.beval.server.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CastleRepository extends JpaRepository<CastleEntity, Long> {
    List<CastleEntity> findAllByQuadrant(int quadrant);
    CastleEntity findCastleEntitiesByOwner(UserEntity user);
    Optional<CastleEntity> findCastleEntitiesByOwner_Username(String username);
}
