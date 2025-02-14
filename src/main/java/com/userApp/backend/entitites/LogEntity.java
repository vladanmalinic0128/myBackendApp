package com.userApp.backend.entitites;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "log", schema = "ipdatabase", catalog = "")
public class LogEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "time")
    private Timestamp time;

}
