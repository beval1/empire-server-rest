package com.beval.server.service;

import com.beval.server.dto.payload.SignInDTO;
import com.beval.server.dto.payload.SignupDTO;

public interface AuthService {
    String signInUser(SignInDTO signinDto);
    void signUpUser(SignupDTO signupDto);
}
