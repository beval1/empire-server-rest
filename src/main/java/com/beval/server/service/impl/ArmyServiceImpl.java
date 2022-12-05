package com.beval.server.service.impl;

import com.beval.server.config.AppConstants;
import com.beval.server.dto.payload.AttackDTO;
import com.beval.server.dto.payload.BuyArmyUnitsDTO;
import com.beval.server.dto.payload.WaveDTO;
import com.beval.server.dto.response.ArmyUnitDTO;
import com.beval.server.dto.response.CastleArmyDTO;
import com.beval.server.exception.*;
import com.beval.server.model.entity.*;
import com.beval.server.model.enums.ArmyUnitTypeEnum;
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

import static com.beval.server.config.AppConstants.*;

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

    @Transactional
    @Override
    public void launchAttack(UserPrincipal userPrincipal, String victim, AttackDTO attackDTO) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);
        UserEntity victimEntity = userRepository.findByUsernameOrEmail(victim, victim)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "No victim entity found!"));

        CastleEntity victimCastle = victimEntity.getCastle();
        List<CastleArmy> castleArmies = victimCastle.getArmy().stream()
                .filter(castleArmy -> castleArmy.getArmyUnit().getUnitType() == ArmyUnitTypeEnum.DEFENSE).toList();
        CastleDefense castleDefense = victimCastle.getCastleDefense();

//        if (castleDefense.getFrontSoldierMeleePowerPercent() + castleDefense.getLeftFlankSoldierMeleePowerPercent() +
//        castleDefense.getRightFlankSoldiersPercent() > 100){
//            throw new ApiException("")
//        }

        //TODO: compensate for rounding
        int frontSoldierCount = (int) Math.round(CASTLE_WALL_LIMIT * castleDefense.getFrontSoldiersPercent() / 100.0);
        int leftFlankSoldierCount = (int) Math.round(CASTLE_WALL_LIMIT * castleDefense.getLeftFlankSoldiersPercent() / 100.0);
        int rightFlankSoldierCount = (int) Math.round(CASTLE_WALL_LIMIT * castleDefense.getRightFlankSoldiersPercent() / 100.0);

        List<WaveDTO> waves = attackDTO.getWaves();
        for (WaveDTO wave : waves){
            int totalAttackFrontMeleePower = 0;
            int totalAttackFrontRangedPower = 0;
            int totalAttackLeftFlankMeleePower = 0;
            int totalAttackLeftFlankRangedPower = 0;
            int totalAttackRightFlankMeleePower = 0;
            int totalAttackRightFlankRangedPower = 0;
            for (int i = 0; i < ATTACK_FRONT_MAX_SLOTS; i++) {
                CastleArmyDTO armyType = wave.getFrontArmy().get(i);
                totalAttackFrontMeleePower += armyType.getArmyUnit().getMeleeAttackPower() * armyType.getArmyUnitCount();
                totalAttackFrontRangedPower += armyType.getArmyUnit().getRangedAttackPower() * armyType.getArmyUnitCount();
            }
            for (int i = 0; i < ATTACK_FLANK_MAX_SLOTS; i++) {
                CastleArmyDTO armyType = wave.getLeftFlankArmy().get(i);
                totalAttackLeftFlankMeleePower += armyType.getArmyUnit().getMeleeAttackPower() * armyType.getArmyUnitCount();
                totalAttackLeftFlankRangedPower += armyType.getArmyUnit().getRangedAttackPower() * armyType.getArmyUnitCount();
            }
            for (int i = 0; i < ATTACK_FLANK_MAX_SLOTS; i++) {
                CastleArmyDTO armyType = wave.getRightFlankArmy().get(i);
                totalAttackRightFlankMeleePower += armyType.getArmyUnit().getMeleeAttackPower() * armyType.getArmyUnitCount();
                totalAttackRightFlankRangedPower += armyType.getArmyUnit().getRangedAttackPower() * armyType.getArmyUnitCount();
            }



        }
    }


}
