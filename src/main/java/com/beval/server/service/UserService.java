package com.beval.server.service;

import com.beval.server.config.AppConstants;
import com.beval.server.dto.payload.SendPlayerMessageDTO;
import com.beval.server.dto.response.PlayerMessageDTO;
import com.beval.server.dto.response.UserInfoDTO;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.security.UserPrincipal;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserInfoDTO getUserInfo(UserPrincipal principal);
    static int calculateUserLevel(UserEntity user) {
        Map<Integer, Integer> levels = AppConstants.userLevelsXP;
        for (int i = levels.size(); i >= 1; i--) {
            if (levels.get(i) <= user.getTotalXP()){
                return i;
            }
        }
        return 1;
    }

    List<PlayerMessageDTO> getPlayerMessages(UserPrincipal principal);

    void sendMessage(UserPrincipal principal, String playerUsername, SendPlayerMessageDTO sendPlayerMessageDTO);
}
