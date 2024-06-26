package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.RequestService;
import com.searchteam.bot.service.StatisticService;
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

@Component
@RequiredArgsConstructor
public class SelectedRequest extends AbstractTelegramBotPipeline {

    private static final String REJECT = "REJECT-%d";
    private static final String ACCEPT = "ACCEPT-%d";

    private final TelegramService telegramService;
    private final TelegramBot telegramBot;
    private final RequestService requestService;
    private final StatisticService statisticService;


    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (callbackId.contains("ACCEPT")) {
            long requestId = Long.parseLong(callbackId.split("-")[1]);
            var request = requestService.findById(requestId).get();
            requestService.acceptRequest(request);
            statisticService.incrementFoundUsers(request.getTeam().getTitle());
        }
        if (callbackId.contains("REJECT")) {
            long requestId = Long.parseLong(callbackId.split("-")[1]);
            var request = requestService.findById(requestId).get();
            requestService.rejectRequest(request);
        }
        telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.CHECK_REQUESTS);
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        var request = requestService.findById(user.getCurrentRequestChoice()).get();
        SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Заявка на вступление в команду:\n" + request);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(List.of(createButtonWithCallback(String.format(REJECT, request.getId()), "Отклонить")));
        rows.add(List.of(createButtonWithCallback(String.format(ACCEPT, request.getId()), "Принять")));
        rows.add(List.of(createButtonWithCallback("backToRequests", "Вернуться к заявкам")));
        inlineKeyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeAsync(message);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.SELECTED_REQUEST;
    }
}
