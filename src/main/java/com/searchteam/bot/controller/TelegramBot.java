package com.searchteam.bot.controller;

import com.searchteam.bot.configuration.BotConfig;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final TelegramBotsApi telegramBotsApi;

    private final UserRepository userRepository;

    @SneakyThrows
    @PostConstruct
    public void init() {
        telegramBotsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        String userName = update.getMessage().getFrom().getUserName();
        Long chatId = update.getMessage().getChatId();
        //TODO: move this to service
        User user1 = userRepository.findFirstByTelegramChatId(chatId).orElseGet(() -> {
            User user = new User();
            user.setTelegramUsername(userName);
            user.setTelegramChatId(chatId);
            System.out.println("REGISTER USER");
            return userRepository.save(user);
        });
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
