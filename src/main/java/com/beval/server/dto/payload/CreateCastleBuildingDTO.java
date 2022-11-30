package com.beval.server.dto.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class CreateCastleBuildingDTO {
    @Positive
    private int row;
    @Positive
    private int column;
    @Positive
    private Long typeId;
}
