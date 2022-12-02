package com.beval.server.service.impl;

import com.beval.server.dto.response.ArmyUnitDTO;
import com.beval.server.exception.NoBarracksException;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.model.entity.ArmyUnitEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.ArmyUnitRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.ArmyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class ArmyServiceImpl implements ArmyService  {
    private final ArmyUnitRepository armyUnitRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ArmyServiceImpl(ArmyUnitRepository armyUnitRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.armyUnitRepository = armyUnitRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public List<ArmyUnitDTO> getAllForUserBarracksLevel(UserPrincipal userPrincipal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);
        List<ArmyUnitEntity> armyUnitEntities = armyUnitRepository.findAll();
        int userBarracksLevel = userEntity.getCastle().getBuildings()
                .stream()
                .filter(building -> building.getBuildingEntity().getBuildingType().getBuildingName().equals("Barracks"))
                .findFirst().orElseThrow(NoBarracksException::new)
                .getBuildingEntity().getLevel();

        //filter by barracksLevel
        List<ArmyUnitEntity> filteredArmyEntities = armyUnitEntities.stream()
                .filter(armyUnitEntity -> armyUnitEntity.getBarracksLevel() <= userBarracksLevel)
                .toList();

        return Arrays.asList(modelMapper.map(filteredArmyEntities, ArmyUnitDTO[].class));
    }
}
