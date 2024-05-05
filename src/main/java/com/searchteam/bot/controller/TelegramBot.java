package com.searchteam.bot.controller;

import com.searchteam.bot.configuration.BotConfig;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.pipeline.TelegramBotPipeline;
import com.searchteam.bot.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final TelegramBotsApi telegramBotsApi;

    private final UserRepository userRepository;

    private Map<PipelineEnum, TelegramBotPipeline> pipelineMap;

    @SneakyThrows
    @PostConstruct
    public void init() {
        telegramBotsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        String userName;
        Long chatId;
        if (update.hasCallbackQuery()) {
            userName = update.getCallbackQuery().getFrom().getUserName();
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else if (update.hasMessage()) {
            userName = update.getMessage().getFrom().getUserName();
            chatId = update.getMessage().getChatId();
        } else {
            return;
        }
        User user = userRepository.findFirstByTelegramChatId(chatId).orElseGet(() -> {
            User user1 = new User();
            user1.setTelegramUsername(userName);
            user1.setTelegramChatId(chatId);
            System.out.println("REGISTER USER");
            return userRepository.save(user1);
        });

        pipelineMap.get(user.getPipelineStatus()).onUpdateReceived(update, user);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Autowired
    public void setPipelineMap(@Lazy Map<PipelineEnum, TelegramBotPipeline> pipelineMap) {
        this.pipelineMap = pipelineMap;
    }
}
