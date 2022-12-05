package com.beval.server.utils;

import com.beval.server.model.entity.RoleEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.CastleRepository;
import com.beval.server.repository.RoleRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.service.CastleService;
import com.beval.server.utils.loaders.ArmyUnitLoader;
import com.beval.server.utils.loaders.BuildingLoader;
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
    private final BuildingLoader buildingLoader;
    private final ArmyUnitLoader armyUnitLoader;
    private final CastleRepository castleRepository;
    private final CastleService castleService;

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, BuildingLoader buildingLoader,
                      ArmyUnitLoader armyUnitLoader, CastleRepository castleRepository, CastleService castleService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.buildingLoader = buildingLoader;
        this.armyUnitLoader = armyUnitLoader;
        this.castleRepository = castleRepository;
        this.castleService = castleService;
    }

    @Transactional
    public void run(ApplicationArguments args) {
        if (roleRepository.count() == 0) {
            //set user roles
            RoleEntity adminRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.ADMIN).build());
            RoleEntity userRole = roleRepository.save(RoleEntity.builder().roleName(RoleEnum.USER).build());

            UserEntity user2 = userRepository.save(
                    UserEntity
                            .builder()
                            .username("user2")
                            .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                            .enabled(true)
                            .roles(Set.of(userRole))
                            .firstName(null)
                            .lastName(null)
                            .totalXP(0)
                            .email("deleted@deleted.com")
                            .coins(10000)
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
                            .totalXP(0)
                            .coins(1000000)
                            .build()
            );

            UserEntity user3 = userRepository.save(
                    UserEntity
                            .builder()
                            .username("user3")
                            .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                            .enabled(true)
                            .roles(Set.of(userRole))
                            .firstName("Test")
                            .lastName("Test")
                            .email("user3@test.com")
                            .totalXP(0)
                            .coins(10000)
                            .build()
            );

            UserEntity user4 = userRepository.save(
                    UserEntity
                            .builder()
                            .username("user4")
                            .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                            .enabled(true)
                            .roles(Set.of(userRole))
                            .firstName("Test")
                            .lastName("Test")
                            .email("user4@test.com")
                            .totalXP(0)
                            .coins(10000)
                            .build()
            );

            UserEntity adminUser = userRepository.save(
                    UserEntity
                            .builder()
                            .username("admin")
                            .password("$2a$12$w.GNfFrtuRMFSxWq0TZsgO2M/O3jTwZ8cvdL3X/EW0XQKNitCqD6K")
                            .enabled(true)
                            .roles(Set.of(adminRole))
                            .firstName("Admin")
                            .lastName("Adminov")
                            .email("admin@admin.com")
                            .totalXP(0)
                            .coins(10000)
                            .build()
            );

            //load all buildings
            buildingLoader.loadAll();
            armyUnitLoader.loadAll();

            castleService.createCastleForUser(user);
            castleService.createCastleForUser(adminUser);
            castleService.createCastleForUser(user2);
            castleService.createCastleForUser(user3);
            castleService.createCastleForUser(user4);

            user.setCastle(castleRepository.findCastleEntitiesByOwner(user));
            user2.setCastle(castleRepository.findCastleEntitiesByOwner(user2));
            user3.setCastle(castleRepository.findCastleEntitiesByOwner(user3));
            user4.setCastle(castleRepository.findCastleEntitiesByOwner(user4));
            adminUser.setCastle(castleRepository.findCastleEntitiesByOwner(adminUser));
        }
    }

}
