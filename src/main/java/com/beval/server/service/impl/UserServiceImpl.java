package com.beval.server.service.impl;

import com.beval.server.dto.payload.SendPlayerMessageDTO;
import com.beval.server.dto.response.PlayerMessageDTO;
import com.beval.server.dto.response.UserInfoDTO;
import com.beval.server.exception.ApiException;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.model.entity.PlayerMessageEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.PlayerMessageRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PlayerMessageRepository playerMessageRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PlayerMessageRepository playerMessageRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.playerMessageRepository = playerMessageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserInfoDTO getUserInfo(UserPrincipal principal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(principal.getUsername(),
                principal.getUsername()).orElseThrow(NotAuthorizedException::new);
        int level = UserService.calculateUserLevel(userEntity);
        return UserInfoDTO.builder()
                .username(userEntity.getUsername())
                .coins(userEntity.getCoins())
                .totalXP(userEntity.getTotalXP())
                .level(level)
                .mightyPoints(userEntity.getMightyPoints())
                .rubies(userEntity.getRubies())
                .build();
    }

    @Transactional
    @Override
    public List<PlayerMessageDTO> getPlayerMessages(UserPrincipal principal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(principal.getUsername(),
                principal.getUsername()).orElseThrow(NotAuthorizedException::new);
        List<PlayerMessageEntity> playerMessageEntities = playerMessageRepository.findAllByReceiver(userEntity);
        List<PlayerMessageDTO> playerMessageDTOS = new ArrayList<>();
        for (PlayerMessageEntity playerMessageEntity : playerMessageEntities) {
            PlayerMessageDTO playerMessageDTO = modelMapper.map(playerMessageEntity, PlayerMessageDTO.class);
            playerMessageDTO.setSenderUsername(playerMessageEntity.getSender().getUsername());
            playerMessageDTOS.add(playerMessageDTO);
        }
        return playerMessageDTOS;
    }

    @Transactional
    @Override
    public void sendMessage(UserPrincipal principal, String playerUsername, SendPlayerMessageDTO sendPlayerMessageDTO) {
        UserEntity sender = userRepository.findByUsernameOrEmail(principal.getUsername(),
                principal.getUsername()).orElseThrow(NotAuthorizedException::new);
        UserEntity receiver = userRepository.findByUsernameOrEmail(playerUsername,
                playerUsername).orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Receiver not found!"));

        playerMessageRepository.save(PlayerMessageEntity
                .builder()
                .content(sendPlayerMessageDTO.getContent())
                .title(sendPlayerMessageDTO.getTitle())
                .sender(sender)
                .receiver(receiver)
                .build());
    }

}
