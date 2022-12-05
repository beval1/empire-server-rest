package com.beval.server.dto.payload;

import com.beval.server.dto.response.CastleArmyDTO;
import com.beval.server.model.entity.CastleArmy;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttackDTO {
    List<CastleArmyDTO> armyList;
}
