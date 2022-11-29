package com.beval.server.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuildingEntityDTO {
    private Long id;
    private String buildingName;
    private String buildingImage;
    private int level;
    private int upgradeWoodRequired;
    private int upgradeStoneRequired;
    private int upgradeBuildingTimeSeconds;
    private int upgradeUnlocksOnLevel;
}
