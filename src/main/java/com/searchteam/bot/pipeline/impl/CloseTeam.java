package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.Team;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.TeamService;
import com.searchteam.bot.service.TelegramService;
import com.searchteam.bot.utils.TelegramChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class CloseTeam extends AbstractTelegramBotPipeline {

    private final TelegramBot telegramBot;
    private final TelegramService telegramService;
    private final TeamService teamService;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (callbackId.equals("back")) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.TEAM_ACCOUNT);
        }
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        Team team = teamService.findTeamByUser(user)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        team.setOpen(false);
        teamService.update(team);
        SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Ваша команда скрыта из поиска");
        message = addBackButtons(message);
        telegramBot.executeAsync(message);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.CLOSE_TEAM;
    }

}
