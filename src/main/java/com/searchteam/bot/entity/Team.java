package com.searchteam.bot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "t_team")
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Integer projectId;

    @OneToOne
    @JoinColumn(name = "team_lead_id", referencedColumnName = "id")
    private User teamLead;

    private String description;

    private boolean isOpen;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Request> requests;

    @Override
    public String toString() {
        return title + "\n" + description;
    }
}
