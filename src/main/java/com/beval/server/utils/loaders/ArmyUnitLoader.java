package com.beval.server.utils.loaders;

import com.beval.server.repository.ArmyUnitRepository;
import com.beval.server.utils.loaders.armyunits.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArmyUnitLoader implements Loader {
    private final ArmyUnitRepository armyUnitRepository;
    private final List<LoaderArmyUnit> armyUnits = List.of(
            new ValkyrieRanger(),
            new ValkyrieSniper(),
            new ShieldMaiden(),
            new Protector()
    );

    public ArmyUnitLoader(ArmyUnitRepository armyUnitRepository) {
        this.armyUnitRepository = armyUnitRepository;
    }

    @Override
    public void loadAll() {
        armyUnits.forEach(unit -> armyUnitRepository.save(unit.getUnit()));
    }
}
