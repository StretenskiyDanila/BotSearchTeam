package com.searchteam.bot.entity;

import com.searchteam.bot.pipeline.PipelineEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "t_user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long telegramChatId;

    private String telegramUsername;

    private Timestamp createdAt = Timestamp.from(Instant.now());

    @Enumerated(EnumType.STRING)
    private PipelineEnum pipelineStatus = PipelineEnum.NONE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private Integer currentProjectChoice;

    private Long currentTeamChoice;

    private Long currentRequestChoice;

    @Transient
    private Integer currentPage;
}
