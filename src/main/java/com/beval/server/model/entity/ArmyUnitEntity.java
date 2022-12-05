package com.beval.server.model.entity;

import com.beval.server.model.enums.ArmyUnitTypeEnum;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="armyunit")
public class ArmyUnitEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    @NotEmpty
    private String unitImage;
    @Enumerated
    private ArmyUnitTypeEnum unitType;
    @Positive
    private int foodConsumption;
    @Positive
    private int coinPrice;
    @Positive
    private int movingSpeed;
    private int meleeAttackPower;
    private int rangedAttackPower;
    private int meleeDefensePower;
    private int rangedDefensePower;
    private int lootingCapacity;
    @Builder.Default
    private int barracksLevel = 1;
    @Positive
    private int mightyPointsPerUnit;
}
