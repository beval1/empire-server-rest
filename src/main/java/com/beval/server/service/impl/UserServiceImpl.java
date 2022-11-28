package com.beval.server.service.impl;

import com.beval.server.dto.response.UserInfoDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.UserService;
import org.springframework.stereotype.Service;

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

        return UserInfoDTO.builder()
                .username(userEntity.getUsername())
                .coins(userEntity.getCoins())
                .level(userEntity.getLevel())
                .mightyPoints(userEntity.getMightyPoints())
                .rubies(userEntity.getRubies())
                .build();
    }
}
