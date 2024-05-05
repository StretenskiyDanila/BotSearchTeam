package com.searchteam.bot.service.impl;

import com.searchteam.bot.entity.Project;
import com.searchteam.bot.repository.ProjectRepository;
import com.searchteam.bot.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public Long save(Project project) {
        return projectRepository.save(project).getId();
    }

    @Override
    public Project update(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }
}
