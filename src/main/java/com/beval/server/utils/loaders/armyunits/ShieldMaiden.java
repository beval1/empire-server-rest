package com.beval.server.utils.loaders.armyunits;

import com.beval.server.model.entity.ArmyUnitEntity;
import com.beval.server.model.enums.ArmyUnitTypeEnum;

public class ShieldMaiden implements LoaderArmyUnit {
    @Override
    public ArmyUnitEntity getUnit() {
        return ArmyUnitEntity
                .builder()
                .name("Shield-Maiden")
                .unitImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669979799/empire/army/shield_maden_ukdzfh.png")
                .unitType(ArmyUnitTypeEnum.MELEE)
                .coinPrice(32)
                .foodConsumption(2)
                .lootingCapacity(43)
                .movingSpeed(34)
                .rangedAttackPower(0)
                .meleeAttackPower(225)
                .rangedDefensePower(10)
                .meleeDefensePower(28)
                .barracksLevel(1)
                .build();
    }
}
