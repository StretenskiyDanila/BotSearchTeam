package com.searchteam.bot.repository;

import com.searchteam.bot.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findTeamByTeamLead_Id(Long userId);

//    @Query(nativeQuery = true, value = "SELECT * FROM t_team" +
//            " WHERE id NOT IN " +
//            "(SELECT team_id FROM t_request WHERE user_questionnaire_id = ?1 GROUP BY team_id)")
//    List<Team> findFreeTeamsForUserQuestionnaire(Long userQuestionnaireId);

    @Query(value =
            "select t from Team t JOIN t.project p " +
            "WHERE " +
            "t.id not in " +
            "(select r.team.id FROM Request r WHERE r.userQuestionnaire.id=?1 GROUP BY r.team.id)")
    Page<Team> findFreeTeamsForUserQuestionnaire(Long userQuestionnaireId, Pageable pageable);

}
