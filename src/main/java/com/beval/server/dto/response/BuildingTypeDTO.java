package com.beval.server.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuildingTypeDTO {
    private int id;
    private String buildingName;
    private int castleLimit;
    private boolean buildable;
    private double widthSizingRatio;
    private double heightSizingRatio;
    private boolean destroyable;
}
