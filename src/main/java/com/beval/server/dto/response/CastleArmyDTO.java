package com.beval.server.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CastleArmyDTO {
    private ArmyUnitDTO armyUnit;
    private int armyUnitCount;
}
