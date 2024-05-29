package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.entity.Team;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.StatisticService;
import com.searchteam.bot.service.TeamService;
import com.searchteam.bot.service.TelegramService;
import com.searchteam.bot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class AddTeam extends AbstractTelegramBotPipeline {

    private final TelegramService telegramService;
    private final TeamService teamService;
    private final UserService userService;
    private final StatisticService statisticService;

    @Override
    protected void onMessageReceived(Message message, User user) {
        addTeamDescription(message, user);
        telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.CREATED_TEAM);
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        telegramService.sendMessage(user,
                """
                        Введите описание команды, которое будет видно другим участникам

                        *в первой строчке название команды, на второй - описание""");
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.ADD_TEAM;
    }

    private void addTeamDescription(Message message, User user) {
        String[] teamDescriptions = parseMessage(message.getText());
        Team team = teamService.findTeamByUser(user).orElseGet(Team::new);
        team.setDescription(teamDescriptions[1]);
        team.setTeamLead(user);
        team.setOpen(true);
        team.setProjectId(user.getCurrentProjectChoice());
        team.setTitle(teamDescriptions[0]);
        teamService.update(team);

        statisticService.addTeamStatistic(teamDescriptions[0]);
        team = teamService.findTeamByUser(user).get();
        user.setCurrentTeamChoice(team.getId());
        userService.update(user);
    }

    private String[] parseMessage(String message) {
        int index = message.indexOf("\n");
        String title = message.substring(0, index);
        String description = message.substring(index);
        return new String[]{title, description};
    }

}
