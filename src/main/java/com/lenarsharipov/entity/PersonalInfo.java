package com.lenarsharipov.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Embeddable // говорит, что компонент встраиваемый
public class PersonalInfo {

    private String firstname;

    private String lastname;

//    @Column(name = "birth_date")
    private LocalDate birthDate;
}
