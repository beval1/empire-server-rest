package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="castles")
public class CastleEntity extends BaseEntity{
    @NotBlank
    private String castleName;
    @ManyToMany
    private List<CastleBuilding> buildings;
    private int coordinateX;
    private int coordinateY;
}
