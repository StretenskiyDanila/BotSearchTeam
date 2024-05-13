package com.searchteam.bot.repository;

import com.searchteam.bot.entity.UserQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserQuestionnaireRepository extends JpaRepository<UserQuestionnaire, Long> {
    Optional<UserQuestionnaire> findByUserId(Long userId);
}
