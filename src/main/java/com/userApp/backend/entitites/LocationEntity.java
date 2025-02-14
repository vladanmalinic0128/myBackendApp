package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "location", schema = "ipdatabase", catalog = "")
public class LocationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "link")
    private String link;

    @OneToMany(mappedBy = "location")
    private List<FitnessProgramEntity> fitnessPrograms;

}
