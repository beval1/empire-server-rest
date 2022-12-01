package com.beval.server.dto.payload;

import lombok.*;

import javax.validation.constraints.Positive;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpgradeBuildingDTO {
    @Positive
    private int buildingId;
}
