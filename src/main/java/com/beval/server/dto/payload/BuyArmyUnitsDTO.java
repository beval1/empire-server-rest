package com.beval.server.dto.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuyArmyUnitsDTO {
    private Long armyUnitId;
    private int count;
}
