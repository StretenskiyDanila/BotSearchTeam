package com.searchteam.bot.service.impl;

import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.TelegramService;
import com.searchteam.bot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private TelegramBot telegramBot;
    private final UserService userService;

    @SneakyThrows
    @Override
    public void sendMessage(User user, String message) {
        telegramBot.executeAsync(new SendMessage(user.getTelegramChatId().toString(), message));
    }

    @Override
    public void setTelegramUserPipelineStatus(User user, PipelineEnum pipelineEnum) {
        user.setPipelineStatus(pipelineEnum);
        User update = userService.update(user);
        telegramBot.getPipelineMap().get(pipelineEnum).enterPipeline(update);
    }

    @Autowired
    public void setTelegramBot(@Lazy TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
}
