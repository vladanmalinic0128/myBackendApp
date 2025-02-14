package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "diary", schema = "ipdatabase", catalog = "")
public class DiaryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Basic
    @Column(name = "current_weight")
    private Double currentWeight;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "trainingDuration")
    private Integer trainingDuration;

    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

}
