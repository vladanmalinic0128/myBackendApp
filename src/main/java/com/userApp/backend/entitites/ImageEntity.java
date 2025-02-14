package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "image", schema = "ipdatabase", catalog = "")
public class ImageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "image", length = 100_000)
    private String image;

    @Basic
    @Column(name = "fitness_program_id", insertable = false, updatable = false)
    private Long fitnessProgramId;

    @ManyToOne
    @JoinColumn(name = "fitness_program_id", referencedColumnName = "id", nullable = false)
    private FitnessProgramEntity fitnessProgram;

}
