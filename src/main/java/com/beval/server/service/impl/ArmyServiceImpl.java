package com.beval.server.service.impl;

import com.beval.server.dto.payload.AttackDTO;
import com.beval.server.dto.payload.BuyArmyUnitsDTO;
import com.beval.server.dto.response.ArmyUnitDTO;
import com.beval.server.dto.response.CastleArmyDTO;
import com.beval.server.exception.*;
import com.beval.server.model.entity.*;
import com.beval.server.model.enums.ArmyUnitTypeEnum;
import com.beval.server.repository.ArmyUnitRepository;
import com.beval.server.repository.BattleReportRepository;
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
    private final BattleReportRepository battleReportRepository;

    public ArmyServiceImpl(CastleArmyRepository castleArmyRepository, ArmyUnitRepository armyUnitRepository, UserRepository userRepository, ModelMapper modelMapper, BattleReportRepository battleReportRepository) {
        this.castleArmyRepository = castleArmyRepository;
        this.armyUnitRepository = armyUnitRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.battleReportRepository = battleReportRepository;
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

    @Override
    public void launchAttack(UserPrincipal userPrincipal, String victimUsername, AttackDTO attackDTO) {
        UserEntity attacker = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);
        UserEntity defender = userRepository.findByUsernameOrEmail(victimUsername, victimUsername)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Can't find victim"));

        List<CastleArmy> defenseUnits = defender.getCastle().getArmy().stream()
                .filter(castleArmy -> castleArmy.getArmyUnit().getUnitType() == ArmyUnitTypeEnum.DEFENSE).toList();

        int totalDefensePower = defenseUnits.stream().mapToInt(unit -> (unit.getArmyUnit().getMeleeDefensePower() +
                unit.getArmyUnit().getRangedDefensePower()) * unit.getArmyUnitCount()).sum();
        int totalAttackPower = attackDTO.getArmyList().stream().mapToInt(unit -> (unit.getArmyUnit().getMeleeAttackPower() +
                unit.getArmyUnit().getRangedAttackPower()) * unit.getArmyUnitCount()).sum();

        int combatPowerResult = totalDefensePower - totalAttackPower;
        //choose winner
        UserEntity winner = combatPowerResult < 0 ? attacker : defender;
        //calculate victims
        if (winner != defender) {
            ArmyKilling:
            for (CastleArmy defenseUnit : defenseUnits) {
                for (int j = 0; j < defenseUnit.getArmyUnitCount(); j++) {
                    totalDefensePower -= defenseUnit.getArmyUnit().getMeleeDefensePower() +
                            defenseUnit.getArmyUnit().getRangedDefensePower();
                    defenseUnit.setArmyUnitCount(defenseUnit.getArmyUnitCount()-1);
                    if (totalDefensePower <= 0){
                        break ArmyKilling;
                    }
                }
            }
        } else {
            ArmyKilling:
            for (CastleArmyDTO attack : attackDTO.getArmyList()) {
                for (int j = 0; j < attack.getArmyUnitCount(); j++) {
                    totalAttackPower -= attack.getArmyUnit().getMeleeAttackPower() +
                            attack.getArmyUnit().getRangedAttackPower();
                    attack.setArmyUnitCount(attack.getArmyUnitCount()-1);
                    if (totalAttackPower <= 0){
                        break ArmyKilling;
                    }
                }
            }
        }
        //generate report for both sides
        BattleReportEntity battleReportEntity = battleReportRepository.save(BattleReportEntity
                .builder()
                        .attacker(attacker)
                        .attackingArmy()
                        .defender(defender)
                        .attackingArmyVictims()
                .build());
        attacker.getBattleReports().add(battleReportEntity)
    }
}
