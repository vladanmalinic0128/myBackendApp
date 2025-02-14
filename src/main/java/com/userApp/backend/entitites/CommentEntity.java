package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "comment", schema = "ipdatabase", catalog = "")
public class CommentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "fitness_program_id", insertable = false, updatable = false)
    private Long fitnessProgramId;

    @Basic
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "time")
    private Timestamp time;

    @ManyToOne
    @JoinColumn(name = "fitness_program_id", referencedColumnName = "id", nullable = false)
    private FitnessProgramEntity fitnessProgram;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

}
