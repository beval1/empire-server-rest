package com.beval.server.service.impl;

import com.beval.server.dto.response.CastleBuildingDTO;
import com.beval.server.dto.response.CastleDTO;
import com.beval.server.dto.response.EnemyCastleDTO;
import com.beval.server.dto.response.MapCastleDTO;
import com.beval.server.exception.*;
import com.beval.server.model.entity.*;
import com.beval.server.repository.BuildingEntityRepository;
import com.beval.server.repository.CastleBuildingRepository;
import com.beval.server.repository.CastleRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.CastleService;
import com.beval.server.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.beval.server.config.AppConstants.*;

@Service
public class CastleServiceImpl implements CastleService {
    private final UserRepository userRepository;
    private final CastleRepository castleRepository;
    private final BuildingEntityRepository buildingEntityRepository;
    private final CastleBuildingRepository castleBuildingRepository;
    private final ModelMapper modelMapper;

    public CastleServiceImpl(UserRepository userRepository, CastleRepository castleRepository, BuildingEntityRepository buildingEntityRepository, CastleBuildingRepository castleBuildingRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.castleRepository = castleRepository;
        this.buildingEntityRepository = buildingEntityRepository;
        this.castleBuildingRepository = castleBuildingRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public CastleDTO getCastle(UserPrincipal principal) {
        String usernameToFind = principal.getUsername();
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
    public EnemyCastleDTO getEnemyCastle(String enemyUsername) {
        CastleEntity castleEntity = castleRepository.findCastleEntitiesByOwner_Username(enemyUsername)
                .orElseThrow(CastleNotFoundException::new);
        EnemyCastleDTO enemyCastleDTO = new EnemyCastleDTO();
        enemyCastleDTO.setBuildings(Arrays.asList(modelMapper.map(castleEntity.getBuildings(), CastleBuildingDTO[].class)));
        return enemyCastleDTO;
    }

    @Transactional
    @Override
    public void createCastleForUser(UserEntity user) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(NotAuthorizedException::new);

        if (userEntity.getCastle() != null) {
            throw new CastleAlreadyExistsException();
        }

        BuildingEntity mainTower = buildingEntityRepository
                .findByBuildingTypeBuildingNameAndLevel("The Keep", 1)
                .orElseThrow(BuildingNotFoundException::new);

        CastleBuilding keep = castleBuildingRepository.save(
                CastleBuilding.builder()
                        .buildingEntity(mainTower)
                        .coordinateX(KEEP_COORDINATES.get(0)).coordinateY(KEEP_COORDINATES.get(1)).build());


        //TODO: refactor this with Position Entity in Database, this is not optimal
        List<CastleEntity> castleEntities = castleRepository.findAll();
        List<Integer> castleCoordinates = null;
        for (List<Integer> coordinates : mapCastleValidPositions) {
            if (castleEntities.stream().noneMatch(castleEntity -> castleEntity.getCoordinateX() == coordinates.get(0) &&
                    castleEntity.getCoordinateY() == coordinates.get(1) &&
                    castleEntity.getQuadrant() == coordinates.get(2))) {
                castleCoordinates = coordinates;
            }
        }

        if (castleCoordinates == null){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "No free map position for your castle! Can't register your account");
        }

        CastleEntity castleEntity = castleRepository.save(CastleEntity.builder()
                .castleName("Not implemented")
                .buildings(List.of(keep))
                .coordinateX(castleCoordinates.get(0))
                .coordinateY(castleCoordinates.get(1))
                .quadrant(castleCoordinates.get(2))
                .food(CASTLE_STARTING_FOOD)
                .wood(CASTLE_STARTING_WOOD)
                .stone(CASTLE_STARTING_STONE)
                .owner(user)
                .build());

        userEntity.setCastle(castleEntity);
    }

    @Transactional
    @Override
    public List<MapCastleDTO> getAllCastlesForQuadrant(UserPrincipal userPrincipal, int quadrant) {
        List<MapCastleDTO> mapCastleDTOS = new ArrayList<>();
        List<CastleEntity> castleEntities = castleRepository.findAllByQuadrant(quadrant);
        for (CastleEntity castleEntity : castleEntities) {
            MapCastleDTO mapCastleDTO = modelMapper.map(castleEntity, MapCastleDTO.class);
            int mainKeepLevel = castleEntity.getBuildings()
                    .stream()
                    .filter(building -> building.getBuildingEntity().getBuildingType().getBuildingName().equals("The Keep"))
                    .findFirst()
                    .orElseThrow(() -> new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "No 'The Keep' in the castle"))
                    .getBuildingEntity().getLevel();

            String castleMapImage = keepMapImages.get(mainKeepLevel);
            if (castleMapImage == null) {
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "No image for keep level");
            }
            mapCastleDTO.setCastleImage(castleMapImage);
            mapCastleDTO.setOwnerUsername(castleEntity.getOwner().getUsername());
            int userLevel = UserService.calculateUserLevel(castleEntity.getOwner());
            mapCastleDTO.setOwnerLevel(userLevel);
            mapCastleDTO.setOwnerMightyPoints(castleEntity.getOwner().getMightyPoints());

            mapCastleDTOS.add(mapCastleDTO);
        }
        return mapCastleDTOS;
    }

}
