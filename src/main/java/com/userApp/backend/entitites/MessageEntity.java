package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "message", schema = "ipdatabase", catalog = "")
public class MessageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Basic
    @Column(name = "app_user_id", insertable = false, updatable = false)
    private Long appUserId;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "time")
    private Timestamp time;

    @Basic
    @Column(name = "seen")
    private Boolean seen;

    @Basic
    @Column(name = "answered")
    private Boolean answered;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id", nullable = false)
    private AppUserEntity appUser;
}
