package com.userApp.backend.repositories;

import com.userApp.backend.entitites.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findAllByUserId(Long userId);
    List<SubscriptionEntity> findAllByCategoryId(Long categoryId);
}
