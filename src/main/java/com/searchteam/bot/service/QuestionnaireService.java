package com.searchteam.bot.service;

import java.util.Optional;

import com.searchteam.bot.entity.UserQuestionnaire;

public interface QuestionnaireService {

    Long save(UserQuestionnaire questionnaire);

    UserQuestionnaire update(UserQuestionnaire questionnaire);

    Optional<UserQuestionnaire> findById(Long id);

    Optional<UserQuestionnaire> findByUserId(Long userId);
}
