package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "participation", schema = "ipdatabase", catalog = "")
public class ParticipationEntity {
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
    @Column(name = "payment_type_id", insertable = false, updatable = false)
    private Long paymentTypeId;

    @Basic
    @Column(name = "isActive")
    private Boolean isActive;

    @Basic
    @Column(name = "card_number")
    private String cardNumber;

    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "fitness_program_id", referencedColumnName = "id", nullable = false)
    private FitnessProgramEntity fitnessProgram;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "payment_type_id", referencedColumnName = "id", nullable = false)
    private PaymentTypeEntity paymentType;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

}
