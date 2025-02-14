package com.userApp.backend.repositories;

import com.userApp.backend.entitites.AdministratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorEntityRepository extends JpaRepository<AdministratorEntity, Long> {
}
