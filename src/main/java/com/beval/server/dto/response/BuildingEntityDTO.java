package com.beval.server.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuildingEntityDTO {
    private BuildingTypeDTO buildingType;
    private String buildingImage;
    private int level;
    private int woodRequired;
    private int stoneRequired;
    private int buildingTimeSeconds;
    private int unlocksOnLevel;
    private int production;
    private int buildingXP;
}
