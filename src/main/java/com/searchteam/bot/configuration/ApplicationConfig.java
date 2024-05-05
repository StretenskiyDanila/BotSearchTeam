package com.searchteam.bot.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ApplicationConfig {

    @Value("${event.name}")
    private String eventName;

    @Value("${event.count-people}")
    private String countPeople;

    @Value("${event.projects.url}")
    private String projectsUrl;

}
