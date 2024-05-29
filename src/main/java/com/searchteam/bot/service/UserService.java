package com.searchteam.bot.service;

import java.util.Optional;

import com.searchteam.bot.entity.User;

public interface UserService {

    Long save(User user);

    User update(User user);

    Optional<User> findById(Long id);

    Optional<User> findByTelegramChatId(Long telegramChatId);

    User createUser(String username, Long chatId);

    Optional<User> findByUsername(String username);
}
