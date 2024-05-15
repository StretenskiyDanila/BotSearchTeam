package com.searchteam.bot.service;

import java.util.List;
import java.util.Optional;

import com.searchteam.bot.entity.Project;

public interface ProjectService {

    Long save(Project project);

    Project update(Project project);

    Optional<Project> findById(Long id);

    List<Project> findAll();
}
