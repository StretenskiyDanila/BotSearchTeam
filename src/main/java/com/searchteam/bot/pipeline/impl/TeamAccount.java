package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.Team;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.RequestService;
import com.searchteam.bot.service.TeamService;
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

    private static final String EDIT_TEAM = "editTeam";
    private static final String DELETE_TEAM = "deleteTeam";
    private static final String SHOW_REQUESTS = "showRequests";
    private static final String CLOSE_TEAM = "closeTeam";
    private static final String OPEN_TEAM = "openTeam";

    private final TelegramBot telegramBot;
    private final TelegramService telegramService;
    private final TeamService teamService;
    private final RequestService requestService;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (EDIT_TEAM.equals(callbackId)) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.ADD_TEAM);
            return;
        }
        if (SHOW_REQUESTS.equals(callbackId)) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.CHECK_REQUESTS);
            return;
        }
        if (CLOSE_TEAM.equals(callbackId)) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.CLOSE_TEAM);
            return;
        }
        if (OPEN_TEAM.equals(callbackId)) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.OPEN_TEAM);
            return;
        }
        if (DELETE_TEAM.equals(callbackId)) {
            teamService.deleteTeam(user.getCurrentTeamChoice());
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.START);
        }
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        Team team = teamService.findTeamByUser(user)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Ваша команда:\n" + team);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(createButtonWithCallback(EDIT_TEAM, "Редактировать описание команды"));
        buttonList.add(createButtonWithCallback(DELETE_TEAM, "Удалить команду"));
        buttonList.add(createButtonWithCallback(SHOW_REQUESTS, "Посмотреть заявки в команду"));
        if (team.isOpen()) {
            buttonList.add(createButtonWithCallback(CLOSE_TEAM, "Закрыть команду для поиска"));
        } else {
            buttonList.add(createButtonWithCallback(OPEN_TEAM, "Открыть команду для поиска"));
        }
        inlineKeyboardMarkup.setKeyboard(List.of(buttonList));
        message.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeAsync(message);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.TEAM_ACCOUNT;
    }
}
