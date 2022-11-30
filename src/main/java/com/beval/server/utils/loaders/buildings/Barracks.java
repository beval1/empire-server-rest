package com.beval.server.utils.loaders.buildings;

import com.beval.server.model.entity.BuildingEntity;
import com.beval.server.model.entity.BuildingType;

import java.util.List;

public class Barracks implements LoaderBuilding{
    BuildingType buildingType = BuildingType
            .builder()
            .buildingName("Barracks")
            .buildable(true)
            .castleLimit(1)
            .build();

    @Override
    public BuildingType getBuildingType() {
        return buildingType;
    }

    @Override
    public List<BuildingEntity> getBuildingLevels() {
        return List.of(
                level1()
        );
    }

    private BuildingEntity level1(){
        return BuildingEntity
                .builder()
                .level(1)
                .unlocksOnLevel(11)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669670047/empire/buildings/barracks/barracks_level_1-removebg_wq1oxr.png")
                .woodRequired(1000)
                .stoneRequired(1250)
                .buildingXP(10)
                .build();
    }
}
