package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "payment_type", schema = "ipdatabase", catalog = "")
public class PaymentTypeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "paymentType")
    private List<ParticipationEntity> participations;

}
