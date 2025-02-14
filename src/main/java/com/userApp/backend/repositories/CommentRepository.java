package com.userApp.backend.repositories;

import com.userApp.backend.entitites.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByFitnessProgram_Id(Long id);
}
