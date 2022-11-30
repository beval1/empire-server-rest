package com.beval.server.service;

import com.beval.server.dto.payload.CreateCastleBuildingDTO;
import com.beval.server.dto.response.CastleDTO;
import com.beval.server.model.entity.CastleEntity;
import com.beval.server.security.UserPrincipal;

public interface CastleService {
    CastleDTO getCastle(UserPrincipal userPrincipal, String castleOwnerUsername);
    CastleEntity createCastle();
    void createCastleForUser(UserPrincipal userPrincipal);

    void createCastleBuilding(UserPrincipal userPrincipal, CreateCastleBuildingDTO createCastleBuildingDTO);
}
