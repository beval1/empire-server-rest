package com.beval.server.schedule;

import com.beval.server.model.entity.CastleArmy;
import com.beval.server.model.entity.CastleEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.beval.server.config.AppConstants.CITIZEN_COINS_MULTIPLIER;

@Component
@Slf4j
public class ProduceResourceTask {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final UserRepository userRepository;

    public ProduceResourceTask(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //run every minute
//    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void produceResources() {
        log.info("Assigning resources. The time now: {}", dateFormat.format(new Date()));
        List<UserEntity> users = userRepository.findAll();
        for (UserEntity user : users) {
            CastleEntity userCastle = user.getCastle();
            int citizens = (int) calculateProductionPerHour(userCastle, "Dwelling");
            userCastle.setCitizens(citizens);
            userCastle.setWood(userCastle.getWood() + calculateProductionPerMinute(userCastle, "Woodcutter"));
            userCastle.setStone(userCastle.getStone() + calculateProductionPerMinute(userCastle, "Stone Quarry"));
            double foodProductionConsumptionMinuteDiff = calculateProductionPerMinute(userCastle, "Granary") - calculateFoodConsumptionPerMinute(userCastle);
            double newFoodTotal = userCastle.getFood() + foodProductionConsumptionMinuteDiff;
            userCastle.setFood(newFoodTotal);
            if (newFoodTotal <= 0){
                desertSoldiers(userCastle);
            }
            user.setCoins(user.getCoins() + citizens * CITIZEN_COINS_MULTIPLIER);
        }
    }

    private void desertSoldiers(CastleEntity userCastle) {
        double foodProductionConsumptionHourDiff = calculateProductionPerHour(userCastle, "Granary") -
                calculateFoodConsumptionPerHour(userCastle);
        userCastle.setFood(0);
        List<CastleArmy> castleArmies = userCastle.getArmy();
        int desertedSoldiers = 0;
        armyType:
        for (CastleArmy castleArmy : castleArmies) {
            int unitConsumption = castleArmy.getArmyUnit().getFoodConsumption();
            for (int j = 0; j < castleArmy.getArmyUnitCount(); j++) {
                castleArmy.setArmyUnitCount(castleArmy.getArmyUnitCount() - 1);
                foodProductionConsumptionHourDiff += unitConsumption;
                desertedSoldiers++;
                if (foodProductionConsumptionHourDiff > 0) {
                    break armyType;
                }
            }
        }
        log.info("Deserted soldier {}", desertedSoldiers);
    }

    public static double calculateProductionPerHour(CastleEntity castleEntity, String buildingTypeName) {
        return castleEntity.getBuildings().stream().filter(building -> building.getBuildingEntity().getBuildingType()
                        .getBuildingName().equals(buildingTypeName))
                .map(building -> building.getBuildingEntity().getProduction())
                .reduce(0, Integer::sum);
    }

    private double calculateProductionPerMinute(CastleEntity userCastle, String buildingName) {
        return calculateProductionPerHour(userCastle, buildingName) / 60.0;
    }

    private double calculateFoodConsumptionPerHour(CastleEntity userCastle) {
        return userCastle.getArmy()
                .stream()
                .mapToInt(castleArmy -> castleArmy.getArmyUnit().getFoodConsumption() * castleArmy.getArmyUnitCount())
                .sum();
    }

    private double calculateFoodConsumptionPerMinute(CastleEntity userCastle) {
        return calculateFoodConsumptionPerHour(userCastle) / 60.0;
    }
}