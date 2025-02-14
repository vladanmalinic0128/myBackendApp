package com.userApp.backend.repositories;

import com.userApp.backend.entitites.SpecificAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecificAttributeRepository extends JpaRepository<SpecificAttributeEntity, Long> {
    List<SpecificAttributeEntity> findAllByCategory_Id(Long id);
    Optional<SpecificAttributeEntity> findById(Long id);
}
