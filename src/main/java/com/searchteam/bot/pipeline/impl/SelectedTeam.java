package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.*;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.*;
import com.searchteam.bot.utils.TelegramChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SelectedTeam extends AbstractTelegramBotPipeline {

    private static final String SUBMITTED_QUESTIONNAIRE = "sumbQuestionnaire";

    private final TelegramService telegramService;
    private final TelegramBot telegramBot;
    private final TeamService teamService;
    private final QuestionnaireService questionnaireService;
    private final RequestService requestService;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (callbackId.equals("back")) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.TEAM_CHOICE);
        }
        if (callbackId.equals(SUBMITTED_QUESTIONNAIRE)) {
            sendRequest(user);
            if (!user.getPipelineStatus().equals(PipelineEnum.TEAM_CHOICE)) {
                telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.SUBMITTED_QUESTIONNAIRE);
            }
        }
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        Team team = teamService.findById(Long.valueOf(user.getCurrentTeamChoice())).get();
        SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Описание команды\n" + team.toString());

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(createButtonWithCallback(SUBMITTED_QUESTIONNAIRE, "Отправить анкету в данную команду"));
        buttonList.add(createButtonWithCallback("back", "Вернуться на прошлый шаг"));

        inlineKeyboardMarkup.setKeyboard(List.of(buttonList));
        message.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeAsync(message);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.SELECTED_TEAM;
    }

    private void sendRequest(User user) {
        UserQuestionnaire questionnaire = questionnaireService.findByUserId(user.getId()).orElseThrow(RuntimeException::new);
        var request = requestService.findByUserQuestionnaireId(questionnaire.getId());
        if (request.isPresent()) {
            SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                    "Вы уже отправили анкету в данный проект!");
            try {
                telegramBot.executeAsync(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.TEAM_CHOICE);
            return;
        }
        Request newRequest = new Request();
        Team team = teamService.findById(Long.valueOf(user.getCurrentTeamChoice())).orElseThrow(RuntimeException::new);

        newRequest.setTeam(team);
        newRequest.setUserQuestionnaire(questionnaire);
        requestService.saveRequest(newRequest);
    }
}
