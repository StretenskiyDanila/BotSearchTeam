package com.searchteam.bot.pipeline;

import com.searchteam.bot.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramBotPipeline {

    void enterPipeline(User user);

    void onUpdateReceived(Update update, User user);

    PipelineEnum getPipelineEnum();

}
