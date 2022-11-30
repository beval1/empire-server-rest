package com.beval.server.service;

import com.beval.server.dto.response.BuildingEntityDTO;

import java.util.List;

public interface BuildingService {
    List<BuildingEntityDTO> getAll(int level);
    List<BuildingEntityDTO> getSpecificBuilding(Long buildingTypeId);
}
