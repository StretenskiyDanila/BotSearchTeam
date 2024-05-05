package com.searchteam.bot.service.impl;

import com.searchteam.bot.entity.User;
import com.searchteam.bot.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private final TelegramLongPollingBot telegramBot;

    @SneakyThrows
    @Override
    public void sendMessage(User user, String message) {
        telegramBot.executeAsync(new SendMessage(user.getTelegramChatId().toString(), message));
    }
}
