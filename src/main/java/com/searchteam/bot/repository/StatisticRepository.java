package com.searchteam.bot.repository;

import com.searchteam.bot.entity.Statistic;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    Statistic findByTeamTitle(String teamTitle);
    List<Statistic> findFirst5ByOrderByIdAsc();
    boolean existsByTeamTitle(String teamTitle);

}
