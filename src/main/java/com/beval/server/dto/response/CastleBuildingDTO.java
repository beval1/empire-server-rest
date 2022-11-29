package com.beval.server.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CastleBuildingDTO {
    private BuildingEntityDTO buildingEntity;
    private int coordinateX;
    private int coordinateY;
}
