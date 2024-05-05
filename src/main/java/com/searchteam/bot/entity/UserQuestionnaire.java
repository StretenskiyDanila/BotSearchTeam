package com.searchteam.bot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "t_user_questionnaire")
@Getter
@Setter
public class UserQuestionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    private String questionnaireText;
    @OneToMany
    @JoinColumn(name = "user_questionnaire_id", referencedColumnName = "id")
    private List<Request> requests;


    private boolean isOpen;

}
