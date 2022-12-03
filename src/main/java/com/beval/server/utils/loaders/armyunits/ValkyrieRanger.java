package com.beval.server.utils.loaders.armyunits;

import com.beval.server.model.entity.ArmyUnitEntity;
import com.beval.server.model.enums.ArmyUnitTypeEnum;

public class ValkyrieRanger implements LoaderArmyUnit {
    @Override
    public ArmyUnitEntity getUnit() {
        return ArmyUnitEntity
                .builder()
                .name("Valkyrie Ranger")
                .unitImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669979744/empire/army/valkyrie_ranger_amyxet.png")
                .unitType(ArmyUnitTypeEnum.RANGED)
                .coinPrice(30)
                .foodConsumption(2)
                .lootingCapacity(40)
                .movingSpeed(34)
                .rangedAttackPower(210)
                .meleeAttackPower(0)
                .rangedDefensePower(32)
                .meleeDefensePower(19)
                .barracksLevel(2)
                .build();
    }
}
