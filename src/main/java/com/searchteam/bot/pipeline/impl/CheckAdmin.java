package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.configuration.ApplicationConfig;
import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.TelegramService;
import com.searchteam.bot.utils.TelegramChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class CheckAdmin  extends AbstractTelegramBotPipeline {

    private final TelegramBot telegramBot;
    private final ApplicationConfig applicationConfig;
    private final TelegramService telegramService;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (callbackId.equals("back")) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.START);
        }
    }

    @Override
    @SneakyThrows
    protected void onMessageReceived(Message message, User user) {
        String password = message.getText();
        if (password.equals(applicationConfig.getPassword())) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.STATISTIC);
            return;
        }
        SendMessage message1 = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Пароль не совпадает");
        telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.START);
        telegramBot.executeAsync(message1);
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Введите пароль администратора");
        message = addBackButtons(message);
        telegramBot.executeAsync(message);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.ADMIN_CHECK;
    }

}
