package com.beval.server.model.entity;

import com.beval.server.model.enums.BattleResult;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "battle_reports")
public class BattleReportEntity extends BaseEntity {
    @Enumerated
    private BattleResult result;
    private int attackingArmy;
    private int defendingArmy;
    private int attackingArmyVictims;
    private int defendingArmyVictims;
    @OneToOne
    private UserEntity attacker;
    @OneToOne
    private UserEntity defender;
}
