package com.searchteam.bot.service;

import com.searchteam.bot.entity.Team;
import com.searchteam.bot.entity.User;

import java.util.List;
import java.util.Optional;

public interface TeamService {

    Long save(Team team);

    Team update(Team team);

    Optional<Team> findById(Long id);

    void deleteTeam(Long id);

    void closeTeam(Team team);

    Optional<Team> findTeamByUser(User user);

    List<Team> findAll();
}
