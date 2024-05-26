package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.configuration.ApplicationConfig;
import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.Team;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.FindTeamService;
import com.searchteam.bot.service.TelegramService;
import com.searchteam.bot.service.UserService;
import com.searchteam.bot.utils.TelegramChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectChoice extends AbstractTelegramBotPipeline {

    private final String TEAM = "TEAM-%d";

    private final TelegramService telegramService;
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final FindTeamService findTeamByProjectId;
    private final ApplicationConfig applicationConfig;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (callbackId.equals("back")) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.COMPLETED_QUESTIONNAIRE);
            return;
        }
        int projectId = Integer.parseInt(callbackId.split("-")[1]);
        user.setCurrentProjectChoice(projectId);
        userService.update(user);
        telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.TEAM_CHOICE);
    }

    /*@Override
    protected void onMessageReceived(Message message, User user) {
        int projectId = Integer.parseInt(message.getText());
        List<Team> teams = findTeamByProjectId.findTeamByProjectId(projectId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (Team team : teams) {
            buttonList.add(createButtonWithCallback(String.format(TEAM, team.getId()), team.getTitle()));
            if (buttonList.size() == 3) {
                rows.add(buttonList);
                buttonList = new ArrayList<>();
            }
        }
        if (!buttonList.isEmpty()) {
            rows.add(buttonList);
        }
        rows.add(List.of(createButtonWithCallback("back", "Вернуться на прошлый шаг")));

        inlineKeyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(inlineKeyboardMarkup);
    }*/

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Введите id проекта из таблицы:" + applicationConfig.getProjectsUrl());
        telegramBot.executeAsync(message);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.PROJECT_CHOICE;
    }
}
