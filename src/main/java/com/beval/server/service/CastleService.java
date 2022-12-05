package com.beval.server.service;

import com.beval.server.dto.response.CastleDTO;
import com.beval.server.dto.response.EnemyCastleDTO;
import com.beval.server.dto.response.MapCastleDTO;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.security.UserPrincipal;

import java.util.List;

public interface CastleService {
    CastleDTO getCastle(UserPrincipal userPrincipal);
    void createCastleForUser(UserEntity userEntity);
    List<MapCastleDTO> getAllCastlesForQuadrant(UserPrincipal userPrincipal, int quadrant);
    EnemyCastleDTO getEnemyCastle(String enemyUsername);
}
