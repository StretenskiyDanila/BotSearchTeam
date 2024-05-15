package com.searchteam.bot.pipeline;

import com.searchteam.bot.entity.User;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

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

}
