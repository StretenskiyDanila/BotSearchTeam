package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.Request;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.RequestService;
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
public class Requests extends AbstractTelegramBotPipeline {

    private static final String REQUEST_ID = "REQUEST-%d";

    private final TelegramBot telegramBot;
    private final TelegramService telegramService;
    private final RequestService requestService;
    private final UserService userService;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (callbackId.equals("back")) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.TEAM_ACCOUNT);
            return;
        }
        if(callbackId != null) {
            long requestId = Integer.parseInt(callbackId.split("-")[1]);
            user.setCurrentRequestChoice(requestId);
            userService.update(user);
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.SELECTED_REQUEST);
        }

    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Все заявки на вступление в команду");
        List<Request> requests = requestService.getAllRequestsTeam(user.getTeam().getId());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for(Request request : requests) {
            buttonList.add(createButtonWithCallback(String.format(REQUEST_ID, request.getId()), request.getId().toString()));
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
        return PipelineEnum.CHECK_REQUESTS;
    }
}
