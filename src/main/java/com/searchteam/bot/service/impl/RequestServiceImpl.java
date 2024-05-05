package com.searchteam.bot.service.impl;

import com.searchteam.bot.entity.Request;
import com.searchteam.bot.repository.RequestRepository;
import com.searchteam.bot.service.RequestService;
import com.searchteam.bot.service.TelegramService;
import com.searchteam.bot.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final TelegramService telegramService;

    @Override
    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public List<Request> getAllRequestsByUserQuestionnaireId(Long userQuestionnaireId) {
        return requestRepository.findByUserQuestionnaireId(userQuestionnaireId);
    }

    @Override
    public List<Request> getAllRequestsTeam(Long teamId) {
        return requestRepository.findByTeamIdAndUserQuestionnaire_IsOpen(teamId, true);
    }

    @Override
    public void deleteRequest(Request request) {
        requestRepository.delete(request);
    }

    @Override
    @Transactional
    public void rejectRequest(Request request) {
        telegramService.sendMessage(request.getUserQuestionnaire().getUser(),
                "К сожалению, ваша заявка в команду %s была отклонена."
                        .formatted(request.getTeam().getTitle()));
        requestRepository.delete(request);
    }

    @Override
    @Transactional
    public void acceptRequest(Request request) {
        telegramService.sendMessage(request.getUserQuestionnaire().getUser(),
                "Ваша заявка в команду %s была принята. Свяжитесь с тимлидом: %s"
                        .formatted(request.getTeam().getTitle(),
                                TextUtils.aliasUsername(request.getTeam().getTeamLead().getTelegramUsername())));
        requestRepository.delete(request);

    }

}
