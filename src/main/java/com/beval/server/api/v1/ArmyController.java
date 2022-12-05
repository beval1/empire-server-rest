package com.beval.server.api.v1;

import com.beval.server.dto.payload.AttackDTO;
import com.beval.server.dto.payload.BuyArmyUnitsDTO;
import com.beval.server.dto.response.ArmyUnitDTO;
import com.beval.server.dto.response.CastleArmyDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.ArmyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        List<ArmyUnitDTO> armyUnitDTOS = armyService.getAllUnitsForUserBarracksLevel(principal);

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

    @GetMapping("/army/get-castle")
    public ResponseEntity<ResponseDTO> getAllUnitsForCastle(
            @AuthenticationPrincipal UserPrincipal principal) {
        List<CastleArmyDTO> armyUnitDTOS = armyService.getAllUnitsForCastle(principal);

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


    @PostMapping("/army/buy")
    public ResponseEntity<ResponseDTO> buyArmy(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @Valid @RequestBody BuyArmyUnitsDTO buyArmyUnitsDTO) {
        armyService.buyUnits(userPrincipal, buyArmyUnitsDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Units bought successfully!")
                                .content(null)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }

    @PostMapping("/attack/{victimUsername}")
    public ResponseEntity<ResponseDTO> launchAttack(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                    @PathVariable String victimUsername,
                                                    @Valid @RequestBody AttackDTO attackDTO) {
        armyService.launchAttack(userPrincipal, victimUsername, attackDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Attack launched successfully!")
                                .content(null)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }
}
