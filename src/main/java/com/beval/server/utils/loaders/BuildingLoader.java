package com.beval.server.utils.loaders;

import com.beval.server.repository.BuildingEntityRepository;
import com.beval.server.repository.BuildingTypeRepository;
import com.beval.server.utils.loaders.buildings.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BuildingLoader implements Loader {
    private final BuildingTypeRepository buildingTypeRepository;
    private final BuildingEntityRepository buildingEntityRepository;
    private final List<LoaderBuilding> buildings = List.of(
            new TheKeep(),
            new Barracks(),
            new Granary(),
            new WoodCutter(),
            new StoneQuarry(),
            new Dwelling()
    );

    public BuildingLoader(BuildingTypeRepository buildingTypeRepository, BuildingEntityRepository buildingEntityRepository){
        this.buildingTypeRepository = buildingTypeRepository;
        this.buildingEntityRepository = buildingEntityRepository;
    }

    @Override
    public void loadAll() {
        buildings.forEach(b -> {
            buildingTypeRepository.save(b.getBuildingType());
            buildingEntityRepository.saveAll(b.getBuildingLevels());
        });
    }
}
