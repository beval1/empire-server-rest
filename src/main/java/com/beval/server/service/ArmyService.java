package com.beval.server.service;

import com.beval.server.dto.response.ArmyUnitDTO;
import com.beval.server.security.UserPrincipal;

import java.util.List;

public interface ArmyService {
    List<ArmyUnitDTO> getAllForUserBarracksLevel(UserPrincipal userPrincipal);
}
