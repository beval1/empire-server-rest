package com.beval.server.service;

import com.beval.server.dto.payload.BuyArmyUnitsDTO;
import com.beval.server.dto.response.ArmyUnitDTO;
import com.beval.server.dto.response.CastleArmyDTO;
import com.beval.server.security.UserPrincipal;

import java.util.List;

public interface ArmyService {
    List<ArmyUnitDTO> getAllUnitsForUserBarracksLevel(UserPrincipal userPrincipal);
    List<CastleArmyDTO> getAllUnitsForCastle(UserPrincipal userPrincipal);
    void buyUnits(UserPrincipal userPrincipal, BuyArmyUnitsDTO buyArmyUnitsDTO);
}
