package com.userApp.backend.repositories;

import com.userApp.backend.entitites.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    public List<MessageEntity> findAllByAppUserId(Long userId);
}
