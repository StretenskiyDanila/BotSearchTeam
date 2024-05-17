package com.searchteam.bot.pipeline.impl;

import com.searchteam.bot.entity.User;
import com.searchteam.bot.entity.UserQuestionnaire;
import com.searchteam.bot.pipeline.AbstractTelegramBotPipeline;
import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.service.QuestionnaireService;
import com.searchteam.bot.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class CreateQuestionnaire extends AbstractTelegramBotPipeline {

    private final TelegramService telegramService;
    private final QuestionnaireService questionnaireService;

    @Override
    protected void onMessageReceived(Message message, User user) {
        addUserQuestionnaire(message, user);
        telegramService.setTelegramUserPipelineStatus(user, PipelineEnum.COMPLETED_QUESTIONNAIRE);
    }

    @Override
    public void enterPipeline(User user) {
        telegramService.sendMessage(user, "Напиши анкету");
    }

    @Override
    public PipelineEnum getPipelineEnum() {
        return PipelineEnum.CREATE_QUESTIONNAIRE;
    }

    private void addUserQuestionnaire(Message message, User user) {
        String messageText = message.getText();
        UserQuestionnaire ques = questionnaireService.findByUserId(user.getId()).get();
        ques.setUser(user);
        ques.setQuestionnaireText(messageText);
        ques.setOpen(true);
        questionnaireService.update(ques);
    }
}
