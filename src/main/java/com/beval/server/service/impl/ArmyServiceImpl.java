package com.beval.server.service.impl;

import com.beval.server.dto.payload.BuyArmyUnitsDTO;
import com.beval.server.dto.response.ArmyUnitDTO;
import com.beval.server.dto.response.CastleArmyDTO;
import com.beval.server.exception.*;
import com.beval.server.model.entity.ArmyUnitEntity;
import com.beval.server.model.entity.CastleArmy;
import com.beval.server.model.entity.CastleEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.ArmyUnitRepository;
import com.beval.server.repository.CastleArmyRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.ArmyService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class ArmyServiceImpl implements ArmyService  {
    private final CastleArmyRepository castleArmyRepository;
    private final ArmyUnitRepository armyUnitRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ArmyServiceImpl(CastleArmyRepository castleArmyRepository, ArmyUnitRepository armyUnitRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.castleArmyRepository = castleArmyRepository;
        this.armyUnitRepository = armyUnitRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public List<ArmyUnitDTO> getAllUnitsForUserBarracksLevel(UserPrincipal userPrincipal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);
        List<ArmyUnitEntity> armyUnitEntities = armyUnitRepository.findAll();
        int userBarracksLevel = userEntity.getCastle().getBuildings()
                .stream()
                .filter(building -> building.getBuildingEntity().getBuildingType().getBuildingName().equals("Barracks"))
                .findFirst()
                .orElseThrow(() -> new BuildingNotFoundException(HttpStatus.BAD_REQUEST, "No barracks in the castle!"))
                .getBuildingEntity().getLevel();

        //filter by barracksLevel
        List<ArmyUnitEntity> filteredArmyEntities = armyUnitEntities.stream()
                .filter(armyUnitEntity -> armyUnitEntity.getBarracksLevel() <= userBarracksLevel)
                .toList();

        return Arrays.asList(modelMapper.map(filteredArmyEntities, ArmyUnitDTO[].class));
    }

    @Transactional
    @Override
    public List<CastleArmyDTO> getAllUnitsForCastle(UserPrincipal userPrincipal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);
        List<CastleArmy> castleArmies = userEntity.getCastle().getArmy();
        return Arrays.asList(modelMapper.map(castleArmies, CastleArmyDTO[].class));
    }

    @Transactional
    @Override
    public void buyUnits(UserPrincipal userPrincipal, BuyArmyUnitsDTO buyArmyUnitsDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);
        ArmyUnitEntity armyUnitEntity = armyUnitRepository.findById(buyArmyUnitsDTO.getArmyUnitId())
                .orElseThrow(ResourceNotFoundException::new);
        CastleEntity castleEntity = userEntity.getCastle();

        //check if coins are enough
        int totalCost = armyUnitEntity.getCoinPrice() * buyArmyUnitsDTO.getCount();
        if (totalCost > userEntity.getCoins()){
            throw new NotEnoughResourcesException(HttpStatus.BAD_REQUEST, "Not enough coins!");
        }

        //check if food is enough
//        double foodProductionPerHour = ProduceResourceTask.calculateProductionPerHour(castleEntity, "Granary");
//        if (armyUnitEntity.getFoodConsumption() * buyArmyUnitsDTO.getCount() > foodProductionPerHour){
//            throw new NotEnoughResourcesException(HttpStatus.BAD_REQUEST, "Not enough food production!");
//        }

        //check barracks level
        int userBarracksLevel = userEntity.getCastle().getBuildings()
                .stream()
                .filter(building -> building.getBuildingEntity().getBuildingType().getBuildingName().equals("Barracks"))
                .findFirst()
                .orElseThrow(() -> new BuildingNotFoundException(HttpStatus.BAD_REQUEST, "No barracks in the castle!"))
                .getBuildingEntity().getLevel();
        if (userBarracksLevel < armyUnitEntity.getBarracksLevel()){
            throw new NotUnlockedException();
        }

        List<CastleArmy> castleArmyUnitsList = castleEntity.getArmy();
        CastleArmy existingCastleArmy = castleArmyUnitsList.stream()
                .filter(castleArmy -> castleArmy.getArmyUnit().getId().equals(armyUnitEntity.getId()))
                .findAny().orElse(null);
        if (existingCastleArmy == null) {
            CastleArmy newCastleArmy = castleArmyRepository.save(CastleArmy
                    .builder()
                    .armyUnit(armyUnitEntity)
                    .armyUnitCount(buyArmyUnitsDTO.getCount())
                    .build());
            castleArmyUnitsList.add(newCastleArmy);
        } else {
            int index = castleArmyUnitsList.indexOf(existingCastleArmy);
            existingCastleArmy.setArmyUnitCount(existingCastleArmy.getArmyUnitCount() + buyArmyUnitsDTO.getCount());
            castleArmyUnitsList.set(index, existingCastleArmy);
        }

        userEntity.setCoins(userEntity.getCoins() - totalCost);
    }
}
