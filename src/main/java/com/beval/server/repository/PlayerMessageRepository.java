package com.beval.server.repository;

import com.beval.server.model.entity.PlayerMessageEntity;
import com.beval.server.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMessageRepository extends JpaRepository<PlayerMessageEntity, Long> {
    List<PlayerMessageEntity> findAllByReceiver(UserEntity user);
}
