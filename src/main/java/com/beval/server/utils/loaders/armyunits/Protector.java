package com.beval.server.utils.loaders.armyunits;

import com.beval.server.model.entity.ArmyUnitEntity;
import com.beval.server.model.enums.ArmyUnitTypeEnum;

public class Protector implements LoaderArmyUnit {
    @Override
    public ArmyUnitEntity getUnit() {
        return ArmyUnitEntity
                .builder()
                .name("Protector")
                .unitImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669979812/empire/army/protector_g3sqnx.png")
                .unitType(ArmyUnitTypeEnum.MELEE)
                .coinPrice(350)
                .foodConsumption(2)
                .lootingCapacity(5)
                .movingSpeed(34)
                .rangedAttackPower(0)
                .meleeAttackPower(24)
                .rangedDefensePower(88)
                .meleeDefensePower(220)
                .barracksLevel(1)
                .build();
    }
}
