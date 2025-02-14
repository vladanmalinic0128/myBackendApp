package com.userApp.backend.repositories;

import com.userApp.backend.entitites.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    @Modifying
    @Query("DELETE FROM ImageEntity i WHERE i.fitnessProgramId = :fitnessProgramId")
    void deleteAllByFitnessProgramId(@Param("fitnessProgramId") Long fitnessProgramId);
}
