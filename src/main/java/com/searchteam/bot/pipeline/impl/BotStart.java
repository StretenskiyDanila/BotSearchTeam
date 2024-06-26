package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.configuration.ApplicationConfig;
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
import static com.searchteam.bot.pipeline.PipelineEnum.ADMIN_CHECK;

@Component
@RequiredArgsConstructor
public class BotStart extends AbstractTelegramBotPipeline {

    private static final String SEARCH_TEAM = "searchTeam";
    private static final String SEARCH_MEMBER = "searchMember";
    private static final String ADMIN_STATISTIC = "adminStatistic";
    private static final String START_MESSAGE = "Добро пожаловать! Это бот для поиска команды/участника по %s. " +
            "Максимальное количество человек в команде - %s.\n" +
            "Вы хотите найти команду или ищете участника к себе в команду?";

    private final TelegramService telegramService;
    private final TelegramBot telegramBot;
    private final ApplicationConfig applicationConfig;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (SEARCH_MEMBER.equals(callbackId)) {
            telegramService.setTelegramUserPipelineStatus(user, TEAM_LEAD_CHOICE_PROJECT);
        }
        if (SEARCH_TEAM.equals(callbackId)) {
            telegramService.setTelegramUserPipelineStatus(user, CREATE_QUESTIONNAIRE);
        }
        if (ADMIN_STATISTIC.equals(callbackId)) {
            telegramService.setTelegramUserPipelineStatus(user, ADMIN_CHECK);
        }
    }

    @SneakyThrows
    @Override
    public void enterPipeline(User user) {
        SendMessage message1 = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                        String.format(START_MESSAGE, applicationConfig.getEventName(), applicationConfig.getCountPeople()));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(createButtonWithCallback(SEARCH_TEAM, "Хочу в команду!"));
        buttonList.add(createButtonWithCallback(SEARCH_MEMBER, "Ищу участника в команду!"));
        buttonList.add(createButtonWithCallback(ADMIN_STATISTIC, "Посмотреть статистику работы бота (нужен пароль администратора)"));

        inlineKeyboardMarkup.setKeyboard(List.of(buttonList));
        message1.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeAsync(message1);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.START;
    }
}
