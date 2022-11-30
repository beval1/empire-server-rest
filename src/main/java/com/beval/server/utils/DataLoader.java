package com.beval.server.utils;

import com.beval.server.model.entity.RoleEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.model.enums.RoleEnum;
import com.beval.server.repository.RoleRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.service.CastleService;
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
    private final CastleService castleService;

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, BuildingLoader buildingLoader, CastleService castleService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.buildingLoader = buildingLoader;
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
                            .totalXP(0)
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

            //load all buildings
            buildingLoader.loadAll();

            user.setCastle(castleService.createCastle());
        }
    }

}
