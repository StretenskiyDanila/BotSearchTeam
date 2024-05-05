package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.pipeline.TelegramBotPipeline;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotStart implements TelegramBotPipeline {

    private TelegramLongPollingBot telegramBot;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update, User user) {
        SendMessage method = new SendMessage();
        method.setChatId(user.getTelegramChatId());
        method.setText("Добро пожаловать! Вы хотите найти команду или ищете участника к себе в команду?");
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(new InlineKeyboardButton("Хочу в команду!"));
        buttonList.add(new InlineKeyboardButton("Уже есть команда, ищу участника!"));

        inlineKeyboardMarkup.setKeyboard(List.of(buttonList));
        telegramBot.executeAsync(method);
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
