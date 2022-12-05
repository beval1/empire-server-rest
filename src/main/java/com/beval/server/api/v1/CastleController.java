package com.beval.server.api.v1;

import com.beval.server.dto.response.CastleDTO;
import com.beval.server.dto.response.EnemyCastleDTO;
import com.beval.server.dto.response.MapCastleDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.CastleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(API_BASE)
public class CastleController {
    private final CastleService castleService;

    public CastleController(CastleService castleService) {
        this.castleService = castleService;
    }

    @GetMapping("/castle/load")
    public ResponseEntity<ResponseDTO> getCastle(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CastleDTO castleDTO = castleService.getCastle(userPrincipal);

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

    @GetMapping("/castle/enemy/{enemyUsername}")
    public ResponseEntity<ResponseDTO> getCastle(@PathVariable String enemyUsername) {
        EnemyCastleDTO enemyCastle = castleService.getEnemyCastle(enemyUsername);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Enemy castle fetched successfully")
                                .content(enemyCastle)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }

    @GetMapping("/map")
    public ResponseEntity<ResponseDTO> getCastle(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @RequestParam(name = "quadrant") int quadrant) {
        List<MapCastleDTO> mapCastleDTOS = castleService.getAllCastlesForQuadrant(userPrincipal, quadrant);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Castles fetched successfully")
                                .content(mapCastleDTOS)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }

}
