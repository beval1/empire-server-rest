package com.beval.server.utils.loaders.buildings;

import com.beval.server.model.entity.BuildingEntity;
import com.beval.server.model.entity.BuildingType;

import java.util.List;

import static com.beval.server.config.AppConstants.PRODUCTION_BUILDINGS_CASTLE_LIMIT;

public class StoneQuarry implements LoaderBuilding {
    private final BuildingType buildingType = BuildingType
            .builder()
            .buildingName("Stone Quarry")
            .buildable(true)
            .castleLimit(PRODUCTION_BUILDINGS_CASTLE_LIMIT)
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
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669727893/empire/buildings/stonequarry/stonequarry_small_img-removebg-preview_mklt4x.png")
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
                .unlocksOnLevel(2)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670082012/empire/buildings/stonequarry/stonequarry_level3-removebg-preview_a8ptmh.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(110)
                .build();
    }

    private BuildingEntity level3() {
        return BuildingEntity
                .builder()
                .level(3)
                .unlocksOnLevel(3)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670082018/empire/buildings/stonequarry/stonequarry_level4-removebg-preview_hhcy3z.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(120)
                .build();
    }

    private BuildingEntity level4() {
        return BuildingEntity
                .builder()
                .level(4)
                .unlocksOnLevel(4)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670082024/empire/buildings/stonequarry/stonequarry_level5-removebg-preview_rkzwxz.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(130)
                .build();
    }

    private BuildingEntity level5() {
        return BuildingEntity
                .builder()
                .level(5)
                .unlocksOnLevel(5)
                .buildingType(buildingType)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1670082030/empire/buildings/stonequarry/stonequarry_level7-removebg-preview_tad3dv.png")
                .woodRequired(0)
                .stoneRequired(1)
                .buildingXP(2)
                .production(150)
                .build();
    }
}
