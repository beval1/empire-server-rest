package com.beval.server.model.entity;

import com.beval.server.config.AppConstants;
import lombok.*;

import javax.persistence.Column;
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
    @Column(unique = true)
    private String buildingName;
    @Builder.Default
    private int castleLimit = AppConstants.PRODUCTION_BUILDINGS_CASTLE_LIMIT;
    @Builder.Default
    private boolean buildable = true;
    @Builder.Default
    private boolean destroyable = true;
    @Builder.Default
    private double widthSizingRatio = 1.0;
    @Builder.Default
    private double heightSizingRatio = 1.0;
}
