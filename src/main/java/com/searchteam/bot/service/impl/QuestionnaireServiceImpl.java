package com.searchteam.bot.service.impl;

import com.searchteam.bot.entity.UserQuestionnaire;
import com.searchteam.bot.repository.UserQuestionnaireRepository;
import com.searchteam.bot.service.QuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final UserQuestionnaireRepository userQuestionnaireRepository;

    @Override
    public Long save(UserQuestionnaire questionnaire) {
        return userQuestionnaireRepository.save(questionnaire).getId();
    }

    @Override
    public UserQuestionnaire update(UserQuestionnaire questionnaire) {
        return userQuestionnaireRepository.save(questionnaire);
    }

    @Override
    public Optional<UserQuestionnaire> findById(Long id) {
        return userQuestionnaireRepository.findById(id);
    }

    @Override
    public Optional<UserQuestionnaire> findByUserId(Long userId) {
        //throw new UnsupportedOperationException("Unimplemented method 'findByUserId'");
        return userQuestionnaireRepository.findByUserId(userId);
    }
}
