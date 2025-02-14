package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "specific_attribute", schema = "ipdatabase", catalog = "")
public class SpecificAttributeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryId;

    @Basic
    @Column(name = "isActive")
    private Boolean isActive;

    @OneToMany(mappedBy = "specificAttribute")
    private List<FitnessProgramSpecificAttributeEntity> fitnessProgramSpecificAttributes;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CategoryEntity category;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

}
