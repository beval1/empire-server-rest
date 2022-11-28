package com.beval.server.service;

import com.beval.server.dto.response.UserInfoDTO;
import com.beval.server.security.UserPrincipal;

public interface UserService {
    UserInfoDTO getUserInfo(UserPrincipal principal);
}
