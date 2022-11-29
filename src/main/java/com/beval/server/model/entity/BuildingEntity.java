package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="building_entity")
public class BuildingEntity extends BaseEntity{
    @ManyToOne
    private BuildingType buildingType;
    private String buildingImage;
    private int level;
    private int woodRequired;
    private int stoneRequired;
    private int buildingTimeSeconds;
    private int unlocksOnLevel;
}
