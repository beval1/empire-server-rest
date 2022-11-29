package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="building_type")
public class BuildingType extends BaseEntity {
    private String buildingName;
}
