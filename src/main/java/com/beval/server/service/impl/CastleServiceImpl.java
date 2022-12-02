package com.beval.server.service.impl;

import com.beval.server.dto.payload.CreateCastleBuildingDTO;
import com.beval.server.dto.payload.DestroyBuildingDTO;
import com.beval.server.dto.payload.UpgradeBuildingDTO;
import com.beval.server.dto.response.CastleDTO;
import com.beval.server.exception.*;
import com.beval.server.model.entity.*;
import com.beval.server.repository.*;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.CastleService;
import com.beval.server.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.beval.server.config.AppConstants.*;

@Service
public class CastleServiceImpl implements CastleService {
    private final UserRepository userRepository;
    private final CastleRepository castleRepository;
    private final BuildingEntityRepository buildingEntityRepository;
    private final BuildingTypeRepository buildingTypeRepository;
    private final CastleBuildingRepository castleBuildingRepository;
    private final CastleArmyRepository castleArmyRepository;
    private final ArmyUnitRepository armyUnitRepository;
    private final ModelMapper modelMapper;

    public CastleServiceImpl(UserRepository userRepository, CastleRepository castleRepository, BuildingEntityRepository buildingEntityRepository, BuildingTypeRepository buildingTypeRepository, CastleBuildingRepository castleBuildingRepository, CastleArmyRepository castleArmyRepository, ArmyUnitRepository armyUnitRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.castleRepository = castleRepository;
        this.buildingEntityRepository = buildingEntityRepository;
        this.buildingTypeRepository = buildingTypeRepository;
        this.castleBuildingRepository = castleBuildingRepository;
        this.castleArmyRepository = castleArmyRepository;
        this.armyUnitRepository = armyUnitRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public CastleDTO getCastle(UserPrincipal principal, String castleOwnerUsername) {
        String usernameToFind = castleOwnerUsername == null ? principal.getUsername() : castleOwnerUsername;
        UserEntity userEntity = userRepository.findByUsernameOrEmail(usernameToFind,
                usernameToFind).orElseThrow(NotAuthorizedException::new);
        CastleEntity castleEntity = castleRepository.findById(userEntity.getCastle().getId())
                .orElseThrow(CastleNotFoundException::new);

        int armySize = castleEntity.getArmy().stream().map(CastleArmy::getArmyUnitCount).reduce(0, Integer::sum);
        CastleDTO castleDTO = modelMapper.map(castleEntity, CastleDTO.class);
        castleDTO.setArmySize(armySize);
        return castleDTO;
    }

    @Transactional
    @Override
    public void createCastleForUser(UserEntity user) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(NotAuthorizedException::new);

        if (userEntity.getCastle() != null) {
            throw new CastleAlreadyExistsException();
        }

        CastleEntity castleEntity = createCastle();
        userEntity.setCastle(castleEntity);
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
            throw new BuildingNotUnlockedException();
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
        userEntity.setCastle(castleEntity);
    }

    @Transactional
    @Override
    public CastleEntity createCastle() {
        BuildingEntity mainTower = buildingEntityRepository
                .findByBuildingTypeBuildingNameAndLevel("The Keep", 1)
                .orElseThrow(BuildingNotFoundException::new);

        CastleBuilding keep = castleBuildingRepository.save(
                CastleBuilding.builder()
                        .buildingEntity(mainTower)
                        .coordinateX(KEEP_COORDINATES.get(0)).coordinateY(KEEP_COORDINATES.get(1)).build());

        //TODO: generate valid coordinates for castle on world Map
        return castleRepository.save(CastleEntity.builder()
                .castleName("beval")
                .buildings(List.of(keep))
                .coordinateX(10)
                .coordinateY(10)
                .food(CASTLE_STARTING_FOOD)
                .wood(CASTLE_STARTING_WOOD)
                .stone(CASTLE_STARTING_STONE)
                .build());
    }

    @Transactional
    @Override
    public void upgradeBuilding(UserPrincipal principal, UpgradeBuildingDTO upgradeBuildingDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(principal.getUsername(),
                principal.getUsername()).orElseThrow(NotAuthorizedException::new);
        CastleBuilding castleBuilding = userEntity.getCastle().getBuildings()
                .stream().filter(building -> building.getId() == upgradeBuildingDTO.getBuildingId())
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
            throw new BuildingNotUnlockedException();
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
        userEntity.setCastle(castleEntity);
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
        userEntity.setCastle(castleEntity);
    }

    @Transactional
    @Override
    public void buyUnits(UserPrincipal userPrincipal, Long armyUnitId, int armyCount) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);
        ArmyUnitEntity armyUnitEntity = armyUnitRepository.findById(armyUnitId)
                .orElseThrow(ResourceNotFoundException::new);
        List<CastleArmy> castleArmy = userEntity.getCastle().getArmy();
        ArmyUnitEntity existingArmyUnit = castleArmy.stream().map(CastleArmy::getArmyUnit)
                .filter(unitType -> unitType.getId().equals(armyUnitEntity.getId()))
                .findAny().orElse(null);
        if (existingArmyUnit == null) {
            castleArmy.add(CastleArmy
                    .builder()
                            .armyUnit(armyUnitEntity)
                            .armyUnitCount()
                    .build())
        }
    }

}
