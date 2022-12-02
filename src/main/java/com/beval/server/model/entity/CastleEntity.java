package com.beval.server.model.entity;

import com.beval.server.dto.response.ArmyUnitDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="castles")
public class CastleEntity extends BaseEntity {
    @NotBlank
    private String castleName;
    @ManyToMany
    private List<CastleBuilding> buildings;
    private int wood;
    private int stone;
    private int food;
    private int citizens;
    private int coordinateX;
    private int coordinateY;
    @OneToMany
    private List<CastleArmy> army;
}
