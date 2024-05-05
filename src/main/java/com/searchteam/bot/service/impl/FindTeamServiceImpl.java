package com.searchteam.bot.service.impl;

import com.searchteam.bot.entity.Team;
import com.searchteam.bot.repository.TeamRepository;
import com.searchteam.bot.service.FindTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindTeamServiceImpl implements FindTeamService {

    private static final int PAGE_SIZE = 5;

    private final TeamRepository teamRepository;

    @Override
    public List<Team> findTeamsFromUserQuestionnaireId(Long userQuestionnaireId, int page) {
        return teamRepository.findFreeTeamsForUserQuestionnaire(userQuestionnaireId, Pageable.ofSize(PAGE_SIZE)
                        .withPage(page))
                .getContent();
    }
}
