package com.beval.server.service;

import com.beval.server.dto.response.CastleDTO;
import com.beval.server.security.UserPrincipal;

public interface CastleService {
    CastleDTO getCastle(UserPrincipal userPrincipal, String castleOwnerUsername);
}
