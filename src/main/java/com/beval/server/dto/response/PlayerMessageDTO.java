package com.beval.server.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerMessageDTO {
    private String title;
    private String content;
    private String senderUsername;
}
