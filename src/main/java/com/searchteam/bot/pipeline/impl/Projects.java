package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.Team;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.ProjectService;
import com.searchteam.bot.service.TeamService;
import com.searchteam.bot.service.TelegramService;
import com.searchteam.bot.utils.TelegramChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Projects extends AbstractTelegramBotPipeline {

    private final TelegramService telegramService;
    private final TelegramBot telegramBot;
    private final ProjectService projectService;
    private final TeamService teamService;

    @Override
    protected void onMessageReceived(Message message, User user) {
        String[] description = message.getText().split("\n");
        addNewTeam(description, user);
    }

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.START);
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        SendMessage message1 = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Ссылка на файл со всеми проектами, введите номер проекта и описание команды?");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();

        buttonList.add(createButtonWithCallback("back", "Вернуться в меню"));

        inlineKeyboardMarkup.setKeyboard(List.of(buttonList));
        message1.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeAsync(message1);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.TEAM_LEAD_CHOICE_PROJECT;
    }

    private void addNewTeam(String[] description, User user) {
        projectService.findById(Long.parseLong(description[0])).ifPresentOrElse(project -> {
            Team team = new Team();
            team.setProject(project);
            team.setTeamLead(user);
            team.setOpen(true);
            team.setTitle(description[1]);
            team.setDescription(description[2]);
            teamService.save(team);
            user.setTeam(team);
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.ADD_TEAM);
    }, () -> {
            SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                    "Проект закрыт или удалён");
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> buttonList = new ArrayList<>();

            buttonList.add(createButtonWithCallback("back", "Вернуться в меню"));

            inlineKeyboardMarkup.setKeyboard(List.of(buttonList));
            message.setReplyMarkup(inlineKeyboardMarkup);
            try {
                telegramBot.executeAsync(message);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
