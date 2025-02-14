package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "user", schema = "ipdatabase", catalog = "")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "city_id", insertable = false, updatable = false)
    private Long cityId;

    @OneToMany(mappedBy = "user")
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "user")
    private List<DiaryEntity> diaries;

    @OneToMany(mappedBy = "creator")
    private List<FitnessProgramEntity> fitnessPrograms;

    @OneToMany(mappedBy = "user")
    private List<MessageEntity> messages;

    @OneToMany(mappedBy = "user")
    private List<ParticipationEntity> participations;

    @OneToMany(mappedBy = "user")
    private List<SubscriptionEntity> subscriptions;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    private AppUserEntity appUser;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private CityEntity city;

}
