package com.beval.server.repository;

import com.beval.server.model.entity.BattleReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BattleReportRepository extends JpaRepository<BattleReportEntity, Long> {
}
