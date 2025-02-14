package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "advisor", schema = "ipdatabase", catalog = "")
public class AdvisorEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private AppUserEntity appUser;

}
