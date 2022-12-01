package com.beval.server.utils.loaders.buildings;

import com.beval.server.model.entity.BuildingEntity;
import com.beval.server.model.entity.BuildingType;

import java.util.List;

public class Barracks implements LoaderBuilding{
    BuildingType buildingType = BuildingType
            .builder()
            .buildingName("Barracks")
            .buildable(true)
            .heightSizingRatio(2)
            .widthSizingRatio(2)
            .castleLimit(1)
            .build();

    @Override
    public BuildingType getBuildingType() {
        return buildingType;
    }

    @Override
    public List<BuildingEntity> getBuildingLevels() {
        return List.of(
                level1(),
                level2()
        );
    }

    private BuildingEntity level1(){
        return BuildingEntity
                .builder()
                .level(1)
                .unlocksOnLevel(1)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669670047/empire/buildings/barracks/barracks_level_1-removebg_wq1oxr.png")
                .woodRequired(1000)
                .stoneRequired(1250)
                .buildingXP(10)
                .build();
    }

    private BuildingEntity level2(){
        return BuildingEntity
                .builder()
                .level(2)
                .unlocksOnLevel(3)
                .buildingType(buildingType)
                .buildingTimeSeconds(5200)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669853019/empire/buildings/barracks/barracks_level2-removebg-preview_p72goh.png")
                .woodRequired(10)
                .stoneRequired(10)
                .buildingXP(15)
                .build();
    }
}
