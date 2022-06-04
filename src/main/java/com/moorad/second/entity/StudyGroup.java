package com.moorad.second.entity;

import com.moorad.second.entity.defaults.DefaultFields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "study_group")
@EqualsAndHashCode(callSuper = true)

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudyGroup extends DefaultFields {
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;
    private String name;
    private String description;
    private String requirement;
    private String contacts;
    private LocalDate created;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> members;
    @ManyToOne(fetch = FetchType.LAZY)
    private Interest interest;
    private int count;

    public StudyGroup(User creator, String name, String description, String requirement, String contacts, LocalDate created, List<User> members, Interest interest) {
        this.creator = creator;
        this.name = name;
        this.description = description;
        this.requirement = requirement;
        this.contacts = contacts;
        this.created = created;
        this.members = members;
        this.interest = interest;
    }
}
