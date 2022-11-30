package com.beval.server.utils.loaders.buildings;

import com.beval.server.model.entity.BuildingEntity;
import com.beval.server.model.entity.BuildingType;

import java.util.List;

import static com.beval.server.config.AppConstants.PRODUCTION_BUILDINGS_CASTLE_LIMIT;

public class Dwelling implements LoaderBuilding{
    private final BuildingType buildingType = BuildingType
            .builder()
            .buildingName("Dwelling")
            .castleLimit(PRODUCTION_BUILDINGS_CASTLE_LIMIT)
            .buildable(true)
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
                .unlocksOnLevel(0)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669727607/empire/buildings/dwelling/dwelling_leve1-removebg_h9xpmr.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .build();
    }
}
