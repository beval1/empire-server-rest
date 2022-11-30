package com.beval.server.utils.loaders.buildings;

import com.beval.server.model.entity.BuildingEntity;
import com.beval.server.model.entity.BuildingType;

import java.util.List;

public interface LoaderBuilding {
    BuildingType getBuildingType();
    List<BuildingEntity> getBuildingLevels();
}
