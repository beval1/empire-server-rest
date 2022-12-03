package com.beval.server.service.impl;

import com.beval.server.dto.payload.CreateCastleBuildingDTO;
import com.beval.server.dto.payload.DestroyBuildingDTO;
import com.beval.server.dto.response.BuildingEntityDTO;
import com.beval.server.exception.*;
import com.beval.server.model.entity.*;
import com.beval.server.repository.BuildingEntityRepository;
import com.beval.server.repository.BuildingTypeRepository;
import com.beval.server.repository.CastleBuildingRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.BuildingService;
import com.beval.server.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static com.beval.server.config.AppConstants.validGridBuildingPosition;

@Service
public class BuildingServiceImpl implements BuildingService {
    private final UserRepository userRepository;
    private final CastleBuildingRepository castleBuildingRepository;
    private final BuildingEntityRepository buildingEntityRepository;
    private final BuildingTypeRepository buildingTypeRepository;
    private final ModelMapper modelMapper;

    public BuildingServiceImpl(UserRepository userRepository, CastleBuildingRepository castleBuildingRepository,
                               BuildingEntityRepository buildingEntityRepository,
                               BuildingTypeRepository buildingTypeRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.castleBuildingRepository = castleBuildingRepository;
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

    @Transactional
    @Override
    public void createBuilding(UserPrincipal userPrincipal, CreateCastleBuildingDTO createCastleBuildingDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        BuildingType buildingType = buildingTypeRepository.findById(createCastleBuildingDTO.getTypeId())
                .orElseThrow(ResourceNotFoundException::new);

        //check if max building limit for castle is reached
        List<CastleBuilding> castleBuildingsOfType = userEntity.getCastle().getBuildings().stream()
                .filter(building -> building.getBuildingEntity().getBuildingType().equals(buildingType)).toList();
        if (castleBuildingsOfType.size() >= buildingType.getCastleLimit()) {
            throw new MaxBuildingLimitReachedException();
        }

        //check if buildable
        if (!buildingType.isBuildable()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Building not buildable!");
        }

        BuildingEntity buildingEntity = buildingEntityRepository
                .findByBuildingTypeBuildingNameAndLevel(buildingType.getBuildingName(), 1)
                .orElseThrow(ResourceNotFoundException::new);

        //check if unlocked
        if (buildingEntity.getUnlocksOnLevel() > UserService.calculateUserLevel(userEntity)) {
            throw new NotUnlockedException();
        }

        if (buildingEntity.getStoneRequired() > userEntity.getCastle().getStone() ||
                buildingEntity.getWoodRequired() > userEntity.getCastle().getWood()) {
            throw new NotEnoughResourcesException();
        }

        //check if position is valid
        CastleBuilding existingCoordinatesBuilding = userEntity.getCastle().getBuildings().stream().filter(building ->
                building.getCoordinateX() == createCastleBuildingDTO.getColumn() &&
                        building.getCoordinateY() == createCastleBuildingDTO.getRow()).findFirst().orElse(null);
        if (validGridBuildingPosition[createCastleBuildingDTO.getRow()][createCastleBuildingDTO.getColumn()] == 0
                || existingCoordinatesBuilding != null) {
            throw new InvalidPositionException();
        }

        //create Building
        CastleBuilding castleBuilding = CastleBuilding
                .builder()
                .buildingEntity(buildingEntity)
                .coordinateX(createCastleBuildingDTO.getColumn())
                .coordinateY(createCastleBuildingDTO.getRow())
                .build();
        castleBuildingRepository.save(castleBuilding);
        userEntity.getCastle().getBuildings().add(castleBuilding);

        //give XP
        userEntity.setTotalXP(userEntity.getTotalXP() + buildingEntity.getBuildingXP());

        //subtract actual resources
        CastleEntity castleEntity = userEntity.getCastle();
        castleEntity.setWood(castleEntity.getWood() - buildingEntity.getWoodRequired());
        castleEntity.setStone(castleEntity.getStone() - buildingEntity.getStoneRequired());
    }

    @Transactional
    @Override
    public void upgradeBuilding(UserPrincipal principal, Long buildingId) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(principal.getUsername(),
                principal.getUsername()).orElseThrow(NotAuthorizedException::new);
        CastleBuilding castleBuilding = userEntity.getCastle().getBuildings()
                .stream().filter(building -> building.getId().equals(buildingId))
                .findFirst().orElseThrow(BuildingNotFoundException::new);
        int buildingIndex = userEntity.getCastle().getBuildings().indexOf(castleBuilding);

        //find next level building entity
        BuildingEntity nextLevelBuilding = buildingEntityRepository.findByBuildingTypeBuildingNameAndLevel(
                castleBuilding.getBuildingEntity().getBuildingType().getBuildingName(),
                castleBuilding.getBuildingEntity().getLevel() + 1).orElse(null);

        if (nextLevelBuilding == null) {
            throw new BuildingMaxLevelReachedException();
        }

        int userLevel = UserService.calculateUserLevel(userEntity);
        //check if unlocked
        if (nextLevelBuilding.getUnlocksOnLevel() > userLevel) {
            throw new NotUnlockedException();
        }

        if (nextLevelBuilding.getStoneRequired() > userEntity.getCastle().getStone() ||
                nextLevelBuilding.getWoodRequired() > userEntity.getCastle().getWood()) {
            throw new NotEnoughResourcesException();
        }

        castleBuilding.setBuildingEntity(nextLevelBuilding);
        userEntity.getCastle().getBuildings().set(buildingIndex, castleBuilding);

        //give XP
        userEntity.setTotalXP(userEntity.getTotalXP() + nextLevelBuilding.getBuildingXP());

        //subtract actual resources
        CastleEntity castleEntity = userEntity.getCastle();
        castleEntity.setWood(castleEntity.getWood() - nextLevelBuilding.getWoodRequired());
        castleEntity.setStone(castleEntity.getStone() - nextLevelBuilding.getStoneRequired());
    }

    @Transactional
    @Override
    public void destroyBuilding(UserPrincipal userPrincipal, DestroyBuildingDTO destroyBuildingDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);
        CastleBuilding castleBuilding = userEntity.getCastle().getBuildings().stream()
                .filter(building -> building.getId() == destroyBuildingDTO.getBuildingId())
                .findFirst().orElseThrow(BuildingNotFoundException::new);

        if (!castleBuilding.getBuildingEntity().getBuildingType().isDestroyable()) {
            throw new BuildingNotDestroyableException();
        }

        CastleEntity castleEntity = userEntity.getCastle();
        castleEntity.getBuildings().remove(castleBuilding);
    }
}
