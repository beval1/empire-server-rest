package com.beval.server.api.v1;

import com.beval.server.dto.payload.SendPlayerMessageDTO;
import com.beval.server.dto.response.PlayerMessageDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.dto.response.UserInfoDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/user/messages")
    public ResponseEntity<ResponseDTO> getPlayerMessages(@AuthenticationPrincipal UserPrincipal principal) {
        List<PlayerMessageDTO> playerMessageDTOS = userService.getPlayerMessages(principal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("User messages fetched successfully")
                                .content(playerMessageDTOS)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }

    @PostMapping("/message/send/{playerUsername}")
    public ResponseEntity<ResponseDTO> sendMessage(@AuthenticationPrincipal UserPrincipal principal,
                                                   @PathVariable String playerUsername,
                                                   @Valid @RequestBody SendPlayerMessageDTO sendPlayerMessageDTO
    ) {
        userService.sendMessage(principal, playerUsername, sendPlayerMessageDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Message sent successfully")
                                .content(null)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }
}
