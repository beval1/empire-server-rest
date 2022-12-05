package com.beval.server.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapCastleDTO {
    private String castleName;
    private String ownerUsername;
    private int ownerLevel;
    private int ownerMightyPoints;
    private int coordinateX;
    private int coordinateY;
    private int quadrant;
    private String castleImage;
}
