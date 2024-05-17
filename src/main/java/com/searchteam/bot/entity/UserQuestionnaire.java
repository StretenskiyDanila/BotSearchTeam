package com.searchteam.bot.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "t_user_questionnaire")
@Getter
@Setter
@ToString(exclude = {"requests"})
public class UserQuestionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String questionnaireText;

    @OneToMany
    @JoinColumn(name = "user_questionnaire_id", referencedColumnName = "id")
    private List<Request> requests;

    @Column(name = "is_open")
    private boolean isOpen;

    @Override
    public String toString() {
        return questionnaireText;
    }
}
