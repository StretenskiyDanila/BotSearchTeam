package com.searchteam.bot.service.impl;

import com.searchteam.bot.entity.Request;
import com.searchteam.bot.repository.RequestRepository;
import com.searchteam.bot.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Override
    public Request sendRequest(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public List<Request> getAllRequestsUser(Long userQuestionnaireId) {
        return requestRepository.findByUserQuestionnaireId(userQuestionnaireId);
    }

    @Override
    public List<Request> getAllRequestsTeam(Long teamId) {
        return requestRepository.findByTeamId(teamId);
    }

    @Override
    public void deleteRequest(Request request) {
        requestRepository.delete(request);
    }

}
