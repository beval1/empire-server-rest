package com.beval.server.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnemyCastleDTO {
    private List<CastleBuildingDTO> buildings;
}
