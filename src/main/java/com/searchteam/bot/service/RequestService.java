package com.searchteam.bot.service;

import com.searchteam.bot.entity.Request;

import java.util.List;

public interface RequestService {

    Request saveRequest(Request request);
    List<Request> getAllRequestsByUserQuestionnaireId(Long userId);
    List<Request> getAllRequestsTeam(Long teamId);
    void deleteRequest(Request request);

    void rejectRequest(Request request);
    void acceptRequest(Request request);
}
