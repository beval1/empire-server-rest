package com.beval.server.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "messages")
public class PlayerMessageEntity extends BaseEntity{
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @OneToOne
    private UserEntity sender;
    @OneToOne
    private UserEntity receiver;
}
