package com.userApp.backend.repositories;

import com.userApp.backend.entitites.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByAppUser_Username(String username);
    Optional<UserEntity> findByAppUser_Username(String username);
}
