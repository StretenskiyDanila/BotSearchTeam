package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.Team;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.TeamService;
import com.searchteam.bot.service.TelegramService;
import com.searchteam.bot.service.UserService;
import com.searchteam.bot.utils.TelegramChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class AddTeam extends AbstractTelegramBotPipeline {

    private final TelegramBot telegramBot;
    private final TelegramService telegramService;
    private final TeamService teamService;
    private final UserService userService;

    @Override
    protected void onMessageReceived(Message message, User user) {
        String[] users = message.getText().split("\n");
        Team team = teamService.findTeamByUser(user).get();
        for (String username : users) {
            userService.findByUsername(username).ifPresentOrElse(usr -> {
                usr.setTeam(team);
            }, () -> {
                SendMessage msg = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                        username + ": пользователь не зарегистрирован");
                try {
                    telegramBot.executeAsync(msg);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.CREATED_TEAM);
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        SendMessage message1 = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "На каждой строке ввод пользователей");
        telegramBot.executeAsync(message1);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.ADD_TEAM;
    }
}
