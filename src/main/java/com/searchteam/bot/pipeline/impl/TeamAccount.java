package com.searchteam.bot.pipeline.impl;

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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TeamAccount extends AbstractTelegramBotPipeline {

    private final TelegramBot telegramBot;
    private final TelegramService telegramService;


    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (callbackId.equals("requests")) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.CHECK_REQUESTS);
        }
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Ваша команда:");
        //@TODO описание команды, все участники и тд
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();

        buttonList.add(createButtonWithCallback("requests", "Посмотреть заявки в команду"));

        inlineKeyboardMarkup.setKeyboard(List.of(buttonList));
        message.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeAsync(message);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.TEAM_ACCOUNT;
    }
}
