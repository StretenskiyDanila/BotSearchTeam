package com.searchteam.bot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_statistic")
@Getter
@Setter
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "count_found_users")
    private Integer countFoundUsers = 0;

    @Column(name = "team_title")
    private String teamTitle;

}
