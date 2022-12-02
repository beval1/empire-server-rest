package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_army")
public class CastleArmy extends BaseEntity {
    @OneToOne
    private ArmyUnitEntity armyUnit;
    private int armyUnitCount;
}
