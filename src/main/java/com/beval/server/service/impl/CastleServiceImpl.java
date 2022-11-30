package com.beval.server.service.impl;

import com.beval.server.dto.response.CastleDTO;
import com.beval.server.exception.BuildingNotFoundException;
import com.beval.server.exception.CastleAlreadyExistsException;
import com.beval.server.exception.CastleNotFoundException;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.model.entity.BuildingEntity;
import com.beval.server.model.entity.CastleBuilding;
import com.beval.server.model.entity.CastleEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.BuildingEntityRepository;
import com.beval.server.repository.CastleBuildingRepository;
import com.beval.server.repository.CastleRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.CastleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    public CastleEntity createCastle() {
        BuildingEntity mainTower = buildingEntityRepository
                .findByBuildingTypeBuildingNameAndLevel("The Keep", 1)
                .orElseThrow(BuildingNotFoundException::new);

        CastleBuilding castleBarracksLevel1 = castleBuildingRepository.save(
                CastleBuilding.builder()
                        .buildingEntity(mainTower)
                        .coordinateX(15).coordinateY(1).build());

        //TODO: generate valid coordinates for castle on world Map
        return castleRepository.save(CastleEntity.builder()
                .castleName("beval")
                .buildings(List.of(castleBarracksLevel1))
                .coordinateX(10)
                .coordinateY(10)
                .build());
    }

}
