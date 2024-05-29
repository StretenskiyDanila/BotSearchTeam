package com.searchteam.bot.service;

import com.searchteam.bot.entity.User;
import com.searchteam.bot.pipeline.PipelineEnum;

public interface TelegramService {

    void sendMessage(User user, String message);

    void setTelegramUserPipelineStatus(User user, PipelineEnum pipelineEnum);

}
