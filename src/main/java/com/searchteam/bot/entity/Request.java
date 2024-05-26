package com.searchteam.bot.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_request")
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_questionnaire_id", referencedColumnName = "id")
    private UserQuestionnaire userQuestionnaire;

    @Override
    public String toString() {
        return userQuestionnaire.getUser().getTelegramUsername() + "\n" +
                userQuestionnaire;
    }
}
