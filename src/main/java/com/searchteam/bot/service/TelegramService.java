package com.searchteam.bot.service;

import com.searchteam.bot.entity.User;

public interface TelegramService {

    void sendMessage(User user, String message);

}
