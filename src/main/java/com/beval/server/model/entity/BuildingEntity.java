package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

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
    @NotEmpty
    private String buildingImage;
    @Positive
    private int level;
    @Min(0)
    private int woodRequired;
    @Min(0)
    private int stoneRequired;
    @Min(0)
    private int buildingTimeSeconds;
    @Positive
    @Builder.Default
    private int unlocksOnLevel = 1;
    //not all building have production
    private int production;
    @Min(0)
    private int buildingXP;
}
