package com.userApp.backend.repositories;

import com.userApp.backend.entitites.PaymentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity, Long> {
    Optional<PaymentTypeEntity> findById(Long id);
}
