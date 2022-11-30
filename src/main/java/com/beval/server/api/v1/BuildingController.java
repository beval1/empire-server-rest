package com.beval.server.api.v1;

import com.beval.server.dto.response.BuildingEntityDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.service.BuildingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
