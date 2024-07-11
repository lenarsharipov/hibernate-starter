package com.lenarsharipov.dto;

import com.lenarsharipov.entity.PersonalInfo;
import com.lenarsharipov.entity.Role;

/*
* Обычно дто - immutable, поэтому они record
* */
public record UserReadDto(Long id,
                          PersonalInfo personalInfo,
                          String username,
                          Role role,
                          CompanyReadDto company) {
}
