package com.bsa.springdata.project;

import com.bsa.springdata.project.dto.CreateProjectRequestDto;
import com.bsa.springdata.project.dto.ProjectDto;
import com.bsa.springdata.project.dto.ProjectSummaryDto;
import com.bsa.springdata.team.Team;
import com.bsa.springdata.team.TeamRepository;
import com.bsa.springdata.team.Technology;
import com.bsa.springdata.team.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TechnologyRepository technologyRepository;
    @Autowired
    private TeamRepository teamRepository;

    public List<ProjectDto> findTop5ByTechnology(String technology) {
        // TODO: Use single query to load data. Sort by number of developers in a project
        //  Hint: in order to limit the query you can either use native query with limit or Pageable interface
        //var sort = Sort.sort(Project.class).by(Project::getTeams).by(Team::getUsers).descending();
        var pageable = PageRequest.of(0, 5);
        return projectRepository
                .findByTechnology(technology, pageable)
                .stream()
                .map(ProjectDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<ProjectDto> findTheBiggest() {
        // TODO: Use single query to load data. Sort by teams, developers, project name
        //  Hint: in order to limit the query you can either use native query with limit or Pageable interface
        return projectRepository
                .findTheBiggest(PageRequest.of(0, 1))
                .stream()
                .map(ProjectDto::fromEntity)
                .findFirst();
    }

    // работает нормально, но технологии не в правильном порядке
    public List<ProjectSummaryDto> getSummary() {
        // TODO: Try to use native query and projection first. If it fails try to make as few queries as possible
        return projectRepository.findSummary();
    }

    public int getCountWithRole(String role) {
        // TODO: Use a single query
        return projectRepository.findCountWithRole(role);
    }

    public UUID createWithTeamAndTechnology(CreateProjectRequestDto createProjectRequest) {
        // TODO: Use common JPARepository methods. Build entities in memory and then persist them
        var project = Project.builder()
                .name(createProjectRequest.getProjectName())
                .description(createProjectRequest.getProjectDescription())
                .build();

        var team = Team.builder()
                .name(createProjectRequest.getTeamName())
                .area(createProjectRequest.getTeamArea())
                .room(createProjectRequest.getTeamRoom())
                .build();
        teamRepository.save(team);

        var technology = Technology.builder()
                .name(createProjectRequest.getTech())
                .description(createProjectRequest.getTechDescription())
                .link(createProjectRequest.getTechLink())
                .build();
        technologyRepository.save(technology);

        var savedProject = projectRepository.save(project);
        return savedProject.getId();
    }
}
