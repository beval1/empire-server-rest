package com.beval.server.dto.payload;

import com.beval.server.dto.response.CastleArmyDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaveDTO {
    List<CastleArmyDTO> leftFlankArmy;
    List<CastleArmyDTO> rightFlankArmy;
    List<CastleArmyDTO> frontArmy;
}
