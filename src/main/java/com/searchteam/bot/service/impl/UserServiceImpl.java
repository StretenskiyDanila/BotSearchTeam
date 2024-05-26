package com.searchteam.bot.service.impl;

import com.searchteam.bot.entity.User;
import com.searchteam.bot.repository.UserRepository;
import com.searchteam.bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Long save(User user) {
        return userRepository.save(user).getId();
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByTelegramChatId(Long telegramChatId) {
        return userRepository.findByTelegramChatId(telegramChatId);
    }

    @Override
    public User createUser(String username, Long chatId) {
        User user = new User();
        user.setTelegramUsername(username);
        user.setTelegramChatId(chatId);
        System.out.println("REGISTER USER");
        return userRepository.save(user);

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByTelegramUsername(username);
    }
}
