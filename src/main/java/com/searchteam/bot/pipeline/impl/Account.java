package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.entity.UserQuestionnaire;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.QuestionnaireService;
import com.searchteam.bot.service.TelegramService;
import com.searchteam.bot.service.UserService;
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
public class Account extends AbstractTelegramBotPipeline {

    private static final String EDIT_QUESTIONNAIRE = "editQuestionnaire";
    private static final String DELETE_QUESTIONNAIRE = "deleteQuestionnaire";
    private static final String PROJECT_CHOICE = "projectChoice";

    private final TelegramService telegramService;
    private final TelegramBot telegramBot;
    private final QuestionnaireService questionnaireService;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if(PROJECT_CHOICE.equals(callbackId)) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.PROJECT_CHOICE);
        }
        if(EDIT_QUESTIONNAIRE.equals(callbackId)) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.CREATE_QUESTIONNAIRE);
        }
        if(DELETE_QUESTIONNAIRE.equals(callbackId)) {
            questionnaireService.deleteByUserId(user.getId());
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.START);
        }
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        UserQuestionnaire questionnaire = questionnaireService.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Questionnaire not found"));
        SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Ваша анкета:\n" + questionnaire);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(createButtonWithCallback(EDIT_QUESTIONNAIRE, "Редактировать анкету"));
        buttonList.add(createButtonWithCallback(DELETE_QUESTIONNAIRE, "Удалить анкету"));
        buttonList.add(createButtonWithCallback(PROJECT_CHOICE, "Найти команду"));

        inlineKeyboardMarkup.setKeyboard(List.of(buttonList));
        message.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeAsync(message);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.ACCOUNT;
    }
}
