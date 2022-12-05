package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="castles")
public class CastleEntity extends BaseEntity {
    @OneToOne
    private UserEntity owner;
    @NotBlank
    private String castleName;
    @ManyToMany
    private List<CastleBuilding> buildings;
    private double wood;
    private double stone;
    private double food;
    private double woodProduction;
    private double stoneProduction;
    private double foodProduction;
    private int citizens;
    private int coordinateX;
    private int coordinateY;
    @Positive
    private int quadrant;
    @OneToMany
    private List<CastleArmy> army;
}
