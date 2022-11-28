package com.beval.server.api.v1;

import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.dto.response.UserInfoDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(path = API_BASE)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/info")
    public ResponseEntity<ResponseDTO> userInfo(@AuthenticationPrincipal UserPrincipal principal) {
        UserInfoDTO userInfoDTO = userService.getUserInfo(principal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("User data fetched successfully")
                                .content(userInfoDTO)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }
}
