package com.beval.server.dto.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendPlayerMessageDTO {
    private String title;
    private String content;
}
