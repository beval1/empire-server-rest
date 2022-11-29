package com.beval.server.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CastleDTO {
    private String castleName;
    private List<CastleBuildingDTO> buildings;
}
