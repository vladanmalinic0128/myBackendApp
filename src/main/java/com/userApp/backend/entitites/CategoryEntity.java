package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "category", schema = "ipdatabase", catalog = "")
public class CategoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "isActive")
    private Boolean isActive;

    @OneToMany(mappedBy = "category")
    private List<FitnessProgramEntity> fitnessPrograms;

    @OneToMany(mappedBy = "category")
    private List<SpecificAttributeEntity> specificAttributes;

    @OneToMany(mappedBy = "category")
    private List<SubscriptionEntity> subscriptions;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

}
