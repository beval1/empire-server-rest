package com.beval.server.api.v1;

import com.beval.server.dto.response.ArmyUnitDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.ArmyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(path = API_BASE)
public class ArmyController {
    private final ArmyService armyService;

    public ArmyController(ArmyService armyService) {
        this.armyService = armyService;
    }

    @GetMapping("/army/get-all")
    public ResponseEntity<ResponseDTO> getAllBuildings(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<ArmyUnitDTO> armyUnitDTOS = armyService.getAllForUserBarracksLevel(principal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Army fetched successfully")
                                .content(armyUnitDTOS)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }
}
