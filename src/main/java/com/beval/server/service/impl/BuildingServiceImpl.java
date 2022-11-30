package com.beval.server.service.impl;

import com.beval.server.dto.response.BuildingEntityDTO;
import com.beval.server.exception.BuildingNotFoundException;
import com.beval.server.model.entity.BuildingEntity;
import com.beval.server.model.entity.BuildingType;
import com.beval.server.repository.BuildingEntityRepository;
import com.beval.server.repository.BuildingTypeRepository;
import com.beval.server.service.BuildingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {
    private final BuildingEntityRepository buildingEntityRepository;
    private final BuildingTypeRepository buildingTypeRepository;
    private final ModelMapper modelMapper;

    public BuildingServiceImpl(BuildingEntityRepository buildingEntityRepository, BuildingTypeRepository buildingTypeRepository, ModelMapper modelMapper) {
        this.buildingEntityRepository = buildingEntityRepository;
        this.buildingTypeRepository = buildingTypeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BuildingEntityDTO> getAll(int level) {
        List<BuildingEntity> buildingEntities = buildingEntityRepository.findAllByLevel(level);
        return Arrays.asList(modelMapper.map(buildingEntities, BuildingEntityDTO[].class));
    }

    @Override
    public List<BuildingEntityDTO> getSpecificBuilding(Long buildingTypeId) {
        BuildingType buildingType = buildingTypeRepository.findById(buildingTypeId)
                .orElseThrow(BuildingNotFoundException::new);
        List<BuildingEntity> buildingEntities = buildingEntityRepository.findAllByBuildingType(buildingType);
        return Arrays.asList(modelMapper.map(buildingEntities, BuildingEntityDTO[].class));
    }
}
