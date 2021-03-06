package com.bsa.springdata.team;

import com.bsa.springdata.project.Project;
import com.bsa.springdata.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO: Map table teams to this entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String name;

    private String room;

    private String area;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "technology_id")
    private Technology technology;
}
