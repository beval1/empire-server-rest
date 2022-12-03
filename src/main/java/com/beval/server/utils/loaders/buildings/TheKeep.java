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
                level1(),
                level2(),
                level3(),
                level4()
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

    private BuildingEntity level2() {
        return BuildingEntity
                .builder()
                .level(2)
                .unlocksOnLevel(3)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670074616/empire/buildings/keep/keep_level2-removebg-preview_cesk9c.png")
                .woodRequired(1000)
                .stoneRequired(1250)
                .build();
    }


    private BuildingEntity level3() {
        return BuildingEntity
                .builder()
                .level(3)
                .unlocksOnLevel(5)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670080241/empire/buildings/keep/keep_level3-removebg-preview_lelhie.png")
                .woodRequired(1000)
                .stoneRequired(1250)
                .build();
    }

    private BuildingEntity level4() {
        return BuildingEntity
                .builder()
                .level(4)
                .unlocksOnLevel(7)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670080249/empire/buildings/keep/keep_level4-removebg-preview_ftpwn8.png")
                .woodRequired(1000)
                .stoneRequired(1250)
                .build();
    }
}
