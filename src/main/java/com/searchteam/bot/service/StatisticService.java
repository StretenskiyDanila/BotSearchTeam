package com.searchteam.bot.service;

import com.searchteam.bot.entity.Statistic;

import java.util.List;

public interface StatisticService {

    void addTeamStatistic(String teamTitle);
    void incrementFoundUsers(String teamTitle);
    List<Statistic> findAllStatisticsLimited();
    List<Statistic> findAllStatistics();
    String createExcelStatistic();

}
