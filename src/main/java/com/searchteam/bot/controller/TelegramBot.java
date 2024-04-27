package com.searchteam.bot.controller;

import com.searchteam.bot.configuration.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("hello");
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
