package com.beval.server.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {
    private String username;
    private int totalXP;
    private int level;
    private int mightyPoints;
    private int coins;
    private int rubies;
}
