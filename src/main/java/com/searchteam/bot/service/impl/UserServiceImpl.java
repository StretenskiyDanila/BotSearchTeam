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
        return userRepository.findFirstByTelegramChatId(telegramChatId);
    }
}