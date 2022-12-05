package com.beval.server.dto.payload;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttackDTO {
    List<WaveDTO> waves;
}
