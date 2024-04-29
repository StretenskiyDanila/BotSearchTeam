package com.searchteam.bot.repository;

import com.searchteam.bot.entity.Request;
import com.searchteam.bot.entity.UserQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByUserQuestionnaireId(Long userQuestionnaireId);
    List<Request> findByTeamId(Long teamId);

}
