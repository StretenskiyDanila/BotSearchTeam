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
    private static final String NEXT_PAGE = "next";
    private static final String PREV_PAGE = "previous";
    private static final String BACK_TO_COMMAND = "backToCommand";

    private final TelegramBot telegramBot;
    private final TelegramService telegramService;
    private final RequestService requestService;
    private final UserService userService;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (callbackId.contains(NEXT_PAGE) || callbackId.contains(PREV_PAGE)) {
            updateRequestsMenu(callbackId, user);
            return;
        }
        if(callbackId.equals(BACK_TO_COMMAND)) {
            telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.TEAM_ACCOUNT);
            return;
        }

        if(!callbackId.isEmpty()) {
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

        Integer page = user.getCurrentPage();
        List<Request> requests = requestService.getAllRequestsTeam(user.getTeam().getId());
        int pageSize = 5;
        int totalPages = (int) Math.ceil((double) requests.size() / pageSize);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, requests.size());

        for (int i = startIndex; i < endIndex; i++) {
            Request request = requests.get(i);
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            buttonList.add(createButtonWithCallback(String.format(REQUEST_ID, request.getId()), "Заявка " + request.getId()));
            rows.add(buttonList);
        }

        List<InlineKeyboardButton> navigationButtons = new ArrayList<>();
        if (page > 0) {
            navigationButtons.add(createButtonWithCallback(PREV_PAGE, "Предыдущая страница"));
        }
        if (page < totalPages - 1) {
            navigationButtons.add(createButtonWithCallback(NEXT_PAGE, "Следующая страница"));
        }
        if (!navigationButtons.isEmpty()) {
            rows.add(navigationButtons);
        }

        rows.add(List.of(createButtonWithCallback(BACK_TO_COMMAND, "Вернуться к команде")));

        inlineKeyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeAsync(message);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.CHECK_REQUESTS;
    }

    private void updateRequestsMenu(String callbackId, User user) {
        var page = user.getCurrentPage();
        if (callbackId.equals(PREV_PAGE)) {
            page--;
        } else {
            page++;
        }
        user.setCurrentPage(page);
        userService.update(user);
        enterPipeline(user);
    }
}
