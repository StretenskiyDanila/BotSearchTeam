package com.searchteam.bot.configuration;

import com.searchteam.bot.pipeline.PipelineEnum;
import com.searchteam.bot.pipeline.TelegramBotPipeline;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class PipelineConfiguration {

    @Bean
    public Map<PipelineEnum, TelegramBotPipeline> pipelineMap(Collection<TelegramBotPipeline> pipelines) {
        return pipelines.stream().collect(Collectors.toMap(TelegramBotPipeline::getPipelineEnum, Function.identity()));
    }

}
