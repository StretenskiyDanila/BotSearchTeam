package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.Team;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.FindTeamService;
import com.searchteam.bot.service.TeamService;
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
public class ChoiceTeam extends AbstractTelegramBotPipeline {

    private final static String TEAM = "TEAM-%d";

    private final FindTeamService findTeamService;
    private final TelegramService telegramService;
    private final TelegramBot telegramBot;
    private final UserService userService;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (callbackId.equals("back")) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.PROJECT_CHOICE);
            return;
        }
        long teamId = Long.parseLong(callbackId.split("-")[1]);
        user.setCurrentTeamChoice(teamId);
        userService.update(user);
        telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.SELECTED_TEAM);
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Выберите команду");
        List<Team> allTeams = findTeamService.findTeamByProjectId(user.getCurrentProjectChoice());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for(Team team : allTeams) {
            buttonList.add(createButtonWithCallback(String.format(TEAM, team.getId()), team.getTitle()));
            if(buttonList.size() == 3) {
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
        telegramBot.executeAsync(message);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.TEAM_CHOICE;
    }
}
