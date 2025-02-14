package com.userApp.backend.repositories;

import com.userApp.backend.entitites.FitnessProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FitnessProgramRepository extends JpaRepository<FitnessProgramEntity, Long> {
    List<FitnessProgramEntity> findAllByCategoryId(Long categoryId);
}
