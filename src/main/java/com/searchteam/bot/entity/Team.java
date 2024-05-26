package com.searchteam.bot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "t_team")
@Getter
@Setter
@ToString(exclude = {"requests", "users", "requests"})
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

    @OneToMany(mappedBy = "team")
    List<User> users;

    @OneToMany
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private List<Request> requests;

    @Override
    public String toString() {
        return title + "\n" + description;
    }
}
