package com.searchteam.bot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "t_team")
@Getter
@Setter

public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @OneToOne
    @JoinColumn(name = "team_lead_id", referencedColumnName = "id")
    private User teamLead;

    private String description;

    private boolean isOpen;

    @OneToMany
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private List<Request> requests;

}
