package com.lenarsharipov.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("manager")
public class Manager extends User {

    private String projectName;

    @Builder
    public Manager(Long id,
                   String username,
                   PersonalInfo personalInfo,
                   Role role,
                   Company company,
                   Profile profile,
                   List<UserChat> userChats,
                   String projectName) {
        super(id, username, personalInfo, role, company, profile, userChats);
        this.projectName = projectName;
    }
}
