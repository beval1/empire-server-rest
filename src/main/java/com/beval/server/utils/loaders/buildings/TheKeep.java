package com.beval.server.utils.loaders.buildings;

import com.beval.server.model.entity.BuildingEntity;
import com.beval.server.model.entity.BuildingType;

import java.util.List;

public class TheKeep implements LoaderBuilding {
    private final BuildingType buildingType = BuildingType
            .builder()
            .buildingName("The Keep")
            .buildable(false)
            .widthSizingRatio(4)
            .heightSizingRatio(4)
            .destroyable(false)
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

    private BuildingEntity level1() {
        return BuildingEntity
                .builder()
                .level(1)
                .unlocksOnLevel(1)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669689647/empire/buildings/keep/keep_level1-removebg-preview_tgwddx.png")
                .woodRequired(1000)
                .stoneRequired(1250)
                .build();
    }
}
