package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "castle_building")
public class CastleBuilding extends BaseEntity {
    @OneToOne
    private BuildingEntity buildingEntity;
    private int coordinateX;
    private int coordinateY;
}
