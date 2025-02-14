package com.userApp.backend.repositories;

import com.userApp.backend.entitites.AdvisorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvisorEntityRepository extends JpaRepository<AdvisorEntity, Long> {
}
