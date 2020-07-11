package com.bsa.springdata.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
    @Transactional
    @Modifying
    @Query(value =
            "UPDATE teams SET technology_id = (SELECT id FROM technologies tech WHERE tech.name = :newTechnology) " +
            "FROM teams t LEFT JOIN technologies tech ON tech.id = t.technology_id " +
            "WHERE teams.id = t.id AND tech.name = :oldTechnology AND (SELECT count(*) FROM users WHERE team_id = t.id) < :devsNumber",
            nativeQuery = true)
    void updateTechnology(@Param("devsNumber") Long devsNumber,
                          @Param("oldTechnology") String oldTechnologyName,
                          @Param("newTechnology") String newTechnology);

    @Transactional
    @Modifying
    @Query(value =
            "UPDATE teams SET name = concat_ws('_', teams.name, proj.name, tech.name)  " +
            "FROM (teams t " +
            "INNER JOIN projects proj ON proj.id = t.project_id " +
            "INNER JOIN technologies tech ON tech.id = t.technology_id) " +
            "WHERE teams.id = t.id AND teams.name = :name ",
            nativeQuery = true)
   void normalizeName(@Param("name") String name);

    Optional<Team> findByName(String name);

    int countByTechnologyName(String name);
}
