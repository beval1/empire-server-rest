package com.beval.server.schedule;

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

@Component
@Slf4j
public class ProduceResourceTask {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final UserRepository userRepository;

    public ProduceResourceTask(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //run every minute
    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void produceResources() {
        log.info("Assigning resources. The time now: {}", dateFormat.format(new Date()));
        List<UserEntity> users = userRepository.findAll();
        for (UserEntity user : users) {
            CastleEntity userCastle = user.getCastle();
            int citizens = calculateProductionPerHour(userCastle, "Dwelling");
            userCastle.setWood(userCastle.getWood() + calculateProductionPerMinute(userCastle, "Woodcutter"));
            userCastle.setStone(userCastle.getStone() + calculateProductionPerMinute(userCastle, "Stone Quarry"));
            userCastle.setFood(userCastle.getFood() + calculateProductionPerMinute(userCastle, "Granary"));
            userCastle.setCitizens(citizens);
        }
    }

    private int calculateProductionPerHour(CastleEntity castleEntity, String buildingTypeName) {
        return castleEntity.getBuildings().stream().filter(building -> building.getBuildingEntity().getBuildingType()
                .getBuildingName().equals(buildingTypeName))
                .map(building -> building.getBuildingEntity().getProduction())
                .reduce(0, Integer::sum);
    }

    private int calculateProductionPerMinute(CastleEntity userCastle, String buildingName){
        return (int) Math.ceil(calculateProductionPerHour(userCastle, buildingName) / 60.0);
    }

}