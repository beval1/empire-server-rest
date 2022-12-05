package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "castle_defense")
public class CastleDefense extends BaseEntity {
    private int leftFlankSoldiersPercent;
    private int rightFlankSoldiersPercent;
    private int frontSoldiersPercent;
    private int leftFlankSoldierMeleePowerPercent;
    private int rightFlankSoldierMeleePowerPercent;
    private int frontSoldierMeleePowerPercent;
}
