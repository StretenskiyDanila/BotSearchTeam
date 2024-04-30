package com.searchteam.bot.service;

import com.searchteam.bot.entity.Team;

import java.util.Optional;

public interface TeamService {

    Long save(Team team);

    Team update(Team team);

    Optional<Team> findById(Long id);

    void deleteTeam(Long id);

    void closeTeam(Team team);
}
