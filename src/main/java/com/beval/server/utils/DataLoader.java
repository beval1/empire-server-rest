package com.beval.server.utils;

import com.beval.server.model.entity.*;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
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

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, BuildingTypeRepository buildingRepository, BuildingEntityRepository buildingEntityLevelRepository, CastleRepository castleRepository, CastleBuildingRepository castleBuildingRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.buildingRepository = buildingRepository;
        this.buildingEntityLevelRepository = buildingEntityLevelRepository;
        this.castleRepository = castleRepository;
        this.castleBuildingRepository = castleBuildingRepository;
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
                            .build()
            );

            //TODO: add building image; save building position
            BuildingType barracks = buildingRepository.save(BuildingType
                    .builder()
                    .buildingName("Barracks")
                    .build());
            BuildingEntity barracksLevel1 = buildingEntityLevelRepository.save(BuildingEntity
                    .builder()
                    .level(1)
                    .unlocksOnLevel(0)
                    .buildingType(barracks)
                    .buildingTimeSeconds(3600)
                    .buildingImage("https://res.cloudinary.com/djog8qqis/image/upload/v1669670047/empire/buildings/barracks/barracks_level_1-removebg_wq1oxr.png")
                    .woodRequired(0)
                    .stoneRequired(1)
                    .build());

            CastleBuilding castleBarracksLevel1 = castleBuildingRepository.save(
                    CastleBuilding.builder()
                            .buildingEntity(barracksLevel1)
                    .coordinateX(15).coordinateY(10).build());

            CastleEntity castleEntity = castleRepository.save(CastleEntity.builder()
                    .castleName("beval")
                    .buildings(List.of(castleBarracksLevel1))
                            .coordinateX(10)
                            .coordinateY(10)
                    .build());

            user.setCastle(castleEntity);
        }
    }
}
