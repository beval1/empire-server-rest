package com.beval.server.api.v1;

import com.beval.server.dto.payload.CreateCastleBuildingDTO;
import com.beval.server.dto.payload.DestroyBuildingDTO;
import com.beval.server.dto.response.BuildingEntityDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.BuildingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(API_BASE)
public class BuildingController {
    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/buildings/get-all")
    public ResponseEntity<ResponseDTO> getAllBuildings(@RequestParam(defaultValue = "1", required = false) int level) {
        List<BuildingEntityDTO> buildingEntityDTOS = buildingService.getAll(level);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Buildings fetched successfully")
                                .content(buildingEntityDTOS)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }

    @GetMapping("/buildings/get/{buildingType}")
    public ResponseEntity<ResponseDTO> getSpecificBuilding(@PathVariable Long buildingType) {
        List<BuildingEntityDTO> buildingEntityDTOS = buildingService.getSpecificBuilding(buildingType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Buildings fetched successfully")
                                .content(buildingEntityDTOS)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }

    @PostMapping("/buildings/create")
    public ResponseEntity<ResponseDTO> createCastleBuilding(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                            @Valid @RequestBody CreateCastleBuildingDTO createCastleBuildingDTO
    ) {
        buildingService.createBuilding(userPrincipal, createCastleBuildingDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Building created successfully!")
                                .content(null)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }

    @PostMapping("/buildings/upgrade/{buildingId}")
    public ResponseEntity<ResponseDTO> upgradeBuilding(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                       @PathVariable Long buildingId) {
        buildingService.upgradeBuilding(userPrincipal, buildingId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Building upgraded successfully!")
                                .content(null)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }

    @PostMapping("/buildings/destroy")
    public ResponseEntity<ResponseDTO> upgradeBuilding(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                       @Valid @RequestBody DestroyBuildingDTO destroyBuildingDTO) {
        buildingService.destroyBuilding(userPrincipal, destroyBuildingDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Building destroyed successfully!")
                                .content(null)
                                .status(HttpStatus.OK.value())
                                .build()
                );
    }
}
