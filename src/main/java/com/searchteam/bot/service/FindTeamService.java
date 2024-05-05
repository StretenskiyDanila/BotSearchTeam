package com.searchteam.bot.service;

import com.searchteam.bot.entity.Team;

import java.util.List;

public interface FindTeamService {

    List<Team> findTeamsFromUserQuestionnaireId(Long userQuestionnaireId, int page);

}
