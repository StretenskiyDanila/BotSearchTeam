package com.searchteam.bot.pipeline;

import com.searchteam.bot.entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTelegramBotPipeline implements TelegramBotPipeline {


    @Override
    public void onUpdateReceived(Update update, User user) {
        if (update.hasMessage()) {
            onMessageReceived(update.getMessage(), user);
        }
        if (update.hasCallbackQuery()) {
            onCallBackReceived(update.getCallbackQuery().getData(), update.getCallbackQuery(), user);
        }
    }

    protected InlineKeyboardButton createButtonWithCallback(String buttonId, String buttonMessage) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setCallbackData(buttonId);
        button.setText(buttonMessage);

        return button;
    }

    protected void onMessageReceived(Message message, User user) {
    }

    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
    }


    protected SendMessage addBackButtons(SendMessage message) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(createButtonWithCallback("back", "Вернуться на главную!"));
        inlineKeyboardMarkup.setKeyboard(List.of(buttonList));
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

}
