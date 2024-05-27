package com.searchteam.bot.service.impl;

import com.searchteam.bot.entity.Statistic;
import com.searchteam.bot.repository.StatisticRepository;
import com.searchteam.bot.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    @Override
    public void addTeamStatistic(String teamTitle) {
        Statistic statistic = new Statistic();
        statistic.setTeamTitle(teamTitle);
        statisticRepository.save(statistic);
    }

    @Override
    public void incrementFoundUsers(String teamTitle) {
        Statistic statistic = statisticRepository.findByTeamTitle(teamTitle);
        statistic.setCountFoundUsers(statistic.getCountFoundUsers() + 1);
        statisticRepository.save(statistic);
    }

}
