package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateQuestionnaire extends AbstractTelegramBotPipeline {
    private final TelegramService telegramService;

    @Override
    public void enterPipeline(User user) {
        telegramService.sendMessage(user, "CREATE QUESTIONNAIRE");
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.CREATE_QUESTIONNAIRE;
    }
}
