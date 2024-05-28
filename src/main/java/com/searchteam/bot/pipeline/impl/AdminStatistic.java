package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.controller.TelegramBot;
import com.searchteam.bot.entity.Statistic;
import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.StatisticService;
import com.searchteam.bot.service.TelegramService;
import com.searchteam.bot.utils.TelegramChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminStatistic extends AbstractTelegramBotPipeline {

    public static final String DOWNLOAD_STATISTIC_EXCEL = "downloadStatistic";

    private final TelegramBot telegramBot;
    private final StatisticService statisticService;
    private final TelegramService telegramService;

    @Override
    protected void onCallBackReceived(String callbackId, CallbackQuery callbackQuery, User user) {
        if (DOWNLOAD_STATISTIC_EXCEL.equals(callbackId)) {
            String path = statisticService.createExcelStatistic();
            sendDocument(user.getTelegramChatId(), path);
        }
        telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.START);
    }

    @Override
    @SneakyThrows
    public void enterPipeline(User user) {
        List<Statistic> statistics = statisticService.findAllStatisticsLimited();
        SendMessage message = TelegramChatUtils.sendMessage(user.getTelegramChatId(),
                "Предоставляем сокращенную статистику:\n\n" + getPrintStatistics(statistics));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(List.of(createButtonWithCallback(DOWNLOAD_STATISTIC_EXCEL, "Скачать статистику в виде  excel-файла")));
        inlineKeyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(inlineKeyboardMarkup);
        telegramBot.executeAsync(message);
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.STATISTIC;
    }

    private String getPrintStatistics(List<Statistic> statistics) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Statistic statistic: statistics) {
            stringBuilder
                    .append(statistic.getTeamTitle())
                    .append(": ")
                    .append(statistic.getCountFoundUsers())
                    .append(" участника нашли");
        }
        return stringBuilder.toString();
    }

    @SneakyThrows
    private void sendDocument(long chatId, String filePath) {
        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setDocument(new InputFile(new java.io.File(filePath)));

        telegramBot.execute(sendDocumentRequest);
    }

}
