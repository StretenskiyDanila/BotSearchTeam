package com.searchteam.bot.service.impl;

import com.searchteam.bot.entity.Request;
import com.searchteam.bot.entity.Team;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.repository.TeamRepository;
import com.searchteam.bot.service.RequestService;
import com.searchteam.bot.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final RequestService requestService;

    @Override
    public Long save(Team team) {
        return teamRepository.save(team).getId();
    }

    @Override
    public Team update(Team team) {
        return teamRepository.saveAndFlush(team);
    }

    @Override
    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteTeam(Long id) {
        List<Request> requests = requestService.getAllRequestsTeam(id);
        requests.forEach(requestService::rejectRequest);
        teamRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void closeTeam(Team team) {
        team.setOpen(false);
        teamRepository.save(team);
    }

    @Override
    public Optional<Team> findTeamByUser(User user) {
        return teamRepository.findTeamByTeamLead_Id(user.getId());
    }

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll();
    }
}
