package com.beval.server.utils.loaders.buildings;

import com.beval.server.model.entity.BuildingEntity;
import com.beval.server.model.entity.BuildingType;

import java.util.List;

import static com.beval.server.config.AppConstants.PRODUCTION_BUILDINGS_CASTLE_LIMIT;

public class Granary implements LoaderBuilding {
    private final BuildingType buildingType = BuildingType
            .builder()
            .buildingName("Granary")
            .castleLimit(PRODUCTION_BUILDINGS_CASTLE_LIMIT)
            .heightSizingRatio(1.0)
            .buildable(true)
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
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670074310/empire/buildings/granary/granary_new_lbqgsr.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(100)
                .build();
    }

    private BuildingEntity level2() {
        return BuildingEntity
                .builder()
                .level(2)
                .unlocksOnLevel(3)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670082679/empire/buildings/granary/granary_level2-removebg-preview_v3h9cg.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(130)
                .build();
    }

    private BuildingEntity level3() {
        return BuildingEntity
                .builder()
                .level(3)
                .unlocksOnLevel(5)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670082688/empire/buildings/granary/granary_level3-removebg-preview_mteeh3.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(160)
                .build();
    }

    private BuildingEntity level4() {
        return BuildingEntity
                .builder()
                .level(4)
                .unlocksOnLevel(7)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670082693/empire/buildings/granary/granary_level4-removebg-preview_tkfvpl.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(200)
                .build();
    }
}
