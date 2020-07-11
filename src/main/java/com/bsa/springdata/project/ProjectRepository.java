package com.bsa.springdata.project;

import com.bsa.springdata.project.dto.ProjectSummaryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query("SELECT p FROM Project p " +
            "INNER JOIN p.teams t " +
            "WHERE t.technology.name = :technology ")
    List<Project> findByTechnology(String technology, Pageable pageable);

    @Query("SELECT p FROM Project p " +
            "INNER JOIN p.teams t " +
            "INNER JOIN t.users u " +
            "GROUP BY p.id " +
            "ORDER BY count(t) DESC, count(u) DESC, p.name DESC")
    List<Project> findTheBiggest(Pageable pageable);

    @Query(value =
            "SELECT p.name, count(distinct(teams)) as teamsNumber, count(u) as developersNumber,  " +
            "string_agg(distinct(tech.name), ',') as technologies " +
            "FROM projects p " +
            "INNER JOIN teams ON p.id = teams.project_id " +
            "INNER JOIN technologies tech ON teams.technology_id = tech.id " +
            "INNER JOIN users u ON teams.id = u.team_id " +
            "GROUP BY p.name " +
            "ORDER BY p.name",
            nativeQuery = true)
    List<ProjectSummaryDto> findSummary();

    @Query("SELECT count(p) FROM Project p " +
            "WHERE exists (SELECT r FROM Role r " +
            "INNER JOIN r.users u " +
            "INNER JOIN u.team t " +
            "INNER JOIN t.project proj " +
            "WHERE proj = p AND r.name = :role)")
    int findCountWithRole(@Param("role") String role);
}
