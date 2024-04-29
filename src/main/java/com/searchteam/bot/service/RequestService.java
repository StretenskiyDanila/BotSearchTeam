package com.searchteam.bot.service;

import com.searchteam.bot.entity.Request;

import java.util.List;

public interface RequestService {

    Request sendRequest(Request request);
    List<Request> getAllRequestsUser(Long userId);
    List<Request> getAllRequestsTeam(Long teamId);
    void deleteRequest(Request request);

}
