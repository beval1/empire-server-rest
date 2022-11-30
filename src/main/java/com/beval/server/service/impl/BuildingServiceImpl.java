package com.beval.server.service.impl;

import com.beval.server.dto.response.BuildingEntityDTO;
import com.beval.server.model.entity.BuildingEntity;
import com.beval.server.repository.BuildingEntityRepository;
import com.beval.server.service.BuildingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService {
    private final BuildingEntityRepository buildingEntityRepository;
    private final ModelMapper modelMapper;

    public BuildingServiceImpl(BuildingEntityRepository buildingEntityRepository, ModelMapper modelMapper) {
        this.buildingEntityRepository = buildingEntityRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BuildingEntityDTO> getAll(int level) {
        List<BuildingEntity> buildingEntities = buildingEntityRepository.findAllByLevel(level);
        return Arrays.asList(modelMapper.map(buildingEntities, BuildingEntityDTO[].class));
    }
}
