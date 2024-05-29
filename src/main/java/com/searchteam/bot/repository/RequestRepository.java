package com.searchteam.bot.repository;

import com.searchteam.bot.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByUserQuestionnaireId(Long userQuestionnaireId);

    List<Request> findByTeamIdAndUserQuestionnaire_IsOpen(Long teamId, boolean open);
}
