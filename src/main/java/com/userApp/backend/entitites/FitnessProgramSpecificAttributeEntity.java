package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "fitness_program_specific_attribute", schema = "ipdatabase", catalog = "")
public class FitnessProgramSpecificAttributeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "specific_attribute_id")
    private Long specificAttributeId;

    @Column(name = "fitness_program_id", insertable = false, updatable = false)
    private Long fitnessProgramId;

    @Basic
    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "specific_attribute_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SpecificAttributeEntity specificAttribute;

    @ManyToOne
    @JoinColumn(name = "fitness_program_id", referencedColumnName = "id", nullable = false)
    private FitnessProgramEntity fitnessProgram;
}
