package com.beval.server.service.impl;

import com.beval.server.config.AppConstants;
import com.beval.server.dto.response.UserInfoDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserInfoDTO getUserInfo(UserPrincipal principal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(principal.getUsername(),
                principal.getUsername()).orElseThrow(NotAuthorizedException::new);
        int level = calculateUserLevel(userEntity);
        return UserInfoDTO.builder()
                .username(userEntity.getUsername())
                .coins(userEntity.getCoins())
                .totalXP(userEntity.getTotalXP())
                .level(level)
                .mightyPoints(userEntity.getMightyPoints())
                .rubies(userEntity.getRubies())
                .build();
    }

    public static int calculateUserLevel(UserEntity user){
        Map<Integer, Integer> levels = AppConstants.userLevelsXP;
        for (Map.Entry<Integer, Integer> entry : levels.entrySet()  ){
            if (entry.getValue() <= user.getTotalXP()){
                return entry.getKey();
            }
        }
        return 1;
    }
}
