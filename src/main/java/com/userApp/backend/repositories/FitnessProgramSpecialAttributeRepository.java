package com.userApp.backend.repositories;

import com.userApp.backend.entitites.FitnessProgramSpecificAttributeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FitnessProgramSpecialAttributeRepository extends JpaRepository<FitnessProgramSpecificAttributeEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM FitnessProgramSpecificAttributeEntity f WHERE f.fitnessProgramId = :fitnessProgramId")
    void deleteAllByFitnessProgramId(@Param("fitnessProgramId") Long fitnessProgramId);
}
