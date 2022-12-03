package com.beval.server.api.v1;

import com.beval.server.dto.response.CastleDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.CastleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(API_BASE)
public class CastleController {
    private final CastleService castleService;

    public CastleController(CastleService castleService) {
        this.castleService = castleService;
    }

    @GetMapping("/castle/load")
    public ResponseEntity<ResponseDTO> getCastle(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @RequestParam(required = false, name = "username") String castleOwnerUsername) {
        CastleDTO castleDTO = castleService.getCastle(userPrincipal, castleOwnerUsername);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("User castle fetched successfully")
                                .content(castleDTO)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }

}
