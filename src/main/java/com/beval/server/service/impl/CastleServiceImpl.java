package com.beval.server.service.impl;

import com.beval.server.dto.payload.CreateCastleBuildingDTO;
import com.beval.server.dto.response.CastleDTO;
import com.beval.server.exception.*;
import com.beval.server.model.entity.*;
import com.beval.server.repository.*;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.CastleService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.beval.server.config.AppConstants.KEEP_COORDINATES;

@Service
public class CastleServiceImpl implements CastleService {
    private final UserRepository userRepository;
    private final CastleRepository castleRepository;
    private final BuildingEntityRepository buildingEntityRepository;
    private final BuildingTypeRepository buildingTypeRepository;
    private final CastleBuildingRepository castleBuildingRepository;
    private final ModelMapper modelMapper;

    public CastleServiceImpl(UserRepository userRepository, CastleRepository castleRepository, BuildingEntityRepository buildingEntityRepository, BuildingTypeRepository buildingTypeRepository, CastleBuildingRepository castleBuildingRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.castleRepository = castleRepository;
        this.buildingEntityRepository = buildingEntityRepository;
        this.buildingTypeRepository = buildingTypeRepository;
        this.castleBuildingRepository = castleBuildingRepository;
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

        return modelMapper.map(castleEntity, CastleDTO.class);
    }

    @Transactional
    @Override
    public void createCastleForUser(UserPrincipal principal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(principal.getUsername(),
                principal.getUsername()).orElseThrow(NotAuthorizedException::new);

        if (userEntity.getCastle() != null){
            throw new CastleAlreadyExistsException();
        }

        CastleEntity castleEntity = createCastle();
        userEntity.setCastle(castleEntity);
    }

    @Transactional
    @Override
    public void createBuilding(UserPrincipal userPrincipal, CreateCastleBuildingDTO createCastleBuildingDTO) {
        System.out.println(createCastleBuildingDTO.getColumn());
        System.out.println(createCastleBuildingDTO.getRow());

        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);

        BuildingType buildingType = buildingTypeRepository.findById(createCastleBuildingDTO.getTypeId())
                .orElseThrow(ResourceNotFoundException::new);

        //check if max building limit for castle is reached
        List<CastleBuilding> castleBuildingsOfType = castleBuildingRepository
                .findAllByBuildingEntity_BuildingType(buildingType);
        if (castleBuildingsOfType.size() >= buildingType.getCastleLimit()){
            throw new MaxBuildingLimitReachedException();
        }

        //check if buildable
        if (!buildingType.isBuildable()){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Building not buildable!");
        }

        //create Building
        BuildingEntity buildingEntity = buildingEntityRepository
                .findByBuildingTypeBuildingNameAndLevel(buildingType.getBuildingName(), 1)
                .orElseThrow(ResourceNotFoundException::new);

        if (buildingEntity.getStoneRequired() > userEntity.getCastle().getStone() ||
                buildingEntity.getWoodRequired() > userEntity.getCastle().getWood()){
            throw new NotEnoughResourcesException();
        }

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
                        .food(2500)
                        .wood(2500)
                        .stone(2500)
                .build());
    }

}
