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

import static com.searchteam.bot.pipeline.PipelineEnum.CREATE_QUESTIONNAIRE;
import static com.searchteam.bot.pipeline.PipelineEnum.TEAM_LEAD_CHOICE_PROJECT;

@Component
@RequiredArgsConstructor
public class BotStart extends AbstractTelegramBotPipeline {

    private static final String SEARCH_TEAM = "searchTeam";
    private static final String SEARCH_MEMBER = "searchMember";

    private final TelegramService telegramService;
    private final TelegramBot telegramBot;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (SEARCH_MEMBER.equals(callbackId)) {
            telegramService.setTelegramUserPipelineStatus(user, TEAM_LEAD_CHOICE_PROJECT);
        }
        if (SEARCH_TEAM.equals(callbackId)) {
            telegramService.setTelegramUserPipelineStatus(user, CREATE_QUESTIONNAIRE);
        }
    }

    @SneakyThrows
    @Override
    public void enterPipeline(User user) {
        SendMessage message1 = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Добро пожаловать! Вы хотите найти команду или ищете участника к себе в команду?");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(createButtonWithCallback(SEARCH_TEAM, "Хочу в команду!"));

        buttonList.add(createButtonWithCallback(SEARCH_MEMBER, "Ищу участника в команду!"));

        inlineKeyboardMarkup.setKeyboard(List.of(buttonList));
        message1.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeAsync(message1);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.START;
    }
}
