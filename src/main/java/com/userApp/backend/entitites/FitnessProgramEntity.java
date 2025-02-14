package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "fitness_program", schema = "ipdatabase", catalog = "")
public class FitnessProgramEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "price")
    private Double price;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "duration")
    private Time duration;

    @Basic
    @Column(name = "weight_level")
    private Integer weightLevel;

    @Basic
    @Column(name = "isActive")
    private Boolean isActive;

    @Basic
    @Column(name = "isFinished")
    private Boolean isFinished;

    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt;

    @Basic
    @Column(name = "link")
    private String link;

    @Basic
    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryId;

    @Basic
    @Column(name = "location_id", insertable = false, updatable = false)
    private Long locationId;

    @Basic
    @Column(name = "creator_id", insertable = false, updatable = false)
    private Long creatorId;

    @OneToMany(mappedBy = "fitnessProgram")
    private List<CommentEntity> comments;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    private LocationEntity location;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id", nullable = false)
    private UserEntity creator;

    @OneToMany(mappedBy = "fitnessProgram")
    private List<FitnessProgramSpecificAttributeEntity> fitnessProgramSpecificAttributes;

    @OneToMany(mappedBy = "fitnessProgram")
    private List<ImageEntity> images;

    @OneToMany(mappedBy = "fitnessProgram")
    private List<ParticipationEntity> participations;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }
}
