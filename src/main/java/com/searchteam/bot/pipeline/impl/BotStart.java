package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.utils.TelegramChatUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotStart extends AbstractTelegramBotPipeline {

    private static final String SEARCH_TEAM = "searchTeam";
    private static final String SEARCH_MEMBER = "searchMember";
    private TelegramLongPollingBot telegramBot;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (SEARCH_MEMBER.equals(callbackId)) {
            System.out.println("SEARCH MEMBER");
        }
        if (SEARCH_TEAM.equals(callbackId)) {
            System.out.println("SEARCH TEAM");
        }
    }

    @SneakyThrows
    @Override
    public void enterPipeline(User user) {
        SendMessage message1 = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Добро пожаловать! Вы хотите найти команду или ищете участника к себе в команду?");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(createButtonWithCallback(SEARCH_TEAM, "Хочу в команду!"));

        buttonList.add(createButtonWithCallback(SEARCH_MEMBER, "Ищу участника в команду!"));

        inlineKeyboardMarkup.setKeyboard(List.of(buttonList));
        message1.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeAsync(message1);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.START;
    }

    @Autowired
    public void setTelegramBot(TelegramLongPollingBot telegramBot) {
        this.telegramBot = telegramBot;
    }
}
