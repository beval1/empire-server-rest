package com.beval.server.service;

import com.beval.server.dto.payload.CreateCastleBuildingDTO;
import com.beval.server.dto.payload.DestroyBuildingDTO;
import com.beval.server.dto.response.BuildingEntityDTO;
import com.beval.server.security.UserPrincipal;

import java.util.List;

public interface BuildingService {
    List<BuildingEntityDTO> getAll(int level);
    List<BuildingEntityDTO> getSpecificBuilding(Long buildingTypeId);
    void createBuilding(UserPrincipal userPrincipal, CreateCastleBuildingDTO createCastleBuildingDTO);
    void upgradeBuilding(UserPrincipal principal, Long buildingId);
    void destroyBuilding(UserPrincipal userPrincipal, DestroyBuildingDTO destroyBuildingDTO);
}
