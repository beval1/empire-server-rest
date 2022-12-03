package com.beval.server.dto.response;

import com.beval.server.model.entity.ArmyUnitEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CastleArmyDTO {
    private ArmyUnitEntity armyUnit;
    private int armyUnitCount;
}
