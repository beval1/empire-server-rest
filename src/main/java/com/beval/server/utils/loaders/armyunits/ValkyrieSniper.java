package com.beval.server.utils.loaders.armyunits;

import com.beval.server.model.entity.ArmyUnitEntity;
import com.beval.server.model.enums.ArmyUnitTypeEnum;

public class ValkyrieSniper implements LoaderArmyUnit {
    @Override
    public ArmyUnitEntity getUnit() {
        return ArmyUnitEntity
                .builder()
                .name("Valkyrie Sniper")
                .unitImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669979774/empire/army/valkyrie_sniper_jpgodb.png")
                .unitType(ArmyUnitTypeEnum.RANGED)
                .coinPrice(320)
                .foodConsumption(2)
                .lootingCapacity(5)
                .movingSpeed(34)
                .rangedAttackPower(34)
                .meleeAttackPower(0)
                .rangedDefensePower(220)
                .meleeDefensePower(84)
                .barracksLevel(2)
                .build();
    }
}
