package com.beval.server.dto.response;

import com.beval.server.model.enums.ArmyUnitTypeEnum;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArmyUnitDTO {
    private int id;
    private String name;
    private String unitImage;
    private ArmyUnitTypeEnum unitType;
    private int coinPrice;
    private int foodConsumption;
    private int movingSpeed;
    private int meleeAttackPower;
    private int rangedAttackPower;
    private int meleeDefensePower;
    private int rangedDefensePower;
    private int lootingCapacity;
    private int barracksLevel;
    private int mightyPointsPerUnit;
}
