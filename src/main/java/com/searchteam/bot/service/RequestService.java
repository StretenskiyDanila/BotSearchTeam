package com.searchteam.bot.service;

import com.searchteam.bot.entity.Request;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    Request saveRequest(Request request);
    Optional<Request> getAllRequestsByUserQuestionnaireId(Long userId);
    List<Request> getAllRequestsTeam(Long teamId);
    void deleteRequest(Request request);

    void rejectRequest(Request request);
    void acceptRequest(Request request);

    Optional<Request> findByUserQuestionnaireId(Long requestId);
}
