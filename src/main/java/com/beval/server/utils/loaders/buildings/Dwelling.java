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
                level1(),
                level2(),
                level3(),
                level4(),
                level5()
        );
    }

    private BuildingEntity level1() {
        return BuildingEntity
                .builder()
                .level(1)
                .unlocksOnLevel(1)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669727607/empire/buildings/dwelling/dwelling_leve1-removebg_h9xpmr.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(10)
                .build();
    }

    private BuildingEntity level2() {
        return BuildingEntity
                .builder()
                .level(2)
                .unlocksOnLevel(2)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670080957/empire/buildings/dwelling/dwelling_level3-removebg-preview_poafxx.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(10)
                .build();
    }

    private BuildingEntity level3() {
        return BuildingEntity
                .builder()
                .level(3)
                .unlocksOnLevel(3)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670080965/empire/buildings/dwelling/dwelling_level4-removebg-preview_qqa6pn.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(10)
                .build();
    }

    private BuildingEntity level4() {
        return BuildingEntity
                .builder()
                .level(4)
                .unlocksOnLevel(4)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670080972/empire/buildings/dwelling/dwelling_level7-removebg-preview_al7qfj.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(10)
                .build();
    }

    private BuildingEntity level5() {
        return BuildingEntity
                .builder()
                .level(5)
                .unlocksOnLevel(5)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670080978/empire/buildings/dwelling/dwelling_level9-removebg-preview_zwhf6r.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(10)
                .build();
    }
}
