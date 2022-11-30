package com.beval.server.utils;

import com.beval.server.model.entity.BuildingEntity;
import com.beval.server.model.entity.BuildingType;
import com.beval.server.model.entity.RoleEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.*;
import com.beval.server.service.CastleService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

@Component
@ConditionalOnProperty("app.dataLoader")
public class DataLoader implements ApplicationRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BuildingTypeRepository buildingRepository;
    private final BuildingEntityRepository buildingEntityLevelRepository;
    private final CastleRepository castleRepository;
    private final CastleBuildingRepository castleBuildingRepository;
    private final CastleService castleService;

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, BuildingTypeRepository buildingRepository, BuildingEntityRepository buildingEntityLevelRepository, CastleRepository castleRepository, CastleBuildingRepository castleBuildingRepository, CastleService castleService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.buildingRepository = buildingRepository;
        this.buildingEntityLevelRepository = buildingEntityLevelRepository;
        this.castleRepository = castleRepository;
        this.castleBuildingRepository = castleBuildingRepository;
        this.castleService = castleService;
    }

    @Transactional
    public void run(ApplicationArguments args) {
        if (roleRepository.count() == 0) {
            //set user roles
            RoleEntity adminRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.ADMIN).build());
            RoleEntity userRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.USER).build());

            UserEntity deletedUser = userRepository.save(
                    UserEntity
                            .builder()
                            .username("deleted")
                            .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                            .enabled(false)
                            .roles(Set.of(userRole))
                            .firstName(null)
                            .lastName(null)
                            .totalXP(0)
                            .email("deleted@deleted.com")
                            .build()
            );

            UserEntity user = userRepository.save(
                    UserEntity
                            .builder()
                            .username("test1")
                            .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                            .enabled(true)
                            .roles(Set.of(userRole))
                            .firstName("Test")
                            .lastName("Test")
                            .email("test@test.com")
                            .totalXP(100)
                            .build()
            );

            userRepository.save(
                    UserEntity
                            .builder()
                            .username("admin")
                            .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                            .enabled(true)
                            .roles(Set.of(adminRole))
                            .firstName("Admin")
                            .lastName("Adminov")
                            .email("admin@admin.com")
                            .totalXP(5)
                            .build()
            );

            initializeBuilding();

            user.setCastle(castleService.createCastle());
        }
    }

    private void initializeBuilding() {
        keep();
        barracks();
        woodcutter();
        stoneQuarry();
        granary();
        dwelling();
    }

    private void woodcutter() {
        BuildingType woodcutter = buildingRepository.save(BuildingType
                .builder()
                .buildingName("Woodcutter")
                        .buildable(true)
                .build());
        BuildingEntity woodcutterLeve1 = buildingEntityLevelRepository.save(BuildingEntity
                .builder()
                .level(1)
                .unlocksOnLevel(0)
                .buildingType(woodcutter)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669727847/empire/buildings/woodcutter/woodcutter_small_img-removebg-preview_hm2krq.png")
                .woodRequired(0)
                .stoneRequired(1)
                .build());
    }

    private void stoneQuarry() {
        BuildingType stoneQuarry = buildingRepository.save(BuildingType
                .builder()
                .buildingName("Stone Quarry")
                        .buildable(true)
                .build());
        BuildingEntity stoneQuarryLevel1 = buildingEntityLevelRepository.save(BuildingEntity
                .builder()
                .level(1)
                .unlocksOnLevel(0)
                .buildingType(stoneQuarry)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669727893/empire/buildings/stonequarry/stonequarry_small_img-removebg-preview_mklt4x.png")
                .woodRequired(0)
                .stoneRequired(1)
                .build());
    }

    private void granary() {
        BuildingType granary = buildingRepository.save(BuildingType
                .builder()
                .buildingName("Granary")
                        .buildable(true)
                .build());
        BuildingEntity granaryLevel1 = buildingEntityLevelRepository.save(BuildingEntity
                .builder()
                .level(1)
                .unlocksOnLevel(0)
                .buildingType(granary)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669727583/empire/buildings/granary/granary_qkb6nl.png")
                .woodRequired(0)
                .stoneRequired(1)
                .build());
    }

    private void dwelling() {
        BuildingType dwelling = buildingRepository.save(BuildingType
                .builder()
                .buildingName("Dwelling")
                        .buildable(true)
                .build());
        BuildingEntity dwellingLevel1 = buildingEntityLevelRepository.save(BuildingEntity
                .builder()
                .level(1)
                .unlocksOnLevel(0)
                .buildingType(dwelling)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669727607/empire/buildings/dwelling/dwelling_leve1-removebg_h9xpmr.png")
                .woodRequired(0)
                .stoneRequired(1)
                .build());
    }

    private void barracks() {
        BuildingType theKeep = buildingRepository.save(BuildingType
                .builder()
                .buildingName("Barracks")
                .buildable(true)
                .castleLimit(1)
                .build());
        BuildingEntity theKeepLeve1 = buildingEntityLevelRepository.save(BuildingEntity
                .builder()
                .level(1)
                .unlocksOnLevel(11)
                .buildingType(theKeep)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669670047/empire/buildings/barracks/barracks_level_1-removebg_wq1oxr.png")
                .woodRequired(1000)
                .stoneRequired(1250)
                .build());
    }

    private void keep() {
        BuildingType theKeep = buildingRepository.save(BuildingType
                .builder()
                .buildingName("The Keep")
                        .buildable(false)
                .build());
        BuildingEntity theKeepLeve1 = buildingEntityLevelRepository.save(BuildingEntity
                .builder()
                .level(1)
                .unlocksOnLevel(11)
                .buildingType(theKeep)
                .buildingTimeSeconds(3600)
                .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669689647/empire/buildings/keep/keep_level1-removebg-preview_tgwddx.png")
                .woodRequired(1000)
                .stoneRequired(1250)
                .build());

    }
}
