package com.userApp.backend.repositories;

import com.userApp.backend.entitites.CityEntity;
import com.userApp.backend.entitites.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {
    Optional<CityEntity> findById(Long id);
}
